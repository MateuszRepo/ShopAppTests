package com.mateuszb.shopapptests.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.mateuszb.shopapptests.R
import com.mateuszb.shopapptests.ui.adapters.ShoppingItemsAdapter
import com.mateuszb.shopapptests.ui.viewmodels.ShoppingViewModel
import kotlinx.android.synthetic.main.fragment_shopping.*
import javax.inject.Inject

class ShoppingFragment @Inject constructor(
    val shoppingItemsAdapter: ShoppingItemsAdapter,
    var viewModel: ShoppingViewModel? = null
) : Fragment(R.layout.fragment_shopping) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = viewModel ?: ViewModelProvider(requireActivity()).get(ShoppingViewModel::class.java)
        subscribeToObservers()
        setupRecyclerView()

        fabAddShoppingItem.setOnClickListener {
            findNavController().navigate(ShoppingFragmentDirections.actionShoppingFragmentToAddShoppingItemFragment())
        }
    }

    private val touchHelperCallback = object : ItemTouchHelper.SimpleCallback(
        0,
        LEFT or RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ) = true

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.layoutPosition
            val shoppingItem = shoppingItemsAdapter.shoppingItems[position]
            viewModel?.deleteShoppingItem(shoppingItem)
            Snackbar.make(
                requireView(),
                "Item has been deleted",
                Snackbar.LENGTH_LONG
            ).apply {
                setAction("Undo") {
                    viewModel?.insertShoppingItemIntoDb(shoppingItem)
                }
                show()
            }
        }
    }

    private fun setupRecyclerView() {
        rvShoppingItems.apply {
            adapter = shoppingItemsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            ItemTouchHelper(touchHelperCallback).attachToRecyclerView(this)
        }
    }

    private fun subscribeToObservers() {
        viewModel?.shoppingItems?.observe(viewLifecycleOwner, Observer {
            shoppingItemsAdapter.shoppingItems = it
        })

        viewModel?.totalPrice?.observe(viewLifecycleOwner, Observer {
            val price = it ?: 0f
            val priceText = "Total price: $$price"
            tvShoppingItemPrice.text = priceText
        })
    }
}