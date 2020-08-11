package com.ngangavictor.grocerystore.holders

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ngangavictor.grocerystore.R

class CartHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    val imageViewProduct: ImageView = itemView.findViewById(R.id.imageViewProduct)
    val imageViewRemove: ImageView = itemView.findViewById(R.id.imageViewRemove)
    val textViewProductName: TextView = itemView.findViewById(R.id.textViewProductName)
    val textViewProductDescription: TextView =
        itemView.findViewById(R.id.textViewProductDescription)
    val textViewTotalItemPrice: TextView = itemView.findViewById(R.id.textViewTotalItemPrice)
    val textViewItemPrice: TextView = itemView.findViewById(R.id.textViewItemPrice)
    val textViewSubtract: TextView = itemView.findViewById(R.id.textViewSubtract)
    val textViewQty: TextView = itemView.findViewById(R.id.textViewQty)
    val textViewAdd: TextView = itemView.findViewById(R.id.textViewAdd)
}