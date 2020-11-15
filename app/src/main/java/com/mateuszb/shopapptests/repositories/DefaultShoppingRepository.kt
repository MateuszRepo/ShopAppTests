package com.mateuszb.shopapptests.repositories

import androidx.lifecycle.LiveData
import com.mateuszb.shopapptests.data.local.ShoppingDAO
import com.mateuszb.shopapptests.data.local.ShoppingItem
import com.mateuszb.shopapptests.data.remote.PixabayAPI
import com.mateuszb.shopapptests.data.remote.models.ImageResponse
import com.mateuszb.shopapptests.others.Resource
import javax.inject.Inject

class DefaultShoppingRepository @Inject constructor(
    private val shoppingDAO: ShoppingDAO,
    private val pixabayAPI: PixabayAPI
) : ShoppingRepository {
    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDAO.insertShoppingItem(shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDAO.deleteShoppingItem(shoppingItem)
    }

    override fun getAllShoppingItems(): LiveData<List<ShoppingItem>> {
        return shoppingDAO.getAllShoppingItems()
    }

    override fun getTotalPrice(): LiveData<Float> {
        return shoppingDAO.getTotalPrice()
    }

    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse> {
        return try {
            val response = pixabayAPI.searchForImage(imageQuery)
            if(response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.Success(it)
                } ?: Resource.Error(response.errorBody().toString())
            } else {
                Resource.Error(response.errorBody().toString())
            }
        } catch (e: Exception){
            Resource.Error("Could not reach the server. Check your internet connection.")
        }
    }
}