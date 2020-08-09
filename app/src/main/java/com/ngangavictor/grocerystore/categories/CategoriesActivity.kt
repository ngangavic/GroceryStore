package com.ngangavictor.grocerystore.categories

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
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
import com.ngangavictor.grocerystore.categories.account.AccountActivity
import com.ngangavictor.grocerystore.login.LoginActivity
import com.ngangavictor.grocerystore.utils.CircleImageView
import com.ngangavictor.grocerystore.utils.LocalStoragePrefs
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import java.lang.Exception

class CategoriesActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var textViewTitle: TextView
    private lateinit var imageViewOpenDrawer: ImageView
    private lateinit var imageViewNotifications: ImageView
    private lateinit var imageViewProfile: ImageView

    private lateinit var imageViewSearch: ImageView
    private lateinit var imageViewList: ImageView
    private lateinit var imageViewBasket: ImageView

    private lateinit var layoutSearch: ConstraintLayout
    private lateinit var layoutList: ConstraintLayout
    private lateinit var layoutCart: ConstraintLayout

    private lateinit var floatingActionButtonCancelCart: FloatingActionButton
    private lateinit var floatingActionButtonCancelList: FloatingActionButton
    private lateinit var floatingActionButtonCancelSearch: FloatingActionButton

    private lateinit var recyclerViewSearch: RecyclerView
    private lateinit var recyclerViewList: RecyclerView
    private lateinit var recyclerViewCart: RecyclerView

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storageRef: FirebaseStorage

    private lateinit var localStoragePrefs: LocalStoragePrefs

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)

        imageViewSearch = findViewById(R.id.imageViewSearch)
        imageViewList = findViewById(R.id.imageViewList)
        imageViewBasket = findViewById(R.id.imageViewBasket)

        layoutSearch = findViewById(R.id.layoutSearch)
        layoutList = findViewById(R.id.layoutList)
        layoutCart = findViewById(R.id.layoutCart)

        floatingActionButtonCancelCart = findViewById(R.id.floatingActionButtonCancelCart)
        floatingActionButtonCancelList = findViewById(R.id.floatingActionButtonCancelList)
        floatingActionButtonCancelSearch = findViewById(R.id.floatingActionButtonCancelSearch)

        recyclerViewSearch = findViewById(R.id.recyclerViewSearch)
        recyclerViewList = findViewById(R.id.recyclerViewList)
        recyclerViewCart = findViewById(R.id.recyclerViewCart)

        auth = Firebase.auth
        database = Firebase.database
        storageRef = Firebase.storage

        localStoragePrefs = LocalStoragePrefs(this)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home
            ), drawerLayout
        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setCustomView(R.layout.custom_action_bar)
        supportActionBar?.elevation = 0F
        val view = supportActionBar!!.customView
        textViewTitle = view.findViewById(R.id.textViewTitle)
        imageViewOpenDrawer = view.findViewById(R.id.imageViewOpenDrawer)
        imageViewNotifications = view.findViewById(R.id.imageViewNotifications)
        imageViewProfile = view.findViewById(R.id.imageViewProfile)

        textViewTitle.text="All Categories"

        imageViewOpenDrawer.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        clickListeners()

        val headerView = navView.getHeaderView(0)
        headerView.findViewById<TextView>(R.id.sidebar_name).text =
            localStoragePrefs.getAccDetailsPref("name")
        headerView.findViewById<TextView>(R.id.textViewEmail).text =
            auth.currentUser!!.email.toString()

        navView.setNavigationItemSelectedListener(object :
            NavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.nav_home -> {
                        return true
                    }
                    R.id.nav_fruits -> {
                        return true
                    }
                    R.id.nav_meat -> {
                        return true
                    }
                    R.id.nav_dairy -> {
                        return true
                    }
                    R.id.nav_account -> {
                        startActivity(Intent(this@CategoriesActivity, AccountActivity::class.java))
                        finish()
                        return true
                    }
                    R.id.nav_about -> {
                        return true
                    }
                    R.id.nav_logout -> {
                        localStoragePrefs.clearAccDetailsPref()
                        auth.signOut()
                        startActivity(Intent(this@CategoriesActivity, LoginActivity::class.java))
                        finish()
                        return true
                    }
                }
                return false
            }


        })

        setProfileImage()

    }

    private fun clickListeners() {

        imageViewProfile.setOnClickListener {
            startActivity(Intent(this@CategoriesActivity,AccountActivity::class.java))
            finish()
        }

        imageViewSearch.setOnClickListener {
            layoutSearch.visibility = View.VISIBLE
            layoutList.visibility = View.GONE
            layoutCart.visibility = View.GONE
            recyclerViewSearch.visibility = View.GONE
            floatingActionButtonCancelSearch.visibility = View.GONE
        }

        imageViewList.setOnClickListener {
            layoutSearch.visibility = View.GONE
            layoutList.visibility = View.VISIBLE
            layoutCart.visibility = View.GONE
        }

        imageViewBasket.setOnClickListener {
            layoutSearch.visibility = View.GONE
            layoutList.visibility = View.GONE
            layoutCart.visibility = View.VISIBLE
        }

        floatingActionButtonCancelCart.setOnClickListener {
            layoutSearch.visibility = View.GONE
            layoutList.visibility = View.GONE
            layoutCart.visibility = View.GONE
        }

        floatingActionButtonCancelList.setOnClickListener {
            layoutSearch.visibility = View.GONE
            layoutList.visibility = View.GONE
            layoutCart.visibility = View.GONE
        }

        floatingActionButtonCancelSearch.setOnClickListener {
            layoutSearch.visibility = View.GONE
            layoutList.visibility = View.GONE
            layoutCart.visibility = View.GONE
        }
    }

    private fun setProfileImage() {
        database.getReference("green-orchard").child("users")
            .child(auth.currentUser!!.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Error:" + error.message,
                        Snackbar.LENGTH_LONG
                    ).show()
                }

                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.child("profileImage").exists()) {

                        Picasso.get().load(snapshot.child("profileImage").value.toString()).transform(CircleImageView()).placeholder(R.drawable.loading).networkPolicy(
                            NetworkPolicy.OFFLINE).into(imageViewProfile,object: Callback {
                            override fun onSuccess() {

                            }

                            override fun onError(e: Exception?) {
                                Log.e("PICASSO:",e?.message.toString())
                                Picasso.get().load(snapshot.child("profileImage").value.toString())
                                    .transform(CircleImageView())
                                    .placeholder(R.drawable.loading)
                                    .into(imageViewProfile)
                            }

                        })


                    }

                }

            })
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}