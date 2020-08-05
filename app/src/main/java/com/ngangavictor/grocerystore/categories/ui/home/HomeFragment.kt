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
import com.ngangavictor.grocerystore.adpters.CategoryAdapter
import com.ngangavictor.grocerystore.models.CategoryModel

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var categoryAdapter: CategoryAdapter

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

            categoryAdapter = CategoryAdapter(
                dairyCategoryList as ArrayList<CategoryModel>
            )

            categoryAdapter.notifyDataSetChanged()

            recyclerViewDairy.adapter = categoryAdapter

        })

        homeViewModel.getMeatCategory().observe(viewLifecycleOwner, Observer { it ->

            meatCategoryList = it as MutableList<CategoryModel>

            categoryAdapter = CategoryAdapter(
                meatCategoryList as ArrayList<CategoryModel>
            )

            categoryAdapter.notifyDataSetChanged()

            recyclerViewMeat.adapter = categoryAdapter

        })

        homeViewModel.getFruitCategory().observe(viewLifecycleOwner, Observer { it ->

            fruitCategoryList = it as MutableList<CategoryModel>

            categoryAdapter = CategoryAdapter(
                fruitCategoryList as ArrayList<CategoryModel>
            )

            categoryAdapter.notifyDataSetChanged()

            recyclerViewFruits.adapter = categoryAdapter

        })

        return root
    }
}