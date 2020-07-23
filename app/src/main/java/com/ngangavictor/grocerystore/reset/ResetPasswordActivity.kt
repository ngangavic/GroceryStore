package com.ngangavictor.grocerystore.reset

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.ngangavictor.grocerystore.MainActivity
import com.ngangavictor.grocerystore.R
import com.ngangavictor.grocerystore.login.LoginActivity

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var editTextEmail:EditText
    private lateinit var buttonReset:Button
    private lateinit var textViewLogin:TextView
    private lateinit var textViewRegister:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        textViewRegister=findViewById(R.id.textViewRegister)
        textViewLogin=findViewById(R.id.textViewLogin)
        buttonReset=findViewById(R.id.buttonReset)
        editTextEmail=findViewById(R.id.editTextEmail)

        textViewLogin.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }

        textViewRegister.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
}