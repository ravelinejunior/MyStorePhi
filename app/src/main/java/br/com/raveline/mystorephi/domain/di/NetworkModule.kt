package br.com.raveline.mystorephi.domain.di

import br.com.raveline.mystorephi.presentation.listener.NetworkListeners
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideNetworkListener(): NetworkListeners = NetworkListeners()
}