package br.com.raveline.mystorephi.data.repository

import br.com.raveline.mystorephi.data.model.BestSellModel
import br.com.raveline.mystorephi.data.model.CategoryModel
import br.com.raveline.mystorephi.data.model.FeaturesModel
import kotlinx.coroutines.flow.Flow

interface LocalRepository {
    suspend fun insertCategories(categories: List<CategoryModel>)
    suspend fun insertFeatures(features: List<FeaturesModel>)
    suspend fun insertBestSells(bestSells: List<BestSellModel>)

    fun getLocalCategories(): Flow<List<CategoryModel>>
    fun getLocalFeatures(): Flow<List<FeaturesModel>>
    fun getLocalBestSells(): Flow<List<BestSellModel>>

    suspend fun deleteFromCategories()
    suspend fun deleteFromFeatures()
    suspend fun deleteFromBestSells()
}