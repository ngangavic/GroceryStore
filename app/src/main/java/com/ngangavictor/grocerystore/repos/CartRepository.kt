package com.ngangavictor.grocerystore.repos

import androidx.lifecycle.LiveData
import androidx.room.PrimaryKey
import com.ngangavictor.grocerystore.models.Cart
import com.ngangavictor.grocerystore.room.CartDao

class CartRepository(private val cartDao: CartDao) {

    val allCartItems : LiveData<List<Cart>> = cartDao.getAllCartItems()

    fun addToCart(cart: Cart){
        cartDao.addToCart(cart)
    }

    fun clearCart(){
        cartDao.clearCart()
    }

    fun deleteCartItem(cart: Cart){
        cartDao.deleteCartItem(cart)
    }

    fun updateCartItem(key: String,prodQuantity:Int){
        cartDao.updateCartItem(key,prodQuantity)
    }

    fun checkIfItemExists(key: String):Boolean{
       return cartDao.checkIfItemExist(key)
    }

    fun getCartItemQuantity(key: String):Int{
      return  cartDao.getCartItemQuantity(key)
    }

}