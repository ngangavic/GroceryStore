package com.ngangavictor.grocerystore

import android.app.AlertDialog
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ngangavictor.grocerystore.categories.CategoriesActivity
import com.ngangavictor.grocerystore.categories.account.AccountActivity
import com.ngangavictor.grocerystore.login.LoginActivity
import com.ngangavictor.grocerystore.reset.ResetPasswordActivity
import com.ngangavictor.grocerystore.utils.LocalStoragePrefs
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

    private lateinit var alert: AlertDialog

    private lateinit var localStoragePrefs: LocalStoragePrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.RegisterTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonRegister = findViewById(R.id.buttonRegister)
        textViewLogin = findViewById(R.id.textViewLogin)
        textViewResetPassword = findViewById(R.id.textViewResetPassword)

        auth = Firebase.auth
        database = Firebase.database

        localStoragePrefs = LocalStoragePrefs(this)

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
                        database.getReference("green-orchard").child("users")
                            .child(auth.currentUser!!.uid)
                            .setValue(RegUser(email, SimpleDateFormat("dd/M/yyyy").format(Date())))
                            .addOnSuccessListener {
                                Snackbar.make(
                                    findViewById(android.R.id.content),
                                    "Registration successful",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                                startActivity(
                                    Intent(
                                        this@MainActivity,
                                        AccountActivity::class.java
                                    )
                                )
                                finish()
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

    private fun completeProfile() {
        database.getReference("green-orchard").child("users")
            .child(auth.currentUser!!.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    alert.dismiss()
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Error:" + error.message,
                        Snackbar.LENGTH_LONG
                    ).show()
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child("name").exists() && snapshot.child("phone").exists()) {
                        localStoragePrefs.saveAccDetailsPref("name",snapshot.child("name").value.toString())
                        localStoragePrefs.saveAccDetailsPref("phone",snapshot.child("phone").value.toString())
                        startActivity(Intent(this@MainActivity, CategoriesActivity::class.java))
                        finish()
                    } else {
                        startActivity(Intent(this@MainActivity, AccountActivity::class.java))
                        finish()
                    }

                }

            })
    }

    override fun onStart() {
        super.onStart()
        loadingAlert()
        if (auth.currentUser != null) {
            if (auth.currentUser!!.isEmailVerified) {
                completeProfile()
            } else {
                alert.dismiss()
                auth.currentUser!!.sendEmailVerification()
                auth.signOut()
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Please verify your email",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun loadingAlert() {
        val alertDialog = AlertDialog.Builder(this@MainActivity)
        alertDialog.setCancelable(false)
        alertDialog.setMessage("Loading ...")
        alert = alertDialog.create()
        alert.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        alert.dismiss()
    }
}

class RegUser(var email: String, var date: String)