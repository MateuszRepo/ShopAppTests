package com.mateuszb.shopapptests.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.mateuszb.shopapptests.R
import com.mateuszb.shopapptests.data.local.ShoppingItem
import com.mateuszb.shopapptests.getOrAwaitValue
import com.mateuszb.shopapptests.launchFragmentInHiltContainer
import com.mateuszb.shopapptests.repositories.FakeShoppingRepositoryAndroidTest
import com.mateuszb.shopapptests.ui.viewmodels.ShoppingViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class AddShoppingItemFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var fragmentFactory: ShopAppFragmentFactory

    @Before
    fun setup(){
        hiltRule.inject()
    }

    @Test
    fun clickOnInsertIntoDB_shoppingItemInsertedIntoDB() {
        val testViewModel = ShoppingViewModel(FakeShoppingRepositoryAndroidTest())
        launchFragmentInHiltContainer<AddShoppingItemFragment>(fragmentFactory = fragmentFactory) {
            viewModel = testViewModel
        }

        onView(withId(R.id.etShoppingItemName)).perform(replaceText("Item name"))
        onView(withId(R.id.etShoppingItemAmount)).perform(replaceText("7"))
        onView(withId(R.id.etShoppingItemPrice)).perform(replaceText("7.0"))
        onView(withId(R.id.btnAddShoppingItem)).perform(click())

        assertThat(testViewModel.shoppingItems.getOrAwaitValue())
            .contains(ShoppingItem(null, "Item name", 7, 7.7f, "" ))
    }

    @Test
    fun clickImageView_navigateToImagePickFragment() {
        val naVController = mock(NavController::class.java)

        launchFragmentInHiltContainer<AddShoppingItemFragment> {
            Navigation.setViewNavController(requireView(), naVController)
        }

        onView(withId(R.id.ivShoppingImage)).perform(click())

        verify(naVController).navigate(AddShoppingItemFragmentDirections.actionAddShoppingItemFragmentToImagePickFragment())

    }

    @Test
    fun pressBackButton_popBackStack(){
        val navController = mock(NavController::class.java)

        launchFragmentInHiltContainer<AddShoppingItemFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }

        pressBack()

        verify(navController).popBackStack()
    }



}