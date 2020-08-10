package com.ngangavictor.grocerystore.models

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_table")
class Cart {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "key")
    var key: String

    @NonNull
    @ColumnInfo(name = "prodName")
    var prodName: String

    @NonNull
    @ColumnInfo(name = "prodDesc")
    var prodDesc: String

    @NonNull
    @ColumnInfo(name = "prodPrice")
    var prodPrice: String

    @NonNull
    @ColumnInfo(name = "prodQuantity")
    var prodQuantity: Int

    @NonNull
    @ColumnInfo(name = "prodImage")
    var prodImage: String

    constructor(
        key: String,
        prodName: String,
        prodDesc: String,
        prodPrice: String,
        prodQuantity: Int,
        prodImage: String
    ) {
        this.key = key
        this.prodName = prodName
        this.prodDesc = prodDesc
        this.prodPrice = prodPrice
        this.prodQuantity = prodQuantity
        this.prodImage = prodImage
    }
}