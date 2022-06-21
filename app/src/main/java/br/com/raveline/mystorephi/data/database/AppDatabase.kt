package br.com.raveline.mystorephi.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.raveline.mystorephi.data.database.dao.BestSellDao
import br.com.raveline.mystorephi.data.database.dao.CategoryDao
import br.com.raveline.mystorephi.data.database.dao.FeaturesDao
import br.com.raveline.mystorephi.data.database.dao.UserAddressDao
import br.com.raveline.mystorephi.data.model.AddressModel
import br.com.raveline.mystorephi.data.model.BestSellModel
import br.com.raveline.mystorephi.data.model.CategoryModel
import br.com.raveline.mystorephi.data.model.FeaturesModel

@Database(
    entities = [BestSellModel::class, CategoryModel::class, FeaturesModel::class, AddressModel::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bestSellDao(): BestSellDao
    abstract fun categoriesDao(): CategoryDao
    abstract fun featuresDao(): FeaturesDao
    abstract fun userAddressDao(): UserAddressDao
}