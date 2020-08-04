package com.app.fruit2you.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.fruit2you.data.repositories.Fruit2YouRepository

@Suppress("UNCHECKED_CAST")
class Fruit2YouViewModelFactory(
    private val repository: Fruit2YouRepository
): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return Fruit2YouViewModel(repository) as T
    }
}