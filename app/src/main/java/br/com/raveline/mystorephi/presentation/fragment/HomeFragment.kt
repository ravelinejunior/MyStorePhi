package br.com.raveline.mystorephi.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.raveline.mystorephi.R
import br.com.raveline.mystorephi.databinding.FragmentHomeBinding
import br.com.raveline.mystorephi.presentation.adapter.ItemAdapterBestSellCards
import br.com.raveline.mystorephi.presentation.adapter.ItemAdapterFeaturesCards
import br.com.raveline.mystorephi.presentation.adapter.ItemAdapterHomeCards
import br.com.raveline.mystorephi.presentation.listener.UiState
import br.com.raveline.mystorephi.presentation.viewmodel.HomeViewModel
import br.com.raveline.mystorephi.presentation.viewmodel.viewmodel_factory.HomeViewModelFactory
import br.com.raveline.mystorephi.utils.CustomDialogLoading
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    @Inject
    lateinit var homeViewModelFactory: HomeViewModelFactory
    private val homeViewModel: HomeViewModel by viewModels { homeViewModelFactory }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var auth: FirebaseAuth

    private val homeCardsAdapter: ItemAdapterHomeCards by lazy {
        ItemAdapterHomeCards()
    }

    private val featureCardsAdapter: ItemAdapterFeaturesCards by lazy {
        ItemAdapterFeaturesCards()
    }

    private val bestSellCardsAdapter: ItemAdapterBestSellCards by lazy {
        ItemAdapterBestSellCards()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.textViewHomeFragmentCategories.setOnClickListener {
            auth.signOut()
            findNavController().navigate(R.id.action_homeFragment_to_MainFragment)
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.recyclerViewHomeFragmentCategories.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = homeCardsAdapter
        }

        binding.recyclerViewHomeFragmentFeatured.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = featureCardsAdapter
        }

        binding.recyclerViewHomeFragmentBestSell.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = bestSellCardsAdapter
        }

    }

    private fun initObservers() {
        lifecycleScope.launchWhenStarted {
            homeViewModel.uiStateFlow.collect { state ->
                when (state) {
                    UiState.Initial -> {
                        homeViewModel.getCategories()
                        homeViewModel.getFeatures()
                        homeViewModel.getBestSells()
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

        lifecycleScope.launch {
            homeViewModel.categoriesFlow.collectLatest { categories ->
                if (categories.isNotEmpty()) {
                    homeCardsAdapter.setAdapter(categories)
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            homeViewModel.featuresFlow.collectLatest { features ->
                if (features.isNotEmpty()) {
                    featureCardsAdapter.setAdapter(features)
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            homeViewModel.bestSellFlow.collectLatest { bestSells ->
                if (bestSells.isNotEmpty()) {
                    bestSellCardsAdapter.setAdapter(bestSells)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}