package com.app.fruit2you

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment: Fragment(R.layout.home_fragment) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val auth = FirebaseAuth.getInstance()
        signout.setOnClickListener {
            auth.signOut()
            val intent = Intent(activity,LoginActivity::class.java)
            startActivity(intent)
        }
    }
}