package com.ngangavictor.grocerystore.categories.account

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import com.ngangavictor.grocerystore.R
import com.ngangavictor.grocerystore.categories.product.ProductActivity

class AccountActivity : AppCompatActivity() {

    private lateinit var imageViewProfile:ImageView
    private lateinit var imageViewEditName:ImageView
    private lateinit var imageViewEditPhone:ImageView

    private lateinit var textViewName:TextView
    private lateinit var textViewPhone:TextView
    private lateinit var textViewAccountType:TextView

    private lateinit var switchUser:Switch

    private lateinit var buttonAdd:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        imageViewProfile=findViewById(R.id.imageViewProfile)
        imageViewEditName=findViewById(R.id.imageViewEditName)
        imageViewEditPhone=findViewById(R.id.imageViewEditPhone)

        textViewName=findViewById(R.id.textViewName)
        textViewPhone=findViewById(R.id.textViewPhone)
        textViewAccountType=findViewById(R.id.textViewAccountType)

        switchUser=findViewById(R.id.switchUser)

        buttonAdd=findViewById(R.id.buttonAdd)

        clickListeners()

    }

    private fun clickListeners(){
        buttonAdd.setOnClickListener {
            startActivity(Intent(this@AccountActivity,ProductActivity::class.java))
            finish()
        }
    }

}