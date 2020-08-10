package com.ngangavictor.grocerystore.categories.ui.cart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.ngangavictor.grocerystore.models.Cart
import com.ngangavictor.grocerystore.repos.CartRepository
import com.ngangavictor.grocerystore.room.CartDao
import com.ngangavictor.grocerystore.room.CartRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartViewModel(application: Application) : AndroidViewModel(application) {

    private val cartRepository: CartRepository
    val allCartItems: LiveData<List<Cart>>
    var cartDao: CartDao

    init {
        cartDao = CartRoomDatabase.getDatabase(application).cartDao()
        cartRepository = CartRepository(cartDao)
        allCartItems = cartRepository.allCartItems
    }

    fun addCartItem(cart: Cart) = viewModelScope.launch(Dispatchers.IO) {
        cartRepository.addToCart(cart)
    }

    fun deleteCartItem(cart: Cart) = viewModelScope.launch(Dispatchers.IO) {
        cartRepository.deleteCartItem(cart)
    }

    fun clearCart() = viewModelScope.launch(Dispatchers.IO) {
        cartRepository.clearCart()
    }

    fun updateCartItem(key: String, prodQuantity: Int) = viewModelScope.launch(Dispatchers.IO) {
        cartRepository.updateCartItem(key, prodQuantity)
    }

    suspend fun checkIfItemExists(key: String): Boolean = withContext(Dispatchers.Default) {
        cartRepository.checkIfItemExists(key)
    }

    suspend fun getCartItemQuantity(key: String): Int = withContext(Dispatchers.Default) {
        cartRepository.getCartItemQuantity(key)
    }

}