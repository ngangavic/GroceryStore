package com.ngangavictor.grocerystore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.ngangavictor.grocerystore.categories.CategoriesActivity
import com.ngangavictor.grocerystore.login.LoginActivity
import com.ngangavictor.grocerystore.reset.ResetPasswordActivity

class MainActivity : AppCompatActivity() {

    private lateinit var editTextEmail:EditText
    private lateinit var editTextPassword:EditText
    private lateinit var buttonRegister:Button
    private lateinit var textViewLogin:TextView
    private lateinit var textViewResetPassword:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextEmail=findViewById(R.id.editTextEmail)
        editTextPassword=findViewById(R.id.editTextPassword)
        buttonRegister=findViewById(R.id.buttonRegister)
        textViewLogin=findViewById(R.id.textViewLogin)
        textViewResetPassword=findViewById(R.id.textViewResetPassword)

        textViewLogin.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }

        textViewResetPassword.setOnClickListener {
            startActivity(Intent(this, ResetPasswordActivity::class.java))
        }

        buttonRegister.setOnClickListener {
            startActivity(Intent(this,CategoriesActivity::class.java))
        }
    }
}