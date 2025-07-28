package com.example.plantapp.di

import android.content.Context
import com.example.plantapp.data.database.PlantDatabase
import com.example.plantapp.data.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PlantDatabase {
        return PlantDatabase.getDatabase(context)
    }
    
    @Provides
    fun providePlantDao(database: PlantDatabase): PlantDao {
        return database.plantDao()
    }
    
    @Provides
    fun provideUserDao(database: PlantDatabase): UserDao {
        return database.userDao()
    }
    
    @Provides
    fun provideCartDao(database: PlantDatabase): CartDao {
        return database.cartDao()
    }
    
    @Provides
    fun provideOrderDao(database: PlantDatabase): OrderDao {
        return database.orderDao()
    }
    
    @Provides
    fun provideOrderItemDao(database: PlantDatabase): OrderItemDao {
        return database.orderItemDao()
    }
} 