package com.app.fruit2you.ui

import androidx.lifecycle.ViewModel
import com.app.fruit2you.data.database.entities.FruitItem
import com.app.fruit2you.data.repositories.Fruit2YouRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Fruit2YouViewModel(private val repository: Fruit2YouRepository): ViewModel() {
    fun upsert(fruit: FruitItem) = CoroutineScope(Dispatchers.IO).launch {
        repository.upsert(fruit)
    }
    fun delete(fruit: FruitItem) = CoroutineScope(Dispatchers.IO).launch {
        repository.delete(fruit)
    }

    fun getAllShoppingItems() = repository.getAllShoppingItems()
    fun numberOfCartItems() = repository.numberOfCartItems()
    fun priceOfCartItems() = repository.priceOfCartItems()
    fun nukeTable() = CoroutineScope(Dispatchers.IO).launch {
        repository.nukeTable()
    }
}