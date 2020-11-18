package com.mateuszb.shopapptests.ui

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.google.android.material.snackbar.Snackbar
import com.mateuszb.shopapptests.R
import com.mateuszb.shopapptests.others.Resource
import com.mateuszb.shopapptests.ui.viewmodels.ShoppingViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_add_shopping_item.*
import javax.inject.Inject

class AddShoppingItemFragment @Inject constructor(
    val glide: RequestManager
) : Fragment(R.layout.fragment_add_shopping_item) {

    lateinit var viewModel: ShoppingViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ShoppingViewModel::class.java)
        subscribeToObservers()

        btnAddShoppingItem.setOnClickListener {
            viewModel.insertShoppingItem(
                etShoppingItemName.text.toString(),
                etShoppingItemAmount.text.toString(),
                etShoppingItemPrice.text.toString()
            )
        }

        ivShoppingImage.setOnClickListener {
            findNavController().navigate(AddShoppingItemFragmentDirections.actionAddShoppingItemFragmentToImagePickFragment())
        }

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                viewModel.setCurrentImageUrl("")
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    private fun subscribeToObservers() {
        viewModel.currentImageUrl.observe(viewLifecycleOwner, Observer {
            glide.load(it).into(ivShoppingImage)
        })

        viewModel.insertShoppingItemStatus.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { result ->
                when(result){
                    is Resource.Success -> {
                        Snackbar.make(
                            requireActivity().rootLayout,
                            "Shopping item added successfully",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }

                    is Resource.Error -> {
                        Snackbar.make(
                            requireActivity().rootLayout,
                            result.message ?: "Unknown error",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    //Writing to Room database doesn't require dialog because it is operation fast
                    is Resource.Loading -> {
                        /* NO_OP */
                    }
                }
            }
        })
    }
}