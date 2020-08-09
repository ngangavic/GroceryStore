package com.ngangavictor.grocerystore.categories.account

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.ngangavictor.grocerystore.R
import com.ngangavictor.grocerystore.categories.CategoriesActivity
import com.ngangavictor.grocerystore.categories.product.ProductActivity
import com.ngangavictor.grocerystore.utils.CircleImageView
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream


class AccountActivity : AppCompatActivity() {

    private lateinit var imageViewProfile: ImageView
    private lateinit var imageViewEditName: ImageView
    private lateinit var imageViewEditPhone: ImageView

    private lateinit var textViewName: TextView
    private lateinit var textViewPhone: TextView
    private lateinit var textViewAccountType: TextView

    private lateinit var switchUser: Switch

    private lateinit var buttonAdd: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storageRef: FirebaseStorage

    private lateinit var alert: AlertDialog

    private lateinit var imagePath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        imageViewProfile = findViewById(R.id.imageViewProfile)
        imageViewEditName = findViewById(R.id.imageViewEditName)
        imageViewEditPhone = findViewById(R.id.imageViewEditPhone)

        textViewName = findViewById(R.id.textViewName)
        textViewPhone = findViewById(R.id.textViewPhone)
        textViewAccountType = findViewById(R.id.textViewAccountType)

        switchUser = findViewById(R.id.switchUser)

        buttonAdd = findViewById(R.id.buttonAdd)

        auth = Firebase.auth
        database = Firebase.database
        storageRef = Firebase.storage

        imagePath = "empty"

        getDetails()

        clickListeners()

        if (!checkPermission()) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    READ_EXTERNAL_STORAGE,
                    WRITE_EXTERNAL_STORAGE
                ), 200
            )
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Profile"

    }

    private fun clickListeners() {
        buttonAdd.setOnClickListener {
            startActivity(Intent(this@AccountActivity, ProductActivity::class.java))
            finish()
        }

        imageViewProfile.setOnClickListener {
            startActivityForResult(Intent(Intent.ACTION_PICK).setType("image/*"), 300)
        }

        imageViewEditName.setOnClickListener { editDetails("Name", "Enter name") }

        imageViewEditPhone.setOnClickListener { editDetails("Phone", "Enter phone number") }
    }

    private fun editDetails(title: String, hint: String) {
        val snackBar = Snackbar.make(
            findViewById(android.R.id.content),
            "",
            Snackbar.LENGTH_INDEFINITE
        )
        val snackBarLayout: Snackbar.SnackbarLayout = snackBar.view as Snackbar.SnackbarLayout
        val view = View.inflate(this@AccountActivity, R.layout.custom_snackbar, null)
        val textViewTitle = view.findViewById<TextView>(R.id.textViewTitle)
        val editTextInput = view.findViewById<EditText>(R.id.editTextInput)
        val textViewCancel = view.findViewById<TextView>(R.id.textViewCancel)
        val textViewDone = view.findViewById<TextView>(R.id.textViewDone)

        textViewTitle.text = title
        editTextInput.hint = hint
        textViewCancel.setOnClickListener { snackBar.dismiss() }
        textViewDone.setOnClickListener {
            when (title) {
                "Name" -> {
                    if (TextUtils.isEmpty(editTextInput.text.toString())) {
                        editTextInput.error = "Cannot be empty"
                    } else {
                        textViewName.text = editTextInput.text.toString()
                    }
                    snackBar.dismiss()
                }
                "Phone" -> {
                    if (TextUtils.isEmpty(editTextInput.text.toString())) {
                        editTextInput.error = "Cannot be empty"
                    } else {
                        textViewPhone.text = editTextInput.text.toString()
                    }
                    snackBar.dismiss()
                }
            }
        }

        snackBarLayout.addView(view)
        snackBar.setBackgroundTint(resources.getColor(R.color.colorWhite))
        snackBar.show()

    }

    private fun save() {
        val name = textViewName.text.toString()
        val phone = textViewPhone.text.toString()

        if (name == "Username") {
            Snackbar.make(
                findViewById(android.R.id.content),
                "Please add a username",
                Snackbar.LENGTH_LONG
            ).show()
        } else if (phone == "Phone number") {
            Snackbar.make(
                findViewById(android.R.id.content),
                "Please add a phone number",
                Snackbar.LENGTH_LONG
            ).show()
        } else {
            loadingAlert()

            if (imagePath == "empty") {
                Toast.makeText(this@AccountActivity, "Saving", Toast.LENGTH_LONG).show()
                val db =
                    database.getReference("green-orchard").child("users")
                        .child(auth.currentUser!!.uid)
                db.child("name").setValue(name)
                db.child("phone").setValue(phone)
                    .addOnSuccessListener {
                        alert.cancel()
                        Snackbar.make(
                            findViewById(android.R.id.content),
                            "Profile update success",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    .addOnFailureListener {
                        alert.cancel()
                        Snackbar.make(
                            findViewById(android.R.id.content),
                            "Profile update failed",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
            } else {
                Toast.makeText(this@AccountActivity, "Uploading", Toast.LENGTH_LONG).show()
                val byteArrayOutputStream = ByteArrayOutputStream()
                getBitmap().compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)

                val data = byteArrayOutputStream.toByteArray()
                val storage = storageRef.reference.child("green-orchard")
                    .child("profiles/" + auth.uid.toString())
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

                            val db =
                                database.getReference("green-orchard").child("users")
                                    .child(auth.currentUser!!.uid)
                            db.child("name").setValue(name)
                            db.child("phone").setValue(phone)
                            db.child("profileImage").setValue(downloadUri.toString())
                                .addOnSuccessListener {
                                    alert.cancel()
                                    Snackbar.make(
                                        findViewById(android.R.id.content),
                                        "Profile update success",
                                        Snackbar.LENGTH_LONG
                                    ).show()
                                }
                                .addOnFailureListener {
                                    alert.cancel()
                                    Snackbar.make(
                                        findViewById(android.R.id.content),
                                        "Profile update failed",
                                        Snackbar.LENGTH_LONG
                                    ).show()
                                }

                        } else {
                            alert.cancel()
                            Snackbar.make(
                                findViewById(android.R.id.content),
                                "Profile update failed",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                })
            }


        }

    }

    private fun getBitmap(): Bitmap {
        return (imageViewProfile.drawable as BitmapDrawable).bitmap
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                save()
            }
            android.R.id.home -> {
                loadingAlert()
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
                            if (snapshot.child("name").exists() && snapshot.child("phone")
                                    .exists()
                            ) {
                                alert.cancel()
                                startActivity(
                                    Intent(
                                        this@AccountActivity,
                                        CategoriesActivity::class.java
                                    )
                                )
                                finish()
                            } else {
                                alert.cancel()
                                Snackbar.make(
                                    findViewById(android.R.id.content),
                                    "Update your profile",
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }

                        }

                    })
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkPermission(): Boolean {
        val resultReadExternalStorage =
            ContextCompat.checkSelfPermission(applicationContext, READ_EXTERNAL_STORAGE)
        val resultWriteExternalStorage =
            ContextCompat.checkSelfPermission(applicationContext, WRITE_EXTERNAL_STORAGE)

        return resultReadExternalStorage == PackageManager.PERMISSION_GRANTED && resultWriteExternalStorage == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 200 && grantResults.isNotEmpty()) {
            val readAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
            val writeAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED

            if (!readAccepted && !writeAccepted) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Storage permission required",
                    Snackbar.LENGTH_LONG
                ).show()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
                        showMessageOKCancel("You need to allow access to both the permissions",
                            DialogInterface.OnClickListener { dialog, which ->
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(
                                        arrayOf(
                                            READ_EXTERNAL_STORAGE,
                                            WRITE_EXTERNAL_STORAGE
                                        ),
                                        200
                                    )
                                }
                            })
                        return
                    }
                }
            } else {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Permissions granted.",
                    Snackbar.LENGTH_LONG
                ).show()
            }

        }
    }

    private fun showMessageOKCancel(
        message: String,
        okListener: DialogInterface.OnClickListener
    ) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 300 && resultCode == Activity.RESULT_OK) {
            imagePath = data!!.data.toString()
            Picasso.get().load(data!!.data.toString()).transform(CircleImageView())
                .into(imageViewProfile)
        }
    }

    private fun getDetails() {
        loadingAlert()
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
                    if (snapshot.child("name").exists()) {
                        textViewName.text = snapshot.child("name").value.toString()
                    }

                    if (snapshot.child("phone").exists()) {
                        textViewPhone.text = snapshot.child("phone").value.toString()
                    }

                    if (snapshot.child("profileImage").exists()) {
                        Picasso.get().load(snapshot.child("profileImage").value.toString())
                            .transform(CircleImageView())
                            .placeholder(R.drawable.loading)
                            .into(imageViewProfile)
                    }

                    alert.cancel()
                }

            })
    }

    private fun loadingAlert() {
        val alertDialog = AlertDialog.Builder(this@AccountActivity)
        alertDialog.setCancelable(false)
        alertDialog.setMessage("Loading ...")
        alert = alertDialog.create()
        alert.show()
    }

}