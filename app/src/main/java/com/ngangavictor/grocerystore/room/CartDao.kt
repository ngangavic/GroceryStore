package com.ngangavictor.grocerystore.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.ngangavictor.grocerystore.models.Cart

@Dao
interface CartDao {

    @Insert
    fun addToCart(cart: Cart)

    @Query("DELETE FROM cart_table")
    fun clearCart()

    @Query("DELETE FROM cart_table WHERE `key`=:key")
    fun deleteCartItem(key: String)

    @Query("UPDATE cart_table SET prodQuantity=:prodQuantity WHERE `key`=:key")
    fun updateCartItem(key: String, prodQuantity: Int)

    @Query("SELECT * FROM cart_table")
    fun getAllCartItems(): LiveData<List<Cart>>

    @Query("SELECT EXISTS(SELECT * FROM cart_table WHERE `key` = :key)")
    fun checkIfItemExist(key: String): Boolean

    @Query("SELECT prodQuantity FROM cart_table WHERE `key`=:key")
    fun getCartItemQuantity(key: String): Int

}