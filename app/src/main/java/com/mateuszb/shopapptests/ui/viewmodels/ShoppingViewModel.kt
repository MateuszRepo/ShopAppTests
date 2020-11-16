package com.mateuszb.shopapptests.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mateuszb.shopapptests.data.local.ShoppingItem
import com.mateuszb.shopapptests.data.remote.models.ImageResponse
import com.mateuszb.shopapptests.others.Constants.MAX_NAME_ITEM_LENGTH
import com.mateuszb.shopapptests.others.Constants.MAX_PRICE_ITEM_LENGTH
import com.mateuszb.shopapptests.others.Event
import com.mateuszb.shopapptests.others.Resource
import com.mateuszb.shopapptests.repositories.ShoppingRepository
import kotlinx.coroutines.launch

class ShoppingViewModel @ViewModelInject constructor(
    private val repository: ShoppingRepository
) : ViewModel() {

    val shoppingItems = repository.getAllShoppingItems()

    val totalPrice = repository.getTotalPrice()

    private val _images = MutableLiveData<Event<Resource<ImageResponse>>>()
    val images: LiveData<Event<Resource<ImageResponse>>> get() = _images

    private val _currentImageUrl = MutableLiveData<String>()
    val currentImageUrl: LiveData<String> get() = _currentImageUrl

    private val _insertShoppingItemStatus = MutableLiveData<Event<Resource<ShoppingItem>>>()
    val insertShoppingItemStatus: LiveData<Event<Resource<ShoppingItem>>> get() = _insertShoppingItemStatus

    fun setCurrentImageUrl(url: String) {
        _currentImageUrl.postValue(url)
    }

    fun insertShoppingItemIntoDb(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.insertShoppingItem(shoppingItem)
    }

    fun deleteShoppingItem(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.deleteShoppingItem(shoppingItem)
    }

    fun insertShoppingItem(name: String, amountString: String, priceString: String){
        if(name.isEmpty() || amountString.isEmpty() || priceString.isEmpty()){
            _insertShoppingItemStatus.postValue(Event(Resource.Error("Fields must not be empty")))
            return
        }

        if(name.length > MAX_NAME_ITEM_LENGTH){
            _insertShoppingItemStatus.postValue(Event(Resource.Error("Name if the item" +
                    "must not exceed $MAX_NAME_ITEM_LENGTH characters")))
            return
        }

        if(priceString.length > MAX_PRICE_ITEM_LENGTH){
            _insertShoppingItemStatus.postValue(Event(Resource.Error("Price if the item" +
                    "must not exceed $MAX_PRICE_ITEM_LENGTH characters")))
            return
        }

        val amount = try {
            amountString.toInt()
        } catch (e: Exception) {
            _insertShoppingItemStatus.postValue(Event(Resource.Error("Please enter valid amount")))
            return
        }

        val shoppingItem = ShoppingItem(null, name, amount, priceString.toFloat(), _currentImageUrl.value ?: "")
        insertShoppingItemIntoDb(shoppingItem)
        setCurrentImageUrl("")
        _insertShoppingItemStatus.postValue(Event(Resource.Success(shoppingItem)))
    }

    fun searchForImage(imageQuery: String) {
        if(imageQuery.isEmpty()) {
            return
        }
        _images.value = Event(Resource.Loading())
        viewModelScope.launch {
            val response = repository.searchForImage(imageQuery)
            _images.value = Event(response)
        }
    }


}