package com.app.fruit2you.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.app.fruit2you.R
import com.app.fruit2you.ui.fragments.CartFragment
import com.app.fruit2you.ui.fragments.HomeFragment
import com.app.fruit2you.ui.fragments.OrderFragment
import com.app.fruit2you.ui.fragments.ProfileFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setContentView(R.layout.activity_main)
        val homeFragment = HomeFragment()
        val cartFragment = CartFragment()
        val profileFragment = ProfileFragment()
        val orderFragment = OrderFragment()

        setCurrentFragment(homeFragment)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.miHome -> setCurrentFragment(homeFragment)
                R.id.miCart -> setCurrentFragment(cartFragment)
                R.id.miProfile -> setCurrentFragment(profileFragment)
                R.id.miOrder -> setCurrentFragment(orderFragment)
            }
            true
        }
        bottomNavigationView.getOrCreateBadge(R.id.miCart).apply {
            number = 10
            isVisible = true
        }

    }

    private fun setCurrentFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply{
            replace(R.id.flFragment, fragment)
            addToBackStack(null)
            commit()
        }
    }
}
