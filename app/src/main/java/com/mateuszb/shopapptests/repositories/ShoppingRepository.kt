package com.mateuszb.shopapptests.repositories

import androidx.lifecycle.LiveData
import com.mateuszb.shopapptests.data.local.ShoppingItem
import com.mateuszb.shopapptests.data.remote.models.ImageResponse
import com.mateuszb.shopapptests.others.Resource

interface ShoppingRepository {

    suspend fun  insertShoppingItem(shoppingItem: ShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    fun getAllShoppingItems(): LiveData<List<ShoppingItem>>

    fun getTotalPrice(): LiveData<Float>

    suspend fun searchForImage(imageQuery: String): Resource<ImageResponse>
}