package com.ngangavictor.grocerystore.categories.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ngangavictor.grocerystore.R
import com.ngangavictor.grocerystore.adpters.CategoriesAdapter
import com.ngangavictor.grocerystore.categories.CategoriesActivity
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerViewFruits = root.findViewById(R.id.recyclerViewFruits)
        recyclerViewMeat = root.findViewById(R.id.recyclerViewMeat)
        recyclerViewDairy = root.findViewById(R.id.recyclerViewDairy)

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

        homeViewModel.getDairyCategory().observe(viewLifecycleOwner, Observer { it ->

            dairyCategoryList = it as MutableList<CategoryModel>

            categoriesAdapter = CategoriesAdapter(
                requireContext(),
                dairyCategoryList as ArrayList<CategoryModel>
            )

            categoriesAdapter.notifyDataSetChanged()

            recyclerViewDairy.adapter = categoriesAdapter

        })

        homeViewModel.getMeatCategory().observe(viewLifecycleOwner, Observer { it ->

            meatCategoryList = it as MutableList<CategoryModel>

            categoriesAdapter = CategoriesAdapter(
                requireContext(),
                meatCategoryList as ArrayList<CategoryModel>
            )

            categoriesAdapter.notifyDataSetChanged()

            recyclerViewMeat.adapter = categoriesAdapter

        })

        homeViewModel.getFruitCategory().observe(viewLifecycleOwner, Observer { it ->

            fruitCategoryList = it as MutableList<CategoryModel>

            categoriesAdapter = CategoriesAdapter(
                requireContext(),
                fruitCategoryList as ArrayList<CategoryModel>
            )

            categoriesAdapter.notifyDataSetChanged()

            recyclerViewFruits.adapter = categoriesAdapter

        })

        return root
    }
}