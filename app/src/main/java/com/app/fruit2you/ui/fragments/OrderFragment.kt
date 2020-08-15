package com.app.fruit2you.ui.fragments


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.fruit2you.R
import com.app.fruit2you.data.database.entities.FruitItem
import com.app.fruit2you.ui.Fruit2YouViewModel
import com.app.fruit2you.ui.Fruit2YouViewModelFactory
import com.app.fruit2you.ui.LoginActivity
import com.flutterwave.raveandroid.rave_java_commons.Meta
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.order_fragment.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.text.SimpleDateFormat
import java.util.*


class OrderFragment: Fragment(R.layout.order_fragment), KodeinAware {
    override val kodein by kodein()
    private val factory: Fruit2YouViewModelFactory by instance()
    private lateinit var check:String
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProvider(this, factory).get(Fruit2YouViewModel::class.java)
        val homeFragment = HomeFragment()
        val cartFragment = CartFragment()

        orderButton.setOnClickListener {
            viewModel.numberOfCartItems().observe(viewLifecycleOwner, Observer <Int> {
                val x = it
                if (x==null){
                    setCurrentFragment(homeFragment)
                    activity?.bottomNavigationView?.selectedItemId =  R.id.miHome
                }
                else{
                    setCurrentFragment(cartFragment)
                    activity?.bottomNavigationView?.selectedItemId =  R.id.miCart
                }
            })
        }
    }

    private fun setCurrentFragment(fragment: Fragment){
        parentFragmentManager.beginTransaction().apply{
            replace(R.id.flFragment, fragment)
            addToBackStack(null)
            commit()
        }
    }




}