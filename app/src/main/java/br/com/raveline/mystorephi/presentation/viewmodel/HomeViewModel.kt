package br.com.raveline.mystorephi.presentation.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.raveline.mystorephi.data.model.CategoryModel
import br.com.raveline.mystorephi.domain.repositoryImpl.AdapterRepositoryImpl
import br.com.raveline.mystorephi.presentation.listener.UiState
import br.com.raveline.mystorephi.utils.SystemFunctions
import br.com.raveline.mystorephi.utils.categoriesFieldImageUrl
import br.com.raveline.mystorephi.utils.categoriesFieldType
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: AdapterRepositoryImpl,
    @ApplicationContext val context: Context,
) : ViewModel() {

    private val _uiStateFlow = MutableStateFlow<UiState>(UiState.Initial)
    val uiStateFlow: StateFlow<UiState> get() = _uiStateFlow

    private val categoriesStateFlow = MutableStateFlow(listOf<CategoryModel>())
    val categoriesFlow: StateFlow<List<CategoryModel>> get() = categoriesStateFlow


    fun getCategories() = viewModelScope.launch {
        val categories = arrayListOf<CategoryModel>()

        _uiStateFlow.value = UiState.Loading

        if (SystemFunctions.isNetworkAvailable(context)) {
            repository.getCategoriesRepository().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result
                    for (document in result) {
                        val category = CategoryModel(
                            document.get(categoriesFieldType).toString(),
                            document.get(categoriesFieldImageUrl).toString(),
                        )
                        categories.add(category)
                    }

                    categoriesStateFlow.value = categories

                } else {
                    _uiStateFlow.value = UiState.Error
                }
            }.addOnFailureListener {
                _uiStateFlow.value = UiState.Error
            }
        } else {
            _uiStateFlow.value = UiState.NoConnection
        }


    }

}