package com.ngangavictor.grocerystore.repos

import androidx.lifecycle.LiveData
import com.ngangavictor.grocerystore.models.Cart
import com.ngangavictor.grocerystore.room.CartDao

class CartRepository(private val cartDao: CartDao) {

    val allCartItems: LiveData<List<Cart>> = cartDao.getAllCartItems()
    val getTotalPrice=cartDao.getTotalPrice()

    fun addToCart(cart: Cart) {
        cartDao.addToCart(cart)
    }

    fun clearCart() {
        cartDao.clearCart()
    }

    fun deleteCartItem(key: String) {
        cartDao.deleteCartItem(key)
    }

    fun updateCartItem(key: String, prodQuantity: Int,prodTotal:Int) {
        cartDao.updateCartItem(key, prodQuantity,prodTotal)
    }

    fun checkIfItemExists(key: String): Boolean {
        return cartDao.checkIfItemExist(key)
    }

    fun getCartItemQuantity(key: String): Int {
        return cartDao.getCartItemQuantity(key)
    }

}