package com.ngangavictor.grocerystore

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
import com.ngangavictor.grocerystore.categories.CategoriesActivity
import com.ngangavictor.grocerystore.login.LoginActivity
import com.ngangavictor.grocerystore.reset.ResetPasswordActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonRegister: Button
    private lateinit var textViewLogin: TextView
    private lateinit var textViewResetPassword: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonRegister = findViewById(R.id.buttonRegister)
        textViewLogin = findViewById(R.id.textViewLogin)
        textViewResetPassword = findViewById(R.id.textViewResetPassword)

        auth = Firebase.auth
        database = Firebase.database

        textViewLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        textViewResetPassword.setOnClickListener {
            startActivity(Intent(this, ResetPasswordActivity::class.java))
        }

        buttonRegister.setOnClickListener {
            register()
        }
    }

    private fun verifyEmail(email: String): Boolean {
        return email.contains(".") && email.contains("@")
    }

    private fun verifyPassword(password: String): Boolean {
        return password.length < 6
    }

    private fun register() {
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()

        if (TextUtils.isEmpty(email)) {
            editTextEmail.requestFocus()
            editTextEmail.error = "Cannot be empty"
        } else if (TextUtils.isEmpty(password)) {
            editTextPassword.requestFocus()
            editTextPassword.error = "Cannot be empty"
        } else if (!verifyEmail(email)) {
            editTextEmail.requestFocus()
            editTextEmail.error = "Invalid email"
        } else if (verifyPassword(password)) {
            editTextPassword.requestFocus()
            editTextPassword.error = "Too short"
        } else {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        auth.currentUser!!.sendEmailVerification()
                        database.getReference("green-orchard").child("users").child(auth.currentUser!!.uid)
                            .setValue(RegUser(email, SimpleDateFormat("dd/M/yyyy").format(Date())))
                            .addOnSuccessListener {
                                Snackbar.make(
                                    findViewById(android.R.id.content),
                                    "Registration successful",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                                startActivity(Intent(this, CategoriesActivity::class.java))
                            }
                            .addOnFailureListener {
                                auth.currentUser!!.delete()
                                Snackbar.make(
                                    findViewById(android.R.id.content),
                                    "Registration failed",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
                    } else {
                        Snackbar.make(
                            findViewById(android.R.id.content),
                            "Registration failed",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }

                }
        }

    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser!=null) {
            if (auth.currentUser!!.isEmailVerified) {
                startActivity(Intent(this, CategoriesActivity::class.java))
            } else {
                auth.currentUser!!.sendEmailVerification()
                auth.signOut()
            }
        }
    }
}

class RegUser(var email: String, var date: String)