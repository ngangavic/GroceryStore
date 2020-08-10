package com.ngangavictor.grocerystore.adpters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.ngangavictor.grocerystore.R
import com.ngangavictor.grocerystore.categories.CategoriesActivity
import com.ngangavictor.grocerystore.categories.ui.cart.CartViewModel
import com.ngangavictor.grocerystore.categories.ui.category.CategoryFragment
import com.ngangavictor.grocerystore.holders.CategoryHolder
import com.ngangavictor.grocerystore.models.Cart
import com.ngangavictor.grocerystore.models.CategoryModel
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CategoriesAdapter(
    private val context: Context,
    private val products: ArrayList<CategoryModel>
) :
    RecyclerView.Adapter<CategoryHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val viewHolder: CategoryHolder?
        val layoutView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_row_categories, parent, false)
        viewHolder = CategoryHolder(layoutView)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return products.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        holder.textViewProductDescription.text = products[position].productDesc
        holder.textViewProductName.text = products[position].productName
        holder.textViewProductPrice.text = "KES." + products[position].productPrice
        Picasso.get().load(products[position].productImage).placeholder(R.drawable.loading)
            .networkPolicy(NetworkPolicy.OFFLINE).into(holder.imageViewProduct, object : Callback {
                override fun onSuccess() {

                }

                override fun onError(e: Exception?) {
                    Log.e("PICASSO:", e?.message.toString())
                    Picasso.get().load(products[position].productImage)
                        .placeholder(R.drawable.loading)
                        .into(holder.imageViewProduct)
                }

            })
        holder.imageViewProduct.setOnClickListener {
            val activity = context as CategoriesActivity
            val categoryFragment = CategoryFragment.newInstance(products[position].category)
            val fragmentTransaction = activity.supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, categoryFragment)
            fragmentTransaction.commit()
        }
        holder.buttonAddCart.setOnClickListener {
            val ac = context as Activity

            GlobalScope.launch {

                if (CategoriesActivity.cartViewModel.checkIfItemExists(products[position].key)) {
                    //update
                    CategoriesActivity.cartViewModel.updateCartItem(
                        products[position].key,
                        CategoriesActivity.cartViewModel.getCartItemQuantity(products[position].key) + 1
                    )
                    Snackbar.make(
                        ac.findViewById(android.R.id.content),
                        "Cart updated",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {

                    CategoriesActivity.cartViewModel.addCartItem(
                        Cart(
                            products[position].key,
                            products[position].productName,
                            products[position].productDesc,
                            products[position].productPrice,
                            1,
                            products[position].productImage
                        )
                    )
                    Snackbar.make(
                        ac.findViewById(android.R.id.content),
                        "Added to cart",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}