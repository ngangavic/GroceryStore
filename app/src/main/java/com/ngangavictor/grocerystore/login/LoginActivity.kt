package com.ngangavictor.grocerystore.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.ngangavictor.grocerystore.MainActivity
import com.ngangavictor.grocerystore.R
import com.ngangavictor.grocerystore.reset.ResetPasswordActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var textViewRegister: TextView
    private lateinit var textViewResetPassword: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editTextEmail=findViewById(R.id.editTextEmail)
        editTextPassword=findViewById(R.id.editTextPassword)
        buttonLogin=findViewById(R.id.buttonLogin)
        textViewRegister=findViewById(R.id.textViewRegister)
        textViewResetPassword=findViewById(R.id.textViewResetPassword)

        textViewRegister.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }

        textViewResetPassword.setOnClickListener {
            startActivity(Intent(this,ResetPasswordActivity::class.java))
        }
    }
}