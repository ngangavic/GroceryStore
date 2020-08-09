package com.ngangavictor.grocerystore.categories.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ngangavictor.grocerystore.R
import com.ngangavictor.grocerystore.adpters.CategoriesAdapter
import com.ngangavictor.grocerystore.categories.CategoriesActivity
import com.ngangavictor.grocerystore.categories.ui.category.CategoryFragment
import com.ngangavictor.grocerystore.models.CategoryModel

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var categoriesAdapter: CategoriesAdapter

    private lateinit var fruitCategoryList: MutableList<CategoryModel>
    private lateinit var meatCategoryList: MutableList<CategoryModel>
    private lateinit var dairyCategoryList: MutableList<CategoryModel>

    private lateinit var recyclerViewFruits: RecyclerView
    private lateinit var recyclerViewMeat: RecyclerView
    private lateinit var recyclerViewDairy: RecyclerView

    private lateinit var root: View

    private lateinit var textViewFruitsMore:TextView
    private lateinit var textViewMeatMore:TextView
    private lateinit var textViewDairyMore:TextView
    private lateinit var textViewFruitsMessage:TextView
    private lateinit var textViewDairyMessage:TextView
    private lateinit var textViewMeatMessage:TextView

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        root = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerViewFruits = root.findViewById(R.id.recyclerViewFruits)
        recyclerViewMeat = root.findViewById(R.id.recyclerViewMeat)
        recyclerViewDairy = root.findViewById(R.id.recyclerViewDairy)

        recyclerViewFruits.visibility=View.GONE
        recyclerViewMeat.visibility=View.GONE
        recyclerViewDairy.visibility=View.GONE

        textViewFruitsMore = root.findViewById(R.id.textViewFruitsMore)
        textViewMeatMore = root.findViewById(R.id.textViewMeatMore)
        textViewDairyMore = root.findViewById(R.id.textViewDairyMore)
        textViewFruitsMessage = root.findViewById(R.id.textViewFruitsMessage)
        textViewDairyMessage = root.findViewById(R.id.textViewDairyMessage)
        textViewMeatMessage = root.findViewById(R.id.textViewMeatMessage)

        textViewFruitsMessage.visibility=View.GONE
        textViewDairyMessage.visibility=View.GONE
        textViewMeatMessage.visibility=View.GONE

        recyclerViewFruits.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerViewFruits.setHasFixedSize(true)

        recyclerViewMeat.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerViewMeat.setHasFixedSize(true)

        recyclerViewDairy.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerViewDairy.setHasFixedSize(true)

        fruitCategoryList = ArrayList()
        meatCategoryList = ArrayList()
        dairyCategoryList = ArrayList()

        homeViewModel.getDairyCategory().observe(viewLifecycleOwner, Observer {

            if (it.isEmpty()){
                textViewDairyMessage.text="No Products"
                textViewDairyMessage.visibility=View.VISIBLE
            }else {

                dairyCategoryList = it as MutableList<CategoryModel>

                categoriesAdapter = CategoriesAdapter(
                    requireContext(),
                    dairyCategoryList as ArrayList<CategoryModel>
                )

                categoriesAdapter.notifyDataSetChanged()

                recyclerViewDairy.adapter = categoriesAdapter
                recyclerViewDairy.visibility=View.VISIBLE
                textViewDairyMessage.visibility=View.GONE
            }

        })

        homeViewModel.getMeatCategory().observe(viewLifecycleOwner, Observer {

            if (it.isEmpty()){
                textViewMeatMessage.text="No Products"
                textViewMeatMessage.visibility=View.VISIBLE
            }else {

                meatCategoryList = it as MutableList<CategoryModel>

                categoriesAdapter = CategoriesAdapter(
                    requireContext(),
                    meatCategoryList as ArrayList<CategoryModel>
                )

                categoriesAdapter.notifyDataSetChanged()

                recyclerViewMeat.adapter = categoriesAdapter
                recyclerViewMeat.visibility=View.VISIBLE
                textViewMeatMessage.visibility=View.GONE
            }

        })

        homeViewModel.getFruitCategory().observe(viewLifecycleOwner, Observer {

            if (it.isEmpty()) {
                textViewFruitsMessage.text = "No products"
                textViewFruitsMessage.visibility=View.VISIBLE
            }else{

            fruitCategoryList = it as MutableList<CategoryModel>

            categoriesAdapter = CategoriesAdapter(
                requireContext(),
                fruitCategoryList as ArrayList<CategoryModel>
            )

            categoriesAdapter.notifyDataSetChanged()

            recyclerViewFruits.adapter = categoriesAdapter
                recyclerViewFruits.visibility=View.VISIBLE
                textViewFruitsMessage.visibility=View.GONE

        }

        })

        textViewFruitsMore.setOnClickListener {
            val categoryFragment= CategoryFragment.newInstance("Fruits")
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, categoryFragment)
            fragmentTransaction.commit()
        }

        textViewMeatMore.setOnClickListener {
            val categoryFragment= CategoryFragment.newInstance("Meat")
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, categoryFragment)
            fragmentTransaction.commit()
        }

        textViewDairyMore.setOnClickListener {
            val categoryFragment= CategoryFragment.newInstance("Dairy")
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, categoryFragment)
            fragmentTransaction.commit()
        }

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance() = CategoryFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

}