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
import br.com.raveline.mystorephi.databinding.FragmentAddAddressBinding
import br.com.raveline.mystorephi.presentation.listener.UiState
import br.com.raveline.mystorephi.presentation.viewmodel.UserViewModel
import br.com.raveline.mystorephi.presentation.viewmodel.viewmodel_factory.UserViewModelFactory
import br.com.raveline.mystorephi.utils.CustomDialogLoading
import br.com.raveline.mystorephi.utils.SystemFunctions.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class AddAddressFragment : Fragment() {

    private var addressBinding: FragmentAddAddressBinding? = null
    private val binding get() = addressBinding!!

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
        addressBinding = FragmentAddAddressBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()

        binding.toolbarAddAddressFragment.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.buttonSignupFragment.setOnClickListener {
            getFieldsAndSaveAddress()
        }

    }

    private fun getFieldsAndSaveAddress() {
        hideKeyboard()
        binding.apply {
            val nameAddress = textInputAddAddressFragmentName.text?.toString()?.trim()
            val cityAddress = textInputAddAddressFragmentCity.text?.toString()?.trim()
            val address = textInputAddAddressFragmentDescription.text?.toString()?.trim()
            val zipCodeAddress = textInputAddAddressFragmentPostalCode.text?.toString()?.trim()
            val number = textInputAddAddressFragmentAddressNumber.text?.toString()?.trim()

            if (TextUtils.isEmpty(nameAddress)) {
                Toast.makeText(
                    context,
                    getString(R.string.address_name_empty_msg),
                    Toast.LENGTH_SHORT
                )
                    .show()

                return
            }

            if (TextUtils.isEmpty(cityAddress)) {
                Toast.makeText(
                    context,
                    getString(R.string.city_address_empty_msg),
                    Toast.LENGTH_SHORT
                )
                    .show()

                return
            }
            if (TextUtils.isEmpty(address)) {
                Toast.makeText(
                    context,
                    getString(R.string.address_description_empty_msg),
                    Toast.LENGTH_SHORT
                )
                    .show()

                return
            }

            if (TextUtils.isEmpty(zipCodeAddress)) {
                Toast.makeText(
                    context,
                    getString(R.string.address_zip_code_empty_msg),
                    Toast.LENGTH_SHORT
                )
                    .show()

                return
            }

            if (TextUtils.isEmpty(number)) {
                Toast.makeText(
                    context,
                    getString(R.string.address_number_empty_msg),
                    Toast.LENGTH_SHORT
                )
                    .show()

                return
            }

            userViewModel.addAddress(
                nameAddress!!,
                cityAddress!!,
                address!!,
                zipCodeAddress!!,
                number!!
            )


        }
    }

    private fun initObserver() {
        lifecycleScope.launch {
            userViewModel.uiStateFlow.collect { state ->
                when (state) {
                    UiState.Initial -> {

                    }
                    UiState.Loading -> {
                        CustomDialogLoading().startLoading(requireActivity())
                    }
                    UiState.Error -> {
                        CustomDialogLoading().dismissLoading()
                        Toast.makeText(
                            context,
                            getString(R.string.all_fields_must_be_filled),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    UiState.NoConnection -> {
                        CustomDialogLoading().dismissLoading()
                        Toast.makeText(
                            context,
                            getString(R.string.no_internet_connection_msg),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    UiState.Success -> {
                        CustomDialogLoading().dismissLoading()
                        findNavController().popBackStack()
                    }

                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        addressBinding = null
    }

}