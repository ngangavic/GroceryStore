package com.ngangavictor.grocerystore.holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ngangavictor.grocerystore.R

class CategoryHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    val imageViewProduct: ImageView = itemView.findViewById(R.id.imageViewProduct)
    val textViewProductName: TextView = itemView.findViewById(R.id.textViewProductName)
    val textViewProductDescription: TextView =
        itemView.findViewById(R.id.textViewProductDescription)
    val textViewProductPrice: TextView = itemView.findViewById(R.id.textViewProductPrice)

}