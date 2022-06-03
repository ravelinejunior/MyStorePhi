package br.com.raveline.mystorephi.domain.di

import android.content.Context
import android.content.SharedPreferences
import br.com.raveline.mystorephi.utils.USER_SHARED_PREFERENCE_KEY
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(USER_SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE)
    }
}