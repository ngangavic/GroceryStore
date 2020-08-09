package com.ngangavictor.grocerystore.repos

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseRepository {

    private val database: FirebaseDatabase = Firebase.database


    fun getProducts(): DatabaseReference {
        return database.getReference("green-orchard").child("products")
    }

}