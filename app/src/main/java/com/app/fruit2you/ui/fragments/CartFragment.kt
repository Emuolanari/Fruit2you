package com.app.fruit2you.ui.fragments

import android.os.Bundle
import android.text.TextUtils.replace
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import com.app.fruit2you.ui.Fruit2YouViewModel
import com.app.fruit2you.ui.Fruit2YouViewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.fruit2you.R
import com.app.fruit2you.utilities.FruitsAdapter
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.app.fruit2you.data.database.entities.FruitItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.cart_fragment.*



class CartFragment: Fragment(R.layout.cart_fragment), KodeinAware {
    override val kodein by kodein()
    private val factory: Fruit2YouViewModelFactory by instance()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val homeFragment = HomeFragment()
        val viewModel = ViewModelProviders.of(this, factory).get(Fruit2YouViewModel::class.java)
        val adapter = FruitsAdapter(listOf(), viewModel)
        cartRecyclerView.layoutManager = LinearLayoutManager(activity)
        cartRecyclerView.adapter = adapter

        fun setCurrentFragment(fragment: Fragment){
            parentFragmentManager?.beginTransaction()?.apply{
                replace(R.id.flFragment, homeFragment)
                addToBackStack(null)
                commit()
            }
        }
        viewModel.numberOfCartItems().observe(viewLifecycleOwner, Observer <Int> {

            val x = it
            if (x==null){
                checkout.visibility =  View.GONE
                addItems.visibility = View.VISIBLE
            }
        })

        addItems.setOnClickListener {
            setCurrentFragment(homeFragment)
        }


        viewModel.getAllShoppingItems().observe(viewLifecycleOwner, Observer <List<FruitItem>> {
            adapter.items=it
            adapter.notifyDataSetChanged()
        })

    }
}