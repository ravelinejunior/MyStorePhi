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
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.raveline.mystorephi.R
import br.com.raveline.mystorephi.databinding.FragmentItemBinding
import br.com.raveline.mystorephi.presentation.adapter.ItemAdapterAllListedItems
import br.com.raveline.mystorephi.presentation.listener.UiState
import br.com.raveline.mystorephi.presentation.viewmodel.HomeViewModel
import br.com.raveline.mystorephi.presentation.viewmodel.viewmodel_factory.HomeViewModelFactory
import br.com.raveline.mystorephi.utils.CustomDialogLoading
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class ItemFragment : Fragment() {

    private var _binding: FragmentItemBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var homeViewModelFactory: HomeViewModelFactory
    private val homeViewModel: HomeViewModel by viewModels { homeViewModelFactory }

    private val itemsAdapter: ItemAdapterAllListedItems by lazy {
        ItemAdapterAllListedItems(homeViewModel)
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
        _binding = FragmentItemBinding.inflate(inflater, container, false)
        homeViewModel.getAllListedItems()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    private fun initObservers() {
        lifecycleScope.launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.uiStateFlow.collectLatest { state ->
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
        }

        lifecycleScope.launchWhenResumed {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                homeViewModel.apply {
                    allItemsFlow.collect { itemsList ->
                        if (itemsList.isNotEmpty()) {
                            itemsAdapter.setData(itemsList)
                            initRecyclerView()
                        }
                    }
                }
            }
        }
    }

    private fun initRecyclerView() {
        binding.recyclerViewItemFragment.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = itemsAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}