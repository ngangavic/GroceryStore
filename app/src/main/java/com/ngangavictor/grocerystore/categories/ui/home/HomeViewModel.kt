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

    private val fruitCategory = MutableLiveData<List<CategoryModel>>()
    private val meatCategory = MutableLiveData<List<CategoryModel>>()
    private val dairyCategory = MutableLiveData<List<CategoryModel>>()

    var fruitsList: MutableList<CategoryModel> = mutableListOf()
    var meatList: MutableList<CategoryModel> = mutableListOf()
    var dairyList: MutableList<CategoryModel> = mutableListOf()

    fun getDairyCategory(): LiveData<List<CategoryModel>> {
        dairyList.clear()
        firebaseRepository.getProducts().addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                Log.e("data", snapshot.toString())

                for (data in snapshot.children) {
                    if (data.child("category").value.toString() == "Dairy") {
                        dairyList.add(
                            CategoryModel(
                                data.child("image").value.toString(),
                                data.child("name").value.toString(),
                                data.child("desc").value.toString(),
                                data.child("price").value.toString(),
                                data.child("quantity").value.toString(),
                                data.key.toString()
                            )
                        )
                    }
                }
                dairyCategory.value = dairyList
            }

        })

        return dairyCategory
    }

    fun getMeatCategory(): LiveData<List<CategoryModel>> {
        meatList.clear()
        firebaseRepository.getProducts().addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                Log.e("data", snapshot.toString())

                for (data in snapshot.children) {
                    if (data.child("category").value.toString() == "Meat") {
                        meatList.add(
                            CategoryModel(
                                data.child("image").value.toString(),
                                data.child("name").value.toString(),
                                data.child("desc").value.toString(),
                                data.child("price").value.toString(),
                                data.child("quantity").value.toString(),
                                data.key.toString()
                            )
                        )
                    }
                }
                meatCategory.value = meatList
            }

        })

        return meatCategory
    }

    fun getFruitCategory(): LiveData<List<CategoryModel>> {
        fruitsList.clear()
        firebaseRepository.getProducts().addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                Log.e("data", snapshot.toString())

                for (data in snapshot.children) {
                    if (data.child("category").value.toString() == "Fruits") {
                        fruitsList.add(
                            CategoryModel(
                                data.child("image").value.toString(),
                                data.child("name").value.toString(),
                                data.child("desc").value.toString(),
                                data.child("price").value.toString(),
                                data.child("quantity").value.toString(),
                                data.key.toString()
                            )
                        )
                    }
                }
                fruitCategory.value = fruitsList
            }

        })

        return fruitCategory
    }

}