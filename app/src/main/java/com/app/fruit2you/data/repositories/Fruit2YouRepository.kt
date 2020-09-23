package com.app.fruit2you.data.repositories

import com.app.fruit2you.data.database.Fruit2YouDatabase
import com.app.fruit2you.data.database.entities.FruitItem

class Fruit2YouRepository(
    private val db:Fruit2YouDatabase
) {
    suspend fun upsert(item: FruitItem) = db.getFruitsDao().upsert(item)

    suspend fun delete(item: FruitItem) = db.getFruitsDao().delete(item)

    fun getAllShoppingItems() = db.getFruitsDao().getAllShoppingItems()

    fun numberOfCartItems() = db.getFruitsDao().numberOfCartItems()

    fun priceOfCartItems() = db.getFruitsDao().priceOfCartItems()

    suspend fun nukeTable() = db.getFruitsDao().nukeTable()

}