package com.app.fruit2you.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.app.fruit2you.R
import com.app.fruit2you.ui.fragments.CartFragment
import com.app.fruit2you.ui.fragments.HomeFragment
import com.app.fruit2you.ui.fragments.OrderFragment
import com.app.fruit2you.ui.fragments.ProfileFragment
import com.flutterwave.raveandroid.RavePayActivity
import com.flutterwave.raveandroid.rave_java_commons.RaveConstants
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class MainActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    private val factory: Fruit2YouViewModelFactory by instance()

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProviders.of(this, factory).get(Fruit2YouViewModel::class.java)
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
        viewModel.numberOfCartItems().observe(this@MainActivity, Observer <Int> {

            val x = it
            if (x!=null){
                bottomNavigationView.getOrCreateBadge(R.id.miCart).apply {
                    isVisible = true
                    number = x

                }
            }
            else{
                bottomNavigationView.getOrCreateBadge(R.id.miCart).apply {
                    isVisible = false
                }

            }
        })


    }

    private fun setCurrentFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply{
            replace(R.id.flFragment, fragment)
            addToBackStack(null)
            commit()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
   super.onActivityResult(requestCode, resultCode, intent)
   if (requestCode == RaveConstants.RAVE_REQUEST_CODE && intent != null) {
       val message = intent.getStringExtra("response");
       if (resultCode == RavePayActivity.RESULT_SUCCESS) {
           Toast.makeText(this, "SUCCESS $message", Toast.LENGTH_LONG).show()
       } else if (resultCode == RavePayActivity.RESULT_ERROR) {
           Toast.makeText(this, "ERROR $message", Toast.LENGTH_LONG).show()
       } else if (resultCode == RavePayActivity.RESULT_CANCELLED) {
           Toast.makeText(this, "CANCELLED $message", Toast.LENGTH_LONG).show()
       }
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
