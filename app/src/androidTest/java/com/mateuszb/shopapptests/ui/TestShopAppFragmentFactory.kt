package com.mateuszb.shopapptests.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.bumptech.glide.RequestManager
import com.mateuszb.shopapptests.repositories.FakeShoppingRepositoryAndroidTest
import com.mateuszb.shopapptests.ui.adapters.ImageAdapter
import com.mateuszb.shopapptests.ui.adapters.ShoppingItemsAdapter
import com.mateuszb.shopapptests.ui.viewmodels.ShoppingViewModel
import javax.inject.Inject

class TestShopAppFragmentFactory @Inject constructor(
    private val imageAdapter: ImageAdapter,
    private val glide: RequestManager,
    private val shoppingItemsAdapter: ShoppingItemsAdapter
) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className){
            ImagePickFragment::class.java.name -> ImagePickFragment(imageAdapter)
            AddShoppingItemFragment::class.java.name -> AddShoppingItemFragment(glide)
            ShoppingFragment::class.java.name -> ShoppingFragment(
                shoppingItemsAdapter,
                ShoppingViewModel(FakeShoppingRepositoryAndroidTest())
            )
            else -> super.instantiate(classLoader, className)
        }
    }
}