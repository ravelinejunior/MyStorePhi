package br.com.raveline.mystorephi.presentation.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.raveline.mystorephi.data.model.BestSellModel
import br.com.raveline.mystorephi.data.model.CategoryModel
import br.com.raveline.mystorephi.data.model.FeaturesModel
import br.com.raveline.mystorephi.domain.repositoryImpl.AdapterRepositoryImpl
import br.com.raveline.mystorephi.presentation.listener.UiState
import br.com.raveline.mystorephi.utils.*
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

    private val featuresStateFlow = MutableStateFlow(listOf<FeaturesModel>())
    val featuresFlow: StateFlow<List<FeaturesModel>> get() = featuresStateFlow

    private val bestSellStateFlow = MutableStateFlow(listOf<BestSellModel>())
    val bestSellFlow: StateFlow<List<BestSellModel>> get() = bestSellStateFlow


    fun getCategories() = viewModelScope.launch {

        if (categoriesFlow.value.isEmpty()) {
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
                        _uiStateFlow.value = UiState.Success

                    } else {
                        _uiStateFlow.value = UiState.Error
                    }
                }.addOnFailureListener {
                    _uiStateFlow.value = UiState.Error
                }
            } else {
                _uiStateFlow.value = UiState.NoConnection
            }
        } else {
            _uiStateFlow.value = UiState.Error
        }

    }

    fun getFeatures() = viewModelScope.launch {

        if (featuresFlow.value.isEmpty()) {
            val featureList = arrayListOf<FeaturesModel>()

            _uiStateFlow.value = UiState.Loading

            if (SystemFunctions.isNetworkAvailable(context)) {
                repository.getFeaturesRepository().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val result = task.result
                        for (document in result) {
                            val feature = FeaturesModel(
                                name = document.get(featuresFieldName).toString(),
                                description = document.get(featuresFieldDescription).toString(),
                                imageUrl = document.get(featuresFieldImageUrl).toString(),
                                price = document.get(featuresFieldPrice).toString().toDouble(),
                                rating = document.get(featuresFieldRating).toString().toInt(),
                            )
                            featureList.add(feature)
                        }

                        featuresStateFlow.value = featureList
                        _uiStateFlow.value = UiState.Success

                    } else {
                        _uiStateFlow.value = UiState.Error
                    }
                }.addOnFailureListener {
                    _uiStateFlow.value = UiState.Error
                }
            } else {
                _uiStateFlow.value = UiState.NoConnection
            }
        } else {
            _uiStateFlow.value = UiState.Error
        }

    }

    fun getBestSells() = viewModelScope.launch {

        if (bestSellFlow.value.isEmpty()) {
            val bestSells = arrayListOf<BestSellModel>()

            _uiStateFlow.value = UiState.Loading

            if (SystemFunctions.isNetworkAvailable(context)) {
                repository.getBesSellRepository().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val result = task.result
                        for (document in result) {
                            val bestSell = BestSellModel(
                                name = document.get(bestSellFieldName).toString(),
                                description = document.get(bestSellFieldDescription).toString(),
                                imageUrl = document.get(bestSellFieldImageUrl).toString(),
                                price = document.get(bestSellFieldPrice).toString().toDouble(),
                                rating = document.get(bestSellFieldRating).toString().toInt(),
                            )
                            bestSells.add(bestSell)
                        }

                        bestSellStateFlow.value = bestSells
                        _uiStateFlow.value = UiState.Success

                    } else {
                        _uiStateFlow.value = UiState.Error
                    }
                }.addOnFailureListener {
                    _uiStateFlow.value = UiState.Error
                }
            } else {
                _uiStateFlow.value = UiState.NoConnection
            }
        } else {
            _uiStateFlow.value = UiState.Error
        }

    }
}