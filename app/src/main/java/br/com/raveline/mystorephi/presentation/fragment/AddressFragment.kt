package br.com.raveline.mystorephi.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.raveline.mystorephi.R
import br.com.raveline.mystorephi.databinding.FragmentAddressBinding
import br.com.raveline.mystorephi.presentation.adapter.ItemAdapterAddress
import br.com.raveline.mystorephi.presentation.listener.UiState
import br.com.raveline.mystorephi.presentation.viewmodel.UserViewModel
import br.com.raveline.mystorephi.presentation.viewmodel.viewmodel_factory.UserViewModelFactory
import br.com.raveline.mystorephi.utils.CustomDialogLoading
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AddressFragment : Fragment() {

    private var addressBinding: FragmentAddressBinding? = null
    private val binding get() = addressBinding!!

    @Inject
    lateinit var userViewModelFactory: UserViewModelFactory
    private val userViewModel: UserViewModel by viewModels {
        userViewModelFactory
    }

    private val addressAdapter by lazy {
        ItemAdapterAddress(userViewModel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        addressBinding = FragmentAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarAddressFragment.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.buttonAddressFragmentAddAddress.setOnClickListener {
            findNavController().navigate(R.id.action_addressFragment_to_addAddressFragment)
        }

        binding.buttonAddressFragmentContinue.setOnClickListener {
            findNavController().navigate(R.id.action_addressFragment_to_paymentFragment)
        }

        initObserver()
    }

    private fun initObserver() {
        lifecycleScope.launch {
            userViewModel.uiStateFlow.collect { state ->
                when (state) {
                    UiState.Initial -> {
                        userViewModel.getSavedAddresses()
                    }
                    UiState.Loading -> {
                        CustomDialogLoading().startLoading(requireActivity())
                    }
                    UiState.Error -> {
                        CustomDialogLoading().dismissLoading()
                        Toast.makeText(
                            context,
                            getString(R.string.something_wrong_msg),
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

                    }

                }
            }
        }

        lifecycleScope.launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                userViewModel.addressFlow.collectLatest { addressList ->
                    if (addressList.isNotEmpty()) {
                        addressAdapter.setData(addressList)
                        initRecyclerView()
                    }
                }
            }
        }
    }

    private fun initRecyclerView() {
        binding.recyclerViewAddressFragment.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = addressAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        addressBinding = null
    }

}