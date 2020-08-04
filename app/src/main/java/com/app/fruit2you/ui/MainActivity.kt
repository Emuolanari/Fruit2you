package com.app.fruit2you.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.app.fruit2you.R
import com.app.fruit2you.ui.fragments.CartFragment
import com.app.fruit2you.ui.fragments.HomeFragment
import com.app.fruit2you.ui.fragments.OrderFragment
import com.app.fruit2you.ui.fragments.ProfileFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein


class MainActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    //private val factory: Fruit2YouViewModel by instance()

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

   /* override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        } else {
            supportFragmentManager.popBackStack()
        }
    }*/
}
