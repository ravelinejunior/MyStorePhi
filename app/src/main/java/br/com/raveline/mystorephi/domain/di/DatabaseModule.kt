package br.com.raveline.mystorephi.domain.di

import android.content.Context
import androidx.room.Room
import br.com.raveline.mystorephi.data.database.AppDatabase
import br.com.raveline.mystorephi.data.database.dao.BestSellDao
import br.com.raveline.mystorephi.data.database.dao.CategoryDao
import br.com.raveline.mystorephi.data.database.dao.FeaturesDao
import br.com.raveline.mystorephi.utils.databaseName
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun providesDatabaseModule(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            databaseName
        ).enableMultiInstanceInvalidation()
            .allowMainThreadQueries()
            .fallbackToDestructiveMigrationOnDowngrade()
            .build()

    @Provides
    @Singleton
    fun providesBestSellDao(appDatabase: AppDatabase): BestSellDao =
        appDatabase.bestSellDao()

    @Provides
    @Singleton
    fun providesCategoriesDao(appDatabase: AppDatabase): CategoryDao =
        appDatabase.categoriesDao()

    @Provides
    @Singleton
    fun providesFeaturesDao(appDatabase: AppDatabase): FeaturesDao =
        appDatabase.featuresDao()
}