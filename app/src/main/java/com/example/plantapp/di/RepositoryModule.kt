package com.example.plantapp.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    // Repositories are now injected via constructor injection
    // No need for explicit @Provides methods since they use @Inject constructor
}