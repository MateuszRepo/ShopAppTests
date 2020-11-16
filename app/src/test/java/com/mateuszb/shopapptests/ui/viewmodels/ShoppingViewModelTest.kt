package com.mateuszb.shopapptests.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.mateuszb.shopapptests.MainCoroutineRule
import com.mateuszb.shopapptests.getOrAwaitValueTest
import com.mateuszb.shopapptests.others.Constants.MAX_NAME_ITEM_LENGTH
import com.mateuszb.shopapptests.others.Constants.MAX_PRICE_ITEM_LENGTH
import com.mateuszb.shopapptests.others.Resource
import com.mateuszb.shopapptests.repositories.FakeShoppingRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ShoppingViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ShoppingViewModel

    @Before
    fun setup(){
        viewModel = ShoppingViewModel(FakeShoppingRepository())
    }

    @Test
    fun `insert shopping item with empty field, returns error`() {
        viewModel.insertShoppingItem("name", "", "5.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()).isInstanceOf(Resource.Error::class.java)
    }

    @Test
    fun `insert shopping item with too long name, returns error`() {
        val stringName = buildString {
            for(i in 1..MAX_NAME_ITEM_LENGTH + 1) {
                append("A")
            }
        }

        viewModel.insertShoppingItem(stringName, "7", "5.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()).isInstanceOf(Resource.Error::class.java)
    }

    @Test
    fun `insert shopping item with too long price, returns error`() {
        val stringPrice = buildString {
            for(i in 1..MAX_PRICE_ITEM_LENGTH + 1)
                append("A")
        }

        viewModel.insertShoppingItem("name", "7", stringPrice)

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()).isInstanceOf(Resource.Error::class.java)
    }

    @Test
    fun `insert shopping item with too high amount, returns error`() {

        viewModel.insertShoppingItem("name", "100000000000000000000", "5.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()).isInstanceOf(Resource.Error::class.java)
    }

    @Test
    fun `insert shopping item with valid input, returns success`() {

        viewModel.insertShoppingItem("name", "7", "5.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()).isInstanceOf(Resource.Success::class.java)
    }

}