package com.ngangavictor.grocerystore.categories.ui.category

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.ngangavictor.grocerystore.models.CategoryModel
import com.ngangavictor.grocerystore.repos.FirebaseRepository

class CategoryViewModel() : ViewModel() {

    var firebaseRepository = FirebaseRepository()

    private val category = MutableLiveData<List<CategoryModel>>()

    var list: MutableList<CategoryModel> = mutableListOf()

    fun getData(param: String): LiveData<List<CategoryModel>> {
        list.clear()
        firebaseRepository.getProducts().addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                Log.e("data", snapshot.toString())

                for (data in snapshot.children) {
                    if (data.child("category").value.toString() == param) {
                        list.add(
                            CategoryModel(
                                data.child("image").value.toString(),
                                data.child("name").value.toString(),
                                data.child("desc").value.toString(),
                                data.child("price").value.toString(),
                                data.child("quantity").value.toString(),
                                data.key.toString(),
                                data.child("category").value.toString()
                            )
                        )
                    }
                }
                category.value = list
            }

        })

        return category
    }

}