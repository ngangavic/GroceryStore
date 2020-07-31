package com.ngangavictor.grocerystore.categories

import android.os.Bundle
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
import com.ngangavictor.grocerystore.R

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)

        imageViewSearch=findViewById(R.id.imageViewSearch)
        imageViewList=findViewById(R.id.imageViewList)
        imageViewBasket=findViewById(R.id.imageViewBasket)

        layoutSearch=findViewById(R.id.layoutSearch)
        layoutList=findViewById(R.id.layoutList)
        layoutCart=findViewById(R.id.layoutCart)

        floatingActionButtonCancelCart=findViewById(R.id.floatingActionButtonCancelCart)
        floatingActionButtonCancelList=findViewById(R.id.floatingActionButtonCancelList)
        floatingActionButtonCancelSearch=findViewById(R.id.floatingActionButtonCancelSearch)

        recyclerViewSearch=findViewById(R.id.recyclerViewSearch)
        recyclerViewList=findViewById(R.id.recyclerViewList)
        recyclerViewCart=findViewById(R.id.recyclerViewCart)

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

        imageViewOpenDrawer.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        clickListeners()

    }

    private fun clickListeners(){
        imageViewSearch.setOnClickListener {
            layoutSearch.visibility=View.VISIBLE
            layoutList.visibility=View.GONE
            layoutCart.visibility=View.GONE
            recyclerViewSearch.visibility=View.GONE
            floatingActionButtonCancelSearch.visibility=View.GONE
        }

        imageViewList.setOnClickListener {
            layoutSearch.visibility=View.GONE
            layoutList.visibility=View.VISIBLE
            layoutCart.visibility=View.GONE
        }

        imageViewBasket.setOnClickListener {
            layoutSearch.visibility=View.GONE
            layoutList.visibility=View.GONE
            layoutCart.visibility=View.VISIBLE
        }

        floatingActionButtonCancelCart.setOnClickListener {
            layoutSearch.visibility=View.GONE
            layoutList.visibility=View.GONE
            layoutCart.visibility=View.GONE
        }

        floatingActionButtonCancelList.setOnClickListener {
            layoutSearch.visibility=View.GONE
            layoutList.visibility=View.GONE
            layoutCart.visibility=View.GONE
        }

        floatingActionButtonCancelSearch.setOnClickListener {
            layoutSearch.visibility=View.GONE
            layoutList.visibility=View.GONE
            layoutCart.visibility=View.GONE
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.categories, menu)
//        return true
//    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}