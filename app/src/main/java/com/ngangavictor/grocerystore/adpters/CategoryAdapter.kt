package com.ngangavictor.grocerystore.adpters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.ngangavictor.grocerystore.R
import com.ngangavictor.grocerystore.models.CategoryModel
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

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