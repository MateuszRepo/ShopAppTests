package com.mateuszb.shopapptests.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.mateuszb.shopapptests.getOrAwaitValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class ShoppingDAOTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: ShoppingItemsDatabase
    private lateinit var shoppingDao: ShoppingDAO

    @Before
    fun setup() {
        hiltRule.inject()
        shoppingDao = database.shoppingDao()
    }

    @Test
    fun insertShoppingItem() = runBlockingTest {
        val shoppingItem = ShoppingItem(1, "name", 1, 1f, "image" )

        shoppingDao.insertShoppingItem(shoppingItem)

        val shoppingItems = shoppingDao.getAllShoppingItems().getOrAwaitValue()

        assertThat(shoppingItems).contains(shoppingItem)
    }

    @Test
    fun deleteShoppingItem() = runBlockingTest {
        val shoppingItem = ShoppingItem(1, "name", 1, 1f, "image" )

        shoppingDao.insertShoppingItem(shoppingItem)
        shoppingDao.deleteShoppingItem(shoppingItem)

        val shoppingItems = shoppingDao.getAllShoppingItems().getOrAwaitValue()

        assertThat(shoppingItems).doesNotContain(shoppingItem)
    }

    @Test
    fun getTotalPrice()  = runBlockingTest {
        val shoppingItem1 = ShoppingItem(1, "name", 1, 1f, "image" )
        val shoppingItem2 = ShoppingItem(2, "name", 2, 2.5f, "image" )
        val shoppingItem3 = ShoppingItem(3, "name", 3, 3f, "image" )

        shoppingDao.insertShoppingItem(shoppingItem1)
        shoppingDao.insertShoppingItem(shoppingItem2)
        shoppingDao.insertShoppingItem(shoppingItem3)

        val totalPrice = shoppingDao.getTotalPrice().getOrAwaitValue()

        assertThat(totalPrice).isEqualTo(15)
    }

    @After
    fun teardown(){
        database.close()
    }
}