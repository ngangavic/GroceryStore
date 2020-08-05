package com.ngangavictor.grocerystore.adpters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ngangavictor.grocerystore.R
import com.ngangavictor.grocerystore.holders.CategoryHolder
import com.ngangavictor.grocerystore.models.CategoryModel
import com.ngangavictor.grocerystore.utils.CircleImageView
import com.squareup.picasso.Picasso

class CategoryAdapter(private val products: ArrayList<CategoryModel>) :
    RecyclerView.Adapter<CategoryHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val viewHolder: CategoryHolder?
        val layoutView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_row_category, parent, false)
        viewHolder = CategoryHolder(layoutView)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return products.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        holder.textViewProductDescription.text=products[position].productDesc
        holder.textViewProductName.text=products[position].productName
        holder.textViewProductPrice.text="KES."+products[position].productPrice
        Picasso.get().load(products[position].productImage)
            .placeholder(R.drawable.loading)
            .into(holder.imageViewProduct)
    }
}
