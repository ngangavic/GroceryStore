package com.ngangavictor.grocerystore.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ngangavictor.grocerystore.models.Cart

@Database(entities = arrayOf(Cart::class),version = 2,exportSchema = false)
abstract class CartRoomDatabase:RoomDatabase() {

    abstract fun cartDao():CartDao

    companion object{
        private var INSTANCE:CartRoomDatabase?=null

        fun getDatabase(context: Context):CartRoomDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CartRoomDatabase::class.java,
                    "store_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }

        }
    }


}