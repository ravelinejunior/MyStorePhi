package br.com.raveline.mystorephi.presentation.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import br.com.raveline.mystorephi.R
import br.com.raveline.mystorephi.databinding.FragmentLoginBinding
import br.com.raveline.mystorephi.presentation.viewmodel.UserViewModel
import br.com.raveline.mystorephi.presentation.viewmodel.viewmodel_factory.UserViewModelFactory
import br.com.raveline.mystorephi.utils.CustomDialogLoading
import br.com.raveline.mystorephi.utils.SystemFunctions.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var userViewModelFactory: UserViewModelFactory
    private val userViewModel: UserViewModel by viewModels {
        userViewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        initObserver()
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            buttonLoginFragment.setOnClickListener {
                getFieldsAndLogin()
            }

            toolbarLoginFragment.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            textViewLoginFragmentSignup.setOnClickListener {
                findNavController().navigate(R.id.action_LoginFragment_to_signupFragment)
            }
        }

    }

    private fun initObserver() {
        lifecycleScope.launch {
            userViewModel.uiStateFlow.collect { state ->
                when (state) {
                    UserViewModel.UiState.Initial -> {

                    }
                    UserViewModel.UiState.Loading -> {
                        CustomDialogLoading().startLoading(requireActivity())
                    }
                    UserViewModel.UiState.Error -> {
                        CustomDialogLoading().dismissLoading()
                        Toast.makeText(
                            context,
                            getString(R.string.something_wrong_msg),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    UserViewModel.UiState.NoConnection -> {
                        CustomDialogLoading().dismissLoading()
                        Toast.makeText(
                            context,
                            getString(R.string.no_internet_connection_msg),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    UserViewModel.UiState.Success -> {
                        CustomDialogLoading().dismissLoading()
                        findNavController().navigate(R.id.action_LoginFragment_to_homeFragment)
                    }

                }
            }
        }
    }

    private fun getFieldsAndLogin() {
        hideKeyboard()
        binding.apply {
            val email = textInputLoginFragmentEmail.text?.toString()?.trim()
            val password = textInputLoginFragmentPassword.text?.toString()?.trim()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(context, getString(R.string.email_empty_msg), Toast.LENGTH_SHORT)
                    .show()

                return
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(context, getString(R.string.password_empty_msg), Toast.LENGTH_SHORT)
                    .show()

                return
            }

            userViewModel.loginUser(email!!, password!!)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}