package com.ngangavictor.grocerystore.categories.product

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.ngangavictor.grocerystore.R
import com.ngangavictor.grocerystore.categories.CategoriesActivity
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.util.*

class ProductActivity : AppCompatActivity() {

    private lateinit var imageViewProductPhoto: ImageView

    private lateinit var productName: EditText
    private lateinit var productDescription: EditText
    private lateinit var price: EditText
    private lateinit var quantity: EditText

    private lateinit var spinnerCategory: Spinner

    private lateinit var buttonAdd: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storageRef: FirebaseStorage

    private lateinit var alert: AlertDialog

    private lateinit var imagePath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        imageViewProductPhoto = findViewById(R.id.imageViewProductPhoto)
        productName = findViewById(R.id.productName)
        productDescription = findViewById(R.id.productDescription)
        price = findViewById(R.id.price)
        quantity = findViewById(R.id.quantity)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        buttonAdd = findViewById(R.id.buttonAdd)

        ArrayAdapter.createFromResource(
            this@ProductActivity,
            R.array.product_categories,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCategory.adapter = adapter
        }

        auth = Firebase.auth
        database = Firebase.database
        storageRef = Firebase.storage

        imagePath = "empty"

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Add product"

        clickListeners()

    }

    private fun clickListeners() {
        imageViewProductPhoto.setOnClickListener {
            startActivityForResult(
                Intent(Intent.ACTION_PICK).setType(
                    "image/*"
                ), 300
            )
        }

        buttonAdd.setOnClickListener { saveProduct() }
    }

    private fun saveProduct() {
        val prodName = productName.text.toString()
        val prodQuantity = quantity.text.toString()
        val prodDesc = productDescription.text.toString()
        val prodPrice = price.text.toString()
        val prodCategory = spinnerCategory.selectedItem.toString()

        if (TextUtils.isEmpty(prodName)) {
            productName.requestFocus()
            productName.error = "Cannot be empty"
        } else if (TextUtils.isEmpty(prodQuantity)) {
            quantity.requestFocus()
            quantity.error = "Cannot be empty"
        } else if (TextUtils.isEmpty(prodDesc)) {
            productDescription.requestFocus()
            productDescription.error = "Cannot be empty"
        } else if (TextUtils.isEmpty(prodPrice)) {
            price.requestFocus()
            price.error = "Cannot be empty"
        } else if (prodCategory == "Select Category") {
            spinnerCategory.performClick()
            Snackbar.make(
                findViewById(android.R.id.content),
                "Select category",
                Snackbar.LENGTH_LONG
            )
                .show()
        } else {

            if (imagePath == "empty") {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Select product image",
                    Snackbar.LENGTH_LONG
                ).show()
            } else {
                loadingAlert()
                Toast.makeText(this@ProductActivity, "Uploading", Toast.LENGTH_LONG).show()
                val byteArrayOutputStream = ByteArrayOutputStream()
                getBitmap().compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)

                val data = byteArrayOutputStream.toByteArray()
                val storage = storageRef.reference.child("green-orchard")
                    .child("products/" + UUID.randomUUID().toString())
                val uploadTask = storage.putBytes(data)
                uploadTask.continueWithTask { p0 ->
                    if (!p0.isSuccessful) {
                        p0.exception?.let {
                            throw it
                        }
                    }
                    storage.downloadUrl
                }.addOnCompleteListener(object : OnCompleteListener<Uri> {
                    override fun onComplete(p0: Task<Uri>) {
                        if (p0.isSuccessful()) {
                            val downloadUri = p0.getResult()

                            database.getReference("green-orchard").child("products")
                                .push().setValue(
                                    Product(
                                        prodName,
                                        prodQuantity,
                                        prodDesc,
                                        prodPrice,
                                        downloadUri.toString(),
                                        prodCategory
                                    )
                                )
                                .addOnSuccessListener {
                                    reset()
                                    alert.cancel()
                                    Snackbar.make(
                                        findViewById(android.R.id.content),
                                        "Product added successfully",
                                        Snackbar.LENGTH_LONG
                                    ).show()
                                }
                                .addOnFailureListener {
                                    alert.cancel()
                                    Snackbar.make(
                                        findViewById(android.R.id.content),
                                        "Product add failed",
                                        Snackbar.LENGTH_LONG
                                    ).show()
                                }

                        } else {
                            alert.cancel()
                            Snackbar.make(
                                findViewById(android.R.id.content),
                                "Product add failed",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                })


            }

        }

    }

    private fun reset() {
        productName.text.clear()
        quantity.text.clear()
        productDescription.text.clear()
        price.text.clear()
        imageViewProductPhoto.setImageResource(R.drawable.ic_add_photo)
        spinnerCategory.setSelection(0)
    }

    private fun getBitmap(): Bitmap {
        return (imageViewProductPhoto.drawable as BitmapDrawable).bitmap
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 300 && resultCode == Activity.RESULT_OK) {
            imagePath = data!!.data.toString()
            Picasso.get().load(data!!.data.toString())
                .into(imageViewProductPhoto)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                startActivity(
                    Intent(
                        this@ProductActivity,
                        CategoriesActivity::class.java
                    )
                )
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadingAlert() {
        val alertDialog = AlertDialog.Builder(this@ProductActivity)
        alertDialog.setCancelable(false)
        alertDialog.setMessage("Loading ...")
        alert = alertDialog.create()
        alert.show()
    }

}

class Product(
    var name: String,
    var quantity: String,
    var desc: String,
    var price: String,
    var image: String,
    var category: String
)