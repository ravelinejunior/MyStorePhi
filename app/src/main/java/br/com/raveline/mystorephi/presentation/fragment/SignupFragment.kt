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
import br.com.raveline.mystorephi.databinding.FragmentSignupBinding
import br.com.raveline.mystorephi.presentation.listener.NetworkListeners
import br.com.raveline.mystorephi.presentation.viewmodel.UserViewModel
import br.com.raveline.mystorephi.presentation.viewmodel.viewmodel_factory.UserViewModelFactory
import br.com.raveline.mystorephi.utils.CustomDialogLoading
import br.com.raveline.mystorephi.utils.SystemFunctions.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SignupFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: UserViewModelFactory

    @Inject
    lateinit var networkListeners: NetworkListeners

    private val userViewModel: UserViewModel by viewModels {
        viewModelFactory
    }

    private var _signupBinding: FragmentSignupBinding? = null
    private val signupBinding get() = _signupBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _signupBinding = FragmentSignupBinding.inflate(inflater, container, false)

        return signupBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()

        signupBinding.apply {
            buttonSignupFragment.setOnClickListener {
                getFieldsAndRegister()
            }

            toolbarSignupFragment.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            textViewGotoLoginFragmentSignup.setOnClickListener {
                findNavController().navigate(R.id.action_signupFragment_to_LoginFragment)
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
                        findNavController().navigate(R.id.action_signupFragment_to_homeFragment)
                    }

                }
            }
        }
    }

    private fun getFieldsAndRegister() {
        hideKeyboard()
        signupBinding.apply {
            val email = textInputSignupFragmentEmail.text?.toString()?.trim()
            val name = textInputSignupFragmentName.text?.toString()?.trim()
            val phone = textInputSignupFragmentPhoneNumber.text?.toString()?.trim()
            val password = textInputSignupFragmentPassword.text?.toString()?.trim()

            if (TextUtils.isEmpty(name)) {
                Toast.makeText(context, getString(R.string.name_empty_msg), Toast.LENGTH_SHORT)
                    .show()

                return
            }

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(context, getString(R.string.email_empty_msg), Toast.LENGTH_SHORT)
                    .show()

                return
            }

            if (TextUtils.isEmpty(phone)) {
                Toast.makeText(context, getString(R.string.phone_empty_msg), Toast.LENGTH_SHORT)
                    .show()

                return
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(context, getString(R.string.password_empty_msg), Toast.LENGTH_SHORT)
                    .show()

                return
            }

            userViewModel.registerUser(email!!, password!!, name!!, phone!!)

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _signupBinding = null
    }
}