package com.app.fruit2you.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.app.fruit2you.data.database.entities.FruitItem

@Dao
interface FruitsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: FruitItem)

    @Delete
    suspend fun delete(item: FruitItem)

    @Query("SELECT * FROM shopping_items ORDER BY name DESC")
    fun getAllShoppingItems(): LiveData<List<FruitItem>>

    @Query("SELECT SUM(quantity) FROM shopping_items")
    fun numberOfCartItems(): LiveData<Int>

    @Query("SELECT SUM(amount) FROM shopping_items")
    fun priceOfCartItems(): LiveData<Int>

    @Query("DELETE FROM shopping_items")
    fun nukeTable()
}