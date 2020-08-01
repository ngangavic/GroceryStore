package com.ngangavictor.grocerystore.categories.product

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import com.ngangavictor.grocerystore.R

class ProductActivity : AppCompatActivity() {

    private lateinit var imageViewProductPhoto:ImageView

    private lateinit var productName:EditText
    private lateinit var productDescription:EditText
    private lateinit var price:EditText
    private lateinit var quantity:EditText

    private lateinit var spinnerCategory:Spinner

    private lateinit var buttonAdd:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        imageViewProductPhoto=findViewById(R.id.imageViewProductPhoto)
        productName=findViewById(R.id.productName)
        productDescription=findViewById(R.id.productDescription)
        price=findViewById(R.id.price)
        quantity=findViewById(R.id.quantity)
        spinnerCategory=findViewById(R.id.spinnerCategory)
        buttonAdd=findViewById(R.id.buttonAdd)


    }
}