package br.com.raveline.mystorephi.presentation.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.Toast
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.raveline.mystorephi.R
import br.com.raveline.mystorephi.databinding.FragmentHomeBinding
import br.com.raveline.mystorephi.presentation.activity.MainActivity
import br.com.raveline.mystorephi.presentation.adapter.ItemAdapterBestSellCards
import br.com.raveline.mystorephi.presentation.adapter.ItemAdapterFeaturesCards
import br.com.raveline.mystorephi.presentation.adapter.ItemAdapterHomeCards
import br.com.raveline.mystorephi.presentation.listener.UiState
import br.com.raveline.mystorephi.presentation.viewmodel.HomeViewModel
import br.com.raveline.mystorephi.presentation.viewmodel.UserViewModel
import br.com.raveline.mystorephi.presentation.viewmodel.viewmodel_factory.HomeViewModelFactory
import br.com.raveline.mystorephi.presentation.viewmodel.viewmodel_factory.UserViewModelFactory
import br.com.raveline.mystorephi.utils.CustomDialogLoading
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
@SuppressLint("UnsafeRepeatOnLifecycleDetector")
class HomeFragment : Fragment(){

    @Inject
    lateinit var homeViewModelFactory: HomeViewModelFactory
    private val homeViewModel: HomeViewModel by viewModels { homeViewModelFactory }

    @Inject
    lateinit var userViewModelFactory: UserViewModelFactory
    private val userViewModel: UserViewModel by viewModels {
        userViewModelFactory
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var auth: FirebaseAuth

    private val homeCardsAdapter: ItemAdapterHomeCards by lazy {
        ItemAdapterHomeCards()
    }

    private val featureCardsAdapter: ItemAdapterFeaturesCards by lazy {
        ItemAdapterFeaturesCards(requireParentFragment(), homeViewModel)
    }

    private val bestSellCardsAdapter: ItemAdapterBestSellCards by lazy {
        ItemAdapterBestSellCards(requireParentFragment(), homeViewModel)
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


        binding.apply {
            toolbarHomeFragment.inflateMenu(R.menu.menu_main)

            toolbarHomeFragment.setOnMenuItemClickListener{
                userViewModel.logout(auth)
                findNavController().navigate(R.id.action_homeFragment_to_MainFragment)
                true
            }

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
        lifecycleScope.launch {
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
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.apply {
                    categoriesLiveData.observe(viewLifecycleOwner) { categories ->
                        if (categories.isNotEmpty()) {
                            homeCardsAdapter.setAdapter(categories)
                            initRecyclerView()
                        }
                    }
                }
            }

        }
        lifecycleScope.launchWhenResumed {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.apply {
                    bestSellLiveData.observe(viewLifecycleOwner) { bestSells ->
                        if (bestSells.isNotEmpty()) {
                            bestSellCardsAdapter.setAdapter(bestSells)
                            initRecyclerView()
                        }
                    }
                }
            }
        }
        lifecycleScope.launchWhenResumed {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.apply {
                    featuresLiveData.observe(viewLifecycleOwner) { features ->
                        if (features.isNotEmpty()) {
                            featureCardsAdapter.setAdapter(features)
                            initRecyclerView()
                        }
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.menuMainSignOut -> {
                userViewModel.logout(auth)
                findNavController().navigate(R.id.action_homeFragment_to_MainFragment)
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}