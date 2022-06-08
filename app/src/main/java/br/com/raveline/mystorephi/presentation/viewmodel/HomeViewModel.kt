package br.com.raveline.mystorephi.presentation.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.raveline.mystorephi.data.model.BestSellModel
import br.com.raveline.mystorephi.data.model.CategoryModel
import br.com.raveline.mystorephi.data.model.FeaturesModel
import br.com.raveline.mystorephi.domain.repositoryImpl.AdapterRepositoryImpl
import br.com.raveline.mystorephi.domain.repositoryImpl.LocalRepositoryImpl
import br.com.raveline.mystorephi.presentation.listener.UiState
import br.com.raveline.mystorephi.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: AdapterRepositoryImpl,
    private val localRepository: LocalRepositoryImpl,
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

    private val _mutableCategoriesLiveData = MutableLiveData<List<CategoryModel>>()
    val categoriesLiveData: LiveData<List<CategoryModel>> get() = _mutableCategoriesLiveData

    private val _mutableFeaturesLiveData = MutableLiveData<List<FeaturesModel>>()
    val featuresLiveData: LiveData<List<FeaturesModel>> get() = _mutableFeaturesLiveData

    private val _mutableBestSellLiveData = MutableLiveData<List<BestSellModel>>()
    val bestSellLiveData: LiveData<List<BestSellModel>> get() = _mutableBestSellLiveData

    init {
        initRepo()
    }

    private fun initRepo() {
        viewModelScope.launch {
            localRepository.getLocalCategories().collectLatest {
                _mutableCategoriesLiveData.postValue(it)
                getCategories()
            }

        }

        viewModelScope.launch {
            localRepository.getLocalFeatures().collectLatest {
                _mutableFeaturesLiveData.postValue(it)
                getFeatures()
            }
        }

        viewModelScope.launch {
            localRepository.getLocalBestSells().collectLatest {
                _mutableBestSellLiveData.postValue(it).run {
                    getBestSells()
                }
            }
        }
    }


    fun getCategories() = viewModelScope.launch {
        delay(200)
        if (categoriesLiveData.value.isNullOrEmpty()) {
            val categories = arrayListOf<CategoryModel>()

            _uiStateFlow.value = UiState.Loading

            if (SystemFunctions.isNetworkAvailable(context)) {
                repository.getCategoriesRepository().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val result = task.result
                        for (document in result) {
                            val category = CategoryModel(
                                0,
                                document.get(categoriesFieldType).toString(),
                                document.get(categoriesFieldImageUrl).toString(),
                            )
                            categories.add(category)
                        }

                        categoriesStateFlow.value = categories
                        _mutableCategoriesLiveData.postValue(categories)
                        _uiStateFlow.value = UiState.Success

                        viewModelScope.launch {
                            localRepository.deleteFromCategories()
                            localRepository.insertCategories(categories)
                        }

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
            delay(500)
            if (categoriesLiveData.value?.isNotEmpty() == true) {
                if (featuresLiveData.value?.isNotEmpty() == true) {
                    if (bestSellLiveData.value?.isNotEmpty() == true) {
                        _uiStateFlow.value = UiState.Success
                    }
                }
            }

        }

    }

    private fun getFeatures() = viewModelScope.launch {
        delay(200)
        if (featuresLiveData.value.isNullOrEmpty()) {
            val featureList = arrayListOf<FeaturesModel>()

            _uiStateFlow.value = UiState.Loading

            if (SystemFunctions.isNetworkAvailable(context)) {
                repository.getFeaturesRepository().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val result = task.result
                        for (document in result) {
                            val feature = FeaturesModel(
                                0,
                                name = document.get(featuresFieldName).toString(),
                                description = document.get(featuresFieldDescription).toString(),
                                imageUrl = document.get(featuresFieldImageUrl).toString(),
                                price = document.get(featuresFieldPrice).toString().toDouble(),
                                rating = document.get(featuresFieldRating).toString().toInt(),
                            )
                            featureList.add(feature)
                        }

                        featuresStateFlow.value = featureList
                        _mutableFeaturesLiveData.postValue(featureList)
                        _uiStateFlow.value = UiState.Success

                        viewModelScope.launch {
                            localRepository.deleteFromFeatures()
                            localRepository.insertFeatures(featureList)
                        }

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
            delay(500)
            if (categoriesLiveData.value?.isNotEmpty() == true) {
                if (featuresLiveData.value?.isNotEmpty() == true) {
                    if (bestSellLiveData.value?.isNotEmpty() == true) {
                        _uiStateFlow.value = UiState.Success
                    }
                }
            }
        }

    }

    private fun getBestSells() = viewModelScope.launch {
        delay(200)
        if (bestSellLiveData.value.isNullOrEmpty()) {
            val bestSells = arrayListOf<BestSellModel>()

            _uiStateFlow.value = UiState.Loading

            if (SystemFunctions.isNetworkAvailable(context)) {
                repository.getBesSellRepository().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val result = task.result
                        for (document in result) {
                            val bestSell = BestSellModel(
                                0,
                                name = document.get(bestSellFieldName).toString(),
                                description = document.get(bestSellFieldDescription).toString(),
                                imageUrl = document.get(bestSellFieldImageUrl).toString(),
                                price = document.get(bestSellFieldPrice).toString().toDouble(),
                                rating = document.get(bestSellFieldRating).toString().toInt(),
                            )
                            bestSells.add(bestSell)
                        }

                        bestSellStateFlow.value = bestSells
                        _mutableBestSellLiveData.postValue(bestSells)
                        _uiStateFlow.value = UiState.Success

                        viewModelScope.launch {
                            localRepository.deleteFromBestSells()
                            localRepository.insertBestSells(bestSells)
                        }

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
            delay(500)
            if (categoriesLiveData.value?.isNotEmpty() == true) {
                if (featuresLiveData.value?.isNotEmpty() == true) {
                    if (bestSellLiveData.value?.isNotEmpty() == true) {
                        _uiStateFlow.value = UiState.Success
                    }
                }
            }
        }

    }
}