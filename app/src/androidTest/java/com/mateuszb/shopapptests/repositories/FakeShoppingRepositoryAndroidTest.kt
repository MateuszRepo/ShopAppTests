package com.mateuszb.shopapptests.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mateuszb.shopapptests.data.local.ShoppingItem
import com.mateuszb.shopapptests.data.remote.models.ImageResponse
import com.mateuszb.shopapptests.others.Resource

class FakeShoppingRepositoryAndroidTest : ShoppingRepository {
    private val shoppingItems = mutableListOf<ShoppingItem>()

    private val observableShoppingItems = MutableLiveData<List<ShoppingItem>>(shoppingItems)

    private val observableTotalPrice = MutableLiveData<Float>()

    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean){
        shouldReturnNetworkError = value
    }

    private fun refreshLiveData(){
        observableShoppingItems.postValue(shoppingItems)
        observableTotalPrice.postValue(getShoppingItemsTotalPrice())
    }

    private fun getShoppingItemsTotalPrice(): Float{
        return shoppingItems.sumByDouble {
            it.price.toDouble()
        }.toFloat()
    }

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItems.add(shoppingItem)
        refreshLiveData()
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItems.remove(shoppingItem)
        refreshLiveData()
    }

    override fun getAllShoppingItems(): LiveData<List<ShoppingItem>> {
        return observableShoppingItems
    }

    override fun getTotalPrice(): LiveData<Float> {
       return observableTotalPrice
    }

    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse> {
        return if(shouldReturnNetworkError){
            Resource.Error("Error", null)
        } else {
            Resource.Success(ImageResponse(listOf(), 0, 0))
        }
    }
}