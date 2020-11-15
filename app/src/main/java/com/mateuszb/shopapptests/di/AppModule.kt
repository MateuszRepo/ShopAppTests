package com.mateuszb.shopapptests.di

import android.content.Context
import androidx.room.Room
import com.mateuszb.shopapptests.data.local.ShoppingDAO
import com.mateuszb.shopapptests.data.local.ShoppingItemsDatabase
import com.mateuszb.shopapptests.data.remote.PixabayAPI
import com.mateuszb.shopapptests.others.Constants.BASE_URL
import com.mateuszb.shopapptests.others.Constants.DATABASE_NAME
import com.mateuszb.shopapptests.repositories.DefaultShoppingRepository
import com.mateuszb.shopapptests.repositories.ShoppingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideShoppingItemDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, ShoppingItemsDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideShoppingDAO(
        database: ShoppingItemsDatabase
    ) = database.shoppingDao()

    @Singleton
    @Provides
    fun provideDefaultShoppingRepository(
        dao: ShoppingDAO,
        api: PixabayAPI
    ) = DefaultShoppingRepository(dao, api) as ShoppingRepository

    @Singleton
    @Provides
    fun providePixabayAPI(): PixabayAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(PixabayAPI::class.java)
    }

}