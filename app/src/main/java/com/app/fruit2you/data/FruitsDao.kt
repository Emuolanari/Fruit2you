package com.app.fruit2you.data

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.app.fruit2you.data.database.entities.FruitItem

@Dao
interface FruitsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: FruitItem)

    @Delete
    suspend fun delete(item: FruitItem)

    @Query("SELECT * FROM shopping_items")
    fun getAllShoppingItems(): LiveData<List<FruitItem>>
}