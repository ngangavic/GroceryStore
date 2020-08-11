package com.ngangavictor.grocerystore.categories

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.ngangavictor.grocerystore.adpters.CartAdapter
import com.ngangavictor.grocerystore.adpters.SearchAdapter
import com.ngangavictor.grocerystore.categories.account.AccountActivity
import com.ngangavictor.grocerystore.categories.ui.cart.CartViewModel
import com.ngangavictor.grocerystore.categories.ui.category.CategoryFragment
import com.ngangavictor.grocerystore.categories.ui.home.HomeFragment
import com.ngangavictor.grocerystore.login.LoginActivity
import com.ngangavictor.grocerystore.models.Cart
import com.ngangavictor.grocerystore.models.CategoryModel
import com.ngangavictor.grocerystore.utils.CircleImageView
import com.ngangavictor.grocerystore.utils.LocalStoragePrefs
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import java.util.*

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

    private lateinit var bottomRootLayout: LinearLayout

    private lateinit var floatingActionButtonCancelCart: FloatingActionButton
    private lateinit var floatingActionButtonCancelList: FloatingActionButton
    private lateinit var floatingActionButtonCancelSearch: FloatingActionButton

    private lateinit var gridViewSearch: GridView

    private lateinit var recyclerViewList: RecyclerView
    private lateinit var recyclerViewCart: RecyclerView

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storageRef: FirebaseStorage

    private lateinit var localStoragePrefs: LocalStoragePrefs

    private lateinit var editTextTextSearch: EditText

    private lateinit var searchList: MutableList<CategoryModel>
    private lateinit var cartList: MutableList<Cart>

    private lateinit var searchAdapter: SearchAdapter
    private lateinit var cartAdapter: CartAdapter

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

        bottomRootLayout = findViewById(R.id.bottomRootLayout)

        floatingActionButtonCancelCart = findViewById(R.id.floatingActionButtonCancelCart)
        floatingActionButtonCancelList = findViewById(R.id.floatingActionButtonCancelList)
        floatingActionButtonCancelSearch = findViewById(R.id.floatingActionButtonCancelSearch)

        gridViewSearch = findViewById(R.id.gridViewSearch)
        recyclerViewList = findViewById(R.id.recyclerViewList)
        recyclerViewCart = findViewById(R.id.recyclerViewCart)

        auth = Firebase.auth
        database = Firebase.database
        storageRef = Firebase.storage

        localStoragePrefs = LocalStoragePrefs(this)

        cartViewModel = ViewModelProvider(this).get(CartViewModel::class.java)

        editTextTextSearch = findViewById(R.id.editTextTextSearch)

        searchList = ArrayList()
        cartList = ArrayList()

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setCustomView(R.layout.custom_action_bar)
        supportActionBar?.elevation = 0F
        val view = supportActionBar!!.customView
        textViewTitle = view.findViewById(R.id.textViewTitle)
        imageViewOpenDrawer = view.findViewById(R.id.imageViewOpenDrawer)
        imageViewNotifications = view.findViewById(R.id.imageViewNotifications)
        imageViewProfile = view.findViewById(R.id.imageViewProfile)

        textViewTitle.text = "All Categories"

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
                        val homeFragment = HomeFragment()
                        val fragmentTransaction = supportFragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.nav_host_fragment, homeFragment)
                        fragmentTransaction.commit()
                        textViewTitle.text = "All Categories"
                        drawerLayout.closeDrawer(GravityCompat.START)
                        return true
                    }
                    R.id.nav_fruits -> {
                        val categoryFragment = CategoryFragment.newInstance("Fruits")
                        val fragmentTransaction = supportFragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.nav_host_fragment, categoryFragment)
                        fragmentTransaction.commit()
                        drawerLayout.closeDrawer(GravityCompat.START)
                        textViewTitle.text = "Fruits"
                        return true
                    }
                    R.id.nav_meat -> {
                        val categoryFragment = CategoryFragment.newInstance("Meat")
                        val fragmentTransaction = supportFragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.nav_host_fragment, categoryFragment)
                        fragmentTransaction.commit()
                        textViewTitle.text = "Meat"
                        drawerLayout.closeDrawer(GravityCompat.START)
                        return true
                    }
                    R.id.nav_dairy -> {
                        val categoryFragment = CategoryFragment.newInstance("Dairy")
                        val fragmentTransaction = supportFragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.nav_host_fragment, categoryFragment)
                        fragmentTransaction.commit()
                        textViewTitle.text = "Dairy"
                        drawerLayout.closeDrawer(GravityCompat.START)
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

    private fun cart() {

        recyclerViewCart.layoutManager = LinearLayoutManager(this)
        recyclerViewCart.setHasFixedSize(true)

        cartViewModel.allCartItems.observe(this, Observer {
            cartList = it as MutableList<Cart>

            cartAdapter = CartAdapter(
                this,
                cartList as ArrayList<Cart>
            )

            cartAdapter.notifyDataSetChanged()

            recyclerViewCart.adapter = cartAdapter
            recyclerViewCart.visibility = View.VISIBLE
        })
    }

    private fun search() {
        editTextTextSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count == 0) {
                    searchList.clear()
                    floatingActionButtonCancelSearch.visibility = View.GONE
                    searchAdapter.notifyDataSetChanged()
                } else {
                    searchProducts(s.toString())
                }
            }

        })
    }

    private fun searchProducts(name: String) {
        val fetchProductQuery = database.getReference("green-orchard").child("products")
        fetchProductQuery.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                searchList.clear()

                for (postSnapshot in p0.children) {
                    Log.e("DATA", postSnapshot.value.toString())

                    if (postSnapshot.value.toString().contains(name)) {
                        Log.e("VALUE", postSnapshot.value.toString())
                        searchList.add(
                            CategoryModel(
                                postSnapshot.child("image").value.toString(),
                                postSnapshot.child("name").value.toString(),
                                postSnapshot.child("desc").value.toString(),
                                postSnapshot.child("price").value.toString(),
                                postSnapshot.child("quantity").value.toString(),
                                postSnapshot.key.toString(),
                                postSnapshot.child("category").value.toString()
                            )
                        )
                    }
                }
                searchAdapter =
                    SearchAdapter(this@CategoriesActivity, searchList as ArrayList<CategoryModel>)
                searchAdapter.notifyDataSetChanged()
                gridViewSearch.adapter = searchAdapter
                gridViewSearch.visibility = View.VISIBLE
                floatingActionButtonCancelSearch.visibility = View.VISIBLE
            }

        })
    }

    private fun clickListeners() {

        imageViewProfile.setOnClickListener {
            startActivity(Intent(this@CategoriesActivity, AccountActivity::class.java))
            finish()
        }

        imageViewSearch.setOnClickListener {

            DrawableCompat.setTint(
                imageViewSearch.drawable, ContextCompat.getColor(
                    applicationContext, R.color.colorWhite
                )
            )

            DrawableCompat.setTint(
                imageViewList.drawable, ContextCompat.getColor(
                    applicationContext, R.color.colorPrimaryDark
                )
            )

            DrawableCompat.setTint(
                imageViewBasket.drawable, ContextCompat.getColor(
                    applicationContext, R.color.colorPrimaryDark
                )
            )

            layoutSearch.visibility = View.VISIBLE
            layoutList.visibility = View.GONE
            layoutCart.visibility = View.GONE
            gridViewSearch.visibility = View.GONE
            floatingActionButtonCancelSearch.visibility = View.GONE
            bottomRootLayout.setBackgroundColor(resources.getColor(R.color.colorWhite))
            val textView5 = findViewById<TextView>(R.id.textView5)
            textView5.setTextColor(resources.getColor(R.color.colorBlack))
            search()
        }

        imageViewList.setOnClickListener {

            DrawableCompat.setTint(
                imageViewSearch.drawable, ContextCompat.getColor(
                    applicationContext, R.color.colorPrimaryDark
                )
            )

            DrawableCompat.setTint(
                imageViewList.drawable, ContextCompat.getColor(
                    applicationContext, R.color.colorWhite
                )
            )

            DrawableCompat.setTint(
                imageViewBasket.drawable, ContextCompat.getColor(
                    applicationContext, R.color.colorPrimaryDark
                )
            )

            layoutSearch.visibility = View.GONE
            layoutList.visibility = View.VISIBLE
            layoutCart.visibility = View.GONE
        }

        imageViewBasket.setOnClickListener {

            cartList.clear()

            DrawableCompat.setTint(
                imageViewSearch.drawable, ContextCompat.getColor(
                    applicationContext, R.color.colorPrimaryDark
                )
            )

            DrawableCompat.setTint(
                imageViewList.drawable, ContextCompat.getColor(
                    applicationContext, R.color.colorPrimaryDark
                )
            )

            DrawableCompat.setTint(
                imageViewBasket.drawable, ContextCompat.getColor(
                    applicationContext, R.color.colorWhite
                )
            )

            layoutSearch.visibility = View.GONE
            layoutList.visibility = View.GONE
            layoutCart.visibility = View.VISIBLE

            bottomRootLayout.setBackgroundColor(resources.getColor(R.color.colorWhite))
            val textView7 = findViewById<TextView>(R.id.textView7)
            textView7.setTextColor(resources.getColor(R.color.colorBlack))

            cart()
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
            searchList.clear()
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

                        Picasso.get().load(snapshot.child("profileImage").value.toString())
                            .transform(CircleImageView()).placeholder(R.drawable.loading)
                            .networkPolicy(
                                NetworkPolicy.OFFLINE
                            ).into(imageViewProfile, object : Callback {
                                override fun onSuccess() {

                                }

                                override fun onError(e: Exception?) {
                                    Log.e("PICASSO:", e?.message.toString())
                                    Picasso.get()
                                        .load(snapshot.child("profileImage").value.toString())
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

    companion object {
        lateinit var cartViewModel: CartViewModel
    }

}