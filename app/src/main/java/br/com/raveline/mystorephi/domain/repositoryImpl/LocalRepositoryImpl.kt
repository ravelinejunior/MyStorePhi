package br.com.raveline.mystorephi.domain.repositoryImpl

import br.com.raveline.mystorephi.data.database.dao.BestSellDao
import br.com.raveline.mystorephi.data.database.dao.CategoryDao
import br.com.raveline.mystorephi.data.database.dao.FeaturesDao
import br.com.raveline.mystorephi.data.model.BestSellModel
import br.com.raveline.mystorephi.data.model.CategoryModel
import br.com.raveline.mystorephi.data.model.FeaturesModel
import br.com.raveline.mystorephi.data.repository.LocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val bestSellDao: BestSellDao,
    private val categoryDao: CategoryDao,
    private val featuresDao: FeaturesDao
) : LocalRepository {
    override suspend fun insertCategories(categories: List<CategoryModel>) {
        categoryDao.insertCategories(categories)
    }

    override suspend fun insertFeatures(features: List<FeaturesModel>) {
        featuresDao.insertFeatures(features)
    }

    override suspend fun insertBestSells(bestSells: List<BestSellModel>) {
        bestSellDao.insertBestSells(bestSells)
    }

    override fun getLocalCategories(): Flow<List<CategoryModel>> {
        return categoryDao.getCategories()
    }

    override fun getLocalFeatures(): Flow<List<FeaturesModel>> {
        return featuresDao.getFeatures()
    }

    override fun getLocalBestSells(): Flow<List<BestSellModel>> {
        return bestSellDao.getBestSells()
    }

    override suspend fun deleteFromCategories() {
        categoryDao.deleteFromCategories()
    }

    override suspend fun deleteFromFeatures() {
        featuresDao.deleteFromFeatures()
    }

    override suspend fun deleteFromBestSells() {
        bestSellDao.deleteFromBestSells()
    }
}