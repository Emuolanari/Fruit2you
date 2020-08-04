package com.app.fruit2you.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.app.fruit2you.R
import com.app.fruit2you.ui.FruitsActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment: Fragment(R.layout.home_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val auth = FirebaseAuth.getInstance()

        buyO.setOnClickListener {
            val intent = Intent(activity, FruitsActivity::class.java)
            intent.putExtra("orange", R.id.orangeImage)
            startActivity(intent);

        }
        buyM.setOnClickListener {
            val intent = Intent(activity, FruitsActivity::class.java)
            intent.putExtra("mango", R.id.mangoImage)
            startActivity(intent);

        }
        buyS.setOnClickListener {
            val intent = Intent(activity, FruitsActivity::class.java)
            intent.putExtra("strawberry", R.id.strawberryImage)
            startActivity(intent);

        }
        buyC.setOnClickListener {
            val intent = Intent(activity, FruitsActivity::class.java)
            intent.putExtra("coconut", R.id.coconutImage)
            startActivity(intent);
        }

    }
}