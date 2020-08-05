package com.ngangavictor.grocerystore.categories.ui.home

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
import com.ngangavictor.grocerystore.adpters.CategoryAdapter
import com.ngangavictor.grocerystore.models.CategoryModel

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var categoryAdapter: CategoryAdapter

    private lateinit var categoryList: MutableList<CategoryModel>

    private lateinit var recyclerViewFruits:RecyclerView
    private lateinit var root:View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_home, container, false)

        categoryList = ArrayList()
        recyclerViewFruits=root.findViewById(R.id.recyclerViewFruits)
        recyclerViewFruits.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)
        recyclerViewFruits.setHasFixedSize(true)
        homeViewModel.getProducts().observe(viewLifecycleOwner, Observer { it->
            categoryList = it as MutableList<CategoryModel>

            categoryAdapter = CategoryAdapter(
                categoryList as ArrayList<CategoryModel>
            )

            categoryAdapter.notifyDataSetChanged()

            recyclerViewFruits.adapter = categoryAdapter

        })

        return root
    }
}