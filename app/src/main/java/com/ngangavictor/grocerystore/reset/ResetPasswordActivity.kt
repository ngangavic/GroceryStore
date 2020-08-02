package com.ngangavictor.grocerystore.reset

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ngangavictor.grocerystore.MainActivity
import com.ngangavictor.grocerystore.R
import com.ngangavictor.grocerystore.login.LoginActivity

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var buttonReset: Button
    private lateinit var textViewLogin: TextView
    private lateinit var textViewRegister: TextView

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        textViewRegister = findViewById(R.id.textViewRegister)
        textViewLogin = findViewById(R.id.textViewLogin)
        buttonReset = findViewById(R.id.buttonReset)
        editTextEmail = findViewById(R.id.editTextEmail)

        auth = Firebase.auth
        database = Firebase.database

        textViewLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        textViewRegister.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        buttonReset.setOnClickListener { resetPassword() }
    }

    private fun resetPassword(){
        val email=editTextEmail.text.toString()
        if (TextUtils.isEmpty(email)) {
            editTextEmail.requestFocus()
            editTextEmail.error="Cannot be empty"
        }else {
            auth.sendPasswordResetEmail(email)
                .addOnFailureListener {
                    Snackbar.make(findViewById(android.R.id.content),"Error: "+it.message,Snackbar.LENGTH_LONG).show()
                }
                .addOnSuccessListener {
                    Snackbar.make(findViewById(android.R.id.content),"Password reset link sent to your email.",Snackbar.LENGTH_LONG).show()
                }
        }

    }

}