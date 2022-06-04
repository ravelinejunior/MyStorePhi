package br.com.raveline.mystorephi.presentation.viewmodel.viewmodel_factory


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.raveline.mystorephi.domain.repositoryImpl.AdapterRepositoryImpl
import br.com.raveline.mystorephi.presentation.viewmodel.HomeViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class HomeViewModelFactory @Inject constructor(
    private val adapterRepository: AdapterRepositoryImpl,
    @ApplicationContext val context: Context,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(adapterRepository, context) as T
        }

        throw IllegalArgumentException("Wrong class")
    }
}