package com.ngangavictor.grocerystore.adpters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.ngangavictor.grocerystore.R
import com.ngangavictor.grocerystore.categories.CategoriesActivity
import com.ngangavictor.grocerystore.models.Cart
import com.ngangavictor.grocerystore.models.CategoryModel
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CategoryAdapter(val context: Context, private val productList: ArrayList<CategoryModel>) :
    BaseAdapter() {

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = inflater.inflate(R.layout.item_row_category, parent, false)
        val imageViewProduct = itemView.findViewById<ImageView>(R.id.imageViewProduct)
        val textViewProductName = itemView.findViewById<TextView>(R.id.textViewProductName)
        val textViewProductDescription =
            itemView.findViewById<TextView>(R.id.textViewProductDescription)
        val textViewProductPrice = itemView.findViewById<TextView>(R.id.textViewProductPrice)
        val buttonAddCart = itemView.findViewById<Button>(R.id.buttonAddCart)

        Picasso.get().load(productList[position].productImage).placeholder(R.drawable.loading)
            .networkPolicy(
                NetworkPolicy.OFFLINE
            ).into(imageViewProduct, object : Callback {
            override fun onSuccess() {

            }

            override fun onError(e: Exception?) {
                Log.e("PICASSO:", e?.message.toString())
                Picasso.get().load(productList[position].productImage)
                    .placeholder(R.drawable.loading)
                    .into(imageViewProduct)
            }

        })

        textViewProductName.text = productList[position].productName
        textViewProductDescription.text = productList[position].productDesc
        textViewProductPrice.text = "KES." + productList[position].productPrice
        buttonAddCart.setOnClickListener {
            val ac = context as Activity

            GlobalScope.launch {

                if (CategoriesActivity.cartViewModel.checkIfItemExists(productList[position].key)) {
                    //update
                    CategoriesActivity.cartViewModel.updateCartItem(
                        productList[position].key,
                        CategoriesActivity.cartViewModel.getCartItemQuantity(productList[position].key) + 1
                    )
                    Snackbar.make(
                        ac.findViewById(android.R.id.content),
                        "Cart updated",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {

                    CategoriesActivity.cartViewModel.addCartItem(
                        Cart(
                            productList[position].key,
                            productList[position].productName,
                            productList[position].productDesc,
                            productList[position].productPrice,
                            1,
                            productList[position].productImage
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

        return itemView
    }

    override fun getItem(position: Int): Any {
        return productList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return productList.size
    }
}