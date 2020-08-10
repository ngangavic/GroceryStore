package com.ngangavictor.grocerystore.utils

import android.content.Context
import android.content.SharedPreferences

class LocalStoragePrefs(context: Context) {

    companion object {
        const val account_details_pref = "account_pref"
        const val products_cart="products_cart"
    }

    private val sharedPrefAccDetails: SharedPreferences =
        context.getSharedPreferences(account_details_pref, 0)

    private val sharedPrefProdCart: SharedPreferences =
        context.getSharedPreferences(products_cart, 0)

    //PRODUCTS CART DETAILS

    fun addCart(prodKey:String,prodName:String,prodPrice:String,prodImage:String,prodQty:String,prodDesc:String){

    }

    fun removeCartItem(){

    }

    fun clearCart(){

    }

    //ACCOUNT DETAILS SHARED PREF
    fun saveAccDetailsPref(KEY_NAME: String, value: String) {
        val editor: SharedPreferences.Editor = sharedPrefAccDetails.edit()
        editor.putString(KEY_NAME, value)
        editor.apply()
    }

    fun getAccDetailsPref(KEY_NAME: String): String? {
        return sharedPrefAccDetails.getString(KEY_NAME, null)
    }

    fun clearAccDetailsPref() {
        val editor: SharedPreferences.Editor = sharedPrefAccDetails.edit()
        editor.clear()
        editor.apply()
    }


}