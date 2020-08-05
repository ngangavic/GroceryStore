package com.ngangavictor.grocerystore.categories.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.ngangavictor.grocerystore.models.CategoryModel
import com.ngangavictor.grocerystore.repos.FirebaseRepository

class HomeViewModel : ViewModel() {

    var firebaseRepository = FirebaseRepository()

    private val products = MutableLiveData<List<CategoryModel>>()

    fun getProducts(): LiveData<List<CategoryModel>> {
        firebaseRepository.getProducts().addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                Log.e("data",snapshot.toString())
                var productList : MutableList<CategoryModel> = mutableListOf()
                for (data in snapshot.children){
                    productList.add(CategoryModel(data.child("image").value.toString(),data.child("name").value.toString(),data.child("desc").value.toString(),data.child("price").value.toString(),data.child("quantity").value.toString(),data.child("category").value.toString()))
                }
                products.value=productList
            }

        })

        return products
    }

}