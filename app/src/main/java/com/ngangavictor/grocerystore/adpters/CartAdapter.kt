package com.ngangavictor.grocerystore.adpters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.ngangavictor.grocerystore.R
import com.ngangavictor.grocerystore.categories.CategoriesActivity
import com.ngangavictor.grocerystore.categories.ui.category.CategoryFragment
import com.ngangavictor.grocerystore.holders.CartHolder
import com.ngangavictor.grocerystore.holders.CategoryHolder
import com.ngangavictor.grocerystore.models.Cart
import com.ngangavictor.grocerystore.models.CategoryModel
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CartAdapter(
    private val context: Context,
    private val cart: ArrayList<Cart>
) :
    RecyclerView.Adapter<CartHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartHolder {
        val viewHolder: CartHolder?
        val layoutView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_row_cart, parent, false)
        viewHolder = CartHolder(layoutView)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return cart.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CartHolder, position: Int) {

        holder.textViewProductDescription.text = cart[position].prodDesc
        holder.textViewProductName.text = cart[position].prodName
        holder.textViewItemPrice.text = "KES." + cart[position].prodPrice
        Picasso.get().load(cart[position].prodImage).placeholder(R.drawable.loading)
            .networkPolicy(NetworkPolicy.OFFLINE).into(holder.imageViewProduct, object : Callback {
                override fun onSuccess() {

                }

                override fun onError(e: Exception?) {
                    Log.e("PICASSO:", e?.message.toString())
                    Picasso.get().load(cart[position].prodImage)
                        .placeholder(R.drawable.loading)
                        .into(holder.imageViewProduct)
                }

            })
        holder.textViewTotalItemPrice.text=(cart[position].prodPrice.toInt()*cart[position].prodQuantity).toString()
        holder.imageViewRemove.setOnClickListener {
            CategoriesActivity.cartViewModel.deleteCartItem(cart[position].key)
        }
        holder.textViewAdd.setOnClickListener {
            val ac = context as Activity

            GlobalScope.launch {
                    //update qty
                    CategoriesActivity.cartViewModel.updateCartItem(
                        cart[position].key,
                        CategoriesActivity.cartViewModel.getCartItemQuantity(cart[position].key) + 1
                    )
                    Snackbar.make(
                        ac.findViewById(android.R.id.content),
                        "Quantity updated",
                        Snackbar.LENGTH_SHORT
                    ).show()
            }
        }
        holder.textViewSubtract.setOnClickListener {
            val ac = context as Activity

            GlobalScope.launch {

                CategoriesActivity.cartViewModel.subCartItem(cart[position].key)

                Snackbar.make(
                    ac.findViewById(android.R.id.content),
                    "Quantity updated",
                    Snackbar.LENGTH_SHORT
                ).show()

            }
        }
        holder.textViewQty.text=cart[position].prodQuantity.toString()

    }
}