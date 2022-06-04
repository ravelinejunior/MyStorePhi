package br.com.raveline.mystorephi.presentation.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import br.com.raveline.mystorephi.R
import br.com.raveline.mystorephi.databinding.FragmentHomeBinding
import br.com.raveline.mystorephi.presentation.listener.UiState
import br.com.raveline.mystorephi.presentation.viewmodel.HomeViewModel
import br.com.raveline.mystorephi.presentation.viewmodel.viewmodel_factory.HomeViewModelFactory
import br.com.raveline.mystorephi.utils.CustomDialogLoading
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    @Inject
    lateinit var homeViewModelFactory: HomeViewModelFactory
    private val homeViewModel: HomeViewModel by viewModels { homeViewModelFactory }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
    }

    private fun initObservers() {
        lifecycleScope.launchWhenStarted {
            homeViewModel.uiStateFlow.collect { state ->
                when (state) {
                    UiState.Initial -> {
                        Toast.makeText(context, "Initial", Toast.LENGTH_SHORT).show()
                        homeViewModel.getCategories()
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
            homeViewModel.categoriesFlow.collectLatest { categories ->
                if (categories.isNotEmpty()) {
                    categories.forEach {
                        Log.i("TAGCategories", it.toString())
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}