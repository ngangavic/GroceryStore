package com.ngangavictor.grocerystore.categories.ui.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ngangavictor.grocerystore.R
import com.ngangavictor.grocerystore.adpters.CategoriesAdapter
import com.ngangavictor.grocerystore.adpters.CategoryAdapter
import com.ngangavictor.grocerystore.models.CategoryModel

class CategoryFragment : Fragment() {

    private lateinit var categoryViewModel: CategoryViewModel

    private lateinit var root:View

    private lateinit var imageViewSort:ImageView

    private lateinit var textViewSort:TextView
    private lateinit var textViewMessage:TextView

    private lateinit var gridViewCategory:GridView

    private lateinit var categoryAdapter: CategoryAdapter

    private lateinit var categoryList: MutableList<CategoryModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        categoryViewModel=ViewModelProviders.of(this).get(CategoryViewModel::class.java)

        root=inflater.inflate(R.layout.fragment_category, container, false)

        imageViewSort=root.findViewById(R.id.imageViewSort)
        textViewSort=root.findViewById(R.id.textViewSort)
        textViewMessage=root.findViewById(R.id.textViewMessage)
        gridViewCategory=root.findViewById(R.id.gridViewCategory)

        gridViewCategory.visibility=View.GONE

        categoryList = ArrayList()

        categoryViewModel.getData(requireArguments().getString("category").toString()).observe(viewLifecycleOwner, Observer {

            if (it.isEmpty()){
                textViewMessage.text="No Products"
            }else {

                categoryList = it as MutableList<CategoryModel>

                categoryAdapter = CategoryAdapter(
                    requireContext(),
                    categoryList as ArrayList<CategoryModel>
                )

                categoryAdapter.notifyDataSetChanged()

                gridViewCategory.adapter = categoryAdapter
                textViewMessage.visibility=View.GONE
                gridViewCategory.visibility=View.VISIBLE
            }

        })

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            CategoryFragment().apply {
                arguments = Bundle().apply {
                    putString("category", param1)
                }
            }
    }
}