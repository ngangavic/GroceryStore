package com.ngangavictor.grocerystore.login

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
import com.ngangavictor.grocerystore.MainActivity
import com.ngangavictor.grocerystore.R
import com.ngangavictor.grocerystore.RegUser
import com.ngangavictor.grocerystore.categories.CategoriesActivity
import com.ngangavictor.grocerystore.categories.account.AccountActivity
import com.ngangavictor.grocerystore.reset.ResetPasswordActivity
import java.text.SimpleDateFormat
import java.util.*

class LoginActivity : AppCompatActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var textViewRegister: TextView
    private lateinit var textViewResetPassword: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private lateinit var alert: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        textViewRegister = findViewById(R.id.textViewRegister)
        textViewResetPassword = findViewById(R.id.textViewResetPassword)

        auth = Firebase.auth
        database = Firebase.database

        textViewRegister.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        textViewResetPassword.setOnClickListener {
            startActivity(Intent(this, ResetPasswordActivity::class.java))
        }

        buttonLogin.setOnClickListener {
            login()
        }
    }

    private fun login() {
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
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {


                        if (auth.currentUser!!.isEmailVerified) {
                            checkUserDetails(auth.currentUser!!.uid, email)
                        } else {
                            auth.currentUser!!.sendEmailVerification()
                            Snackbar.make(
                                findViewById(android.R.id.content),
                                "Verify your email",
                                Snackbar.LENGTH_SHORT
                            ).show()
                            auth.signOut()
                        }
                    } else {
                        Snackbar.make(
                            findViewById(android.R.id.content),
                            "Login failed:" + task.exception!!.message,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }

                }
        }

    }

    private fun checkUserDetails(uid: String, email: String) {
        database.reference.child("users")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChild(uid)) {
                        startActivity(Intent(this@LoginActivity, CategoriesActivity::class.java))
                    } else {
                        database.getReference("green-orchard").child("users")
                            .child(auth.currentUser!!.uid)
                            .setValue(RegUser(email, SimpleDateFormat("dd/M/yyyy").format(Date())))
                            .addOnSuccessListener {
                                startActivity(
                                    Intent(
                                        this@LoginActivity,
                                        AccountActivity::class.java
                                    )
                                )
                            }
                            .addOnFailureListener {
                                Snackbar.make(
                                    findViewById(android.R.id.content),
                                    "Login failed: "+it.message,
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
                    }
                }

            })
    }

    private fun verifyEmail(email: String): Boolean {
        return email.contains(".") && email.contains("@")
    }

    private fun verifyPassword(password: String): Boolean {
        return password.length < 6
    }

    private fun completeProfile() {
        database.getReference("green-orchard").child("users")
            .child(auth.currentUser!!.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    alert.cancel()
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Error:" + error.message,
                        Snackbar.LENGTH_LONG
                    ).show()
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child("name").exists() && snapshot.child("phone").exists()) {
                        startActivity(Intent(this@LoginActivity, CategoriesActivity::class.java))
                        finish()
                        alert.cancel()
                    } else {
                        startActivity(Intent(this@LoginActivity, AccountActivity::class.java))
                        finish()
                        alert.cancel()
                    }

                }

            })
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            loadingAlert()
            if (auth.currentUser!!.isEmailVerified) {
                completeProfile()
            } else {
                auth.currentUser!!.sendEmailVerification()
                auth.signOut()
                alert.cancel()
            }
        }
    }

    private fun loadingAlert() {
        val alertDialog = AlertDialog.Builder(this@LoginActivity)
        alertDialog.setCancelable(false)
        alertDialog.setMessage("Loading ...")
        alert = alertDialog.create()
        alert.show()
    }

}

class RegUser(var email: String, var date: String)