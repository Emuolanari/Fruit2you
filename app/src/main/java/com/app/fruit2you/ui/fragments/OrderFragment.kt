package com.app.fruit2you.ui.fragments

import android.R.attr.x
import android.R.attr.y
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.fruit2you.R
import com.app.fruit2you.ui.Fruit2YouViewModel
import com.app.fruit2you.ui.Fruit2YouViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class OrderFragment: Fragment(R.layout.order_fragment), KodeinAware {
    override val kodein by kodein()
    private val factory: Fruit2YouViewModelFactory by instance()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProvider(this, factory).get(Fruit2YouViewModel::class.java)
        Log.d("OrdersAct",viewModel.getAllShoppingItems().value.toString())

    }



}