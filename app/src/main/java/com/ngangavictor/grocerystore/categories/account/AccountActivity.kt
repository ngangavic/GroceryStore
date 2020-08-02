package com.ngangavictor.grocerystore.categories.account

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.ngangavictor.grocerystore.R
import com.ngangavictor.grocerystore.categories.product.ProductActivity


class AccountActivity : AppCompatActivity() {

    private lateinit var imageViewProfile: ImageView
    private lateinit var imageViewEditName: ImageView
    private lateinit var imageViewEditPhone: ImageView

    private lateinit var textViewName: TextView
    private lateinit var textViewPhone: TextView
    private lateinit var textViewAccountType: TextView

    private lateinit var switchUser: Switch

    private lateinit var buttonAdd: Button

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

        clickListeners()

        if (!checkPermission()) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    READ_EXTERNAL_STORAGE,
                    WRITE_EXTERNAL_STORAGE
                ), 200
            )
        }

    }

    private fun clickListeners() {
        buttonAdd.setOnClickListener {
            startActivity(Intent(this@AccountActivity, ProductActivity::class.java))
            finish()
        }
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

}