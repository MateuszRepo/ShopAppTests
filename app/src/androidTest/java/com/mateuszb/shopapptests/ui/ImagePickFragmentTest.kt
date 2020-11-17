package com.mateuszb.shopapptests.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.mateuszb.shopapptests.R
import com.mateuszb.shopapptests.getOrAwaitValue
import com.mateuszb.shopapptests.launchFragmentInHiltContainer
import com.mateuszb.shopapptests.repositories.FakeShoppingRepositoryAndroidTest
import com.mateuszb.shopapptests.ui.adapters.ImageAdapter
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
class ImagePickFragmentTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var fragmentFactory: ShopAppFragmentFactory

    @Before
    fun setup(){
        hiltRule.inject()
    }

    @Test
    fun clickImage_popBackStackAndSetImageUrl(){
        val navController = mock(NavController::class.java)
        val imageUrl = "URL"
        val testViewModel = ShoppingViewModel(FakeShoppingRepositoryAndroidTest())

        launchFragmentInHiltContainer<ImagePickFragment> (fragmentFactory = fragmentFactory){
            Navigation.setViewNavController(requireView(), navController)
            imageAdapter.images = listOf(imageUrl)
            viewModel = testViewModel
        }

        onView(withId(R.id.rvImages)).perform(RecyclerViewActions.actionOnItemAtPosition<ImageAdapter.ImageViewHolder>(
            0,
            click())
        )

        verify(navController).popBackStack()
        assertThat(testViewModel.currentImageUrl.getOrAwaitValue()).isEqualTo(imageUrl)
    }
}