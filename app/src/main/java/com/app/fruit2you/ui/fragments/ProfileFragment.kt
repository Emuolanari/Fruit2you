package com.app.fruit2you.ui.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.app.fruit2you.R
import com.app.fruit2you.ui.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.profile_fragment.*

class ProfileFragment: Fragment(R.layout.profile_fragment) {
    private val imgRequestCode = 123;
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val auth = FirebaseAuth.getInstance()
        profilePic.setOnClickListener {
            val intent = Intent();
            intent.type = "image/*";
            intent.action = Intent.ACTION_GET_CONTENT;
            startActivityForResult(Intent.createChooser(intent,"pick an image"), imgRequestCode)
        }

        signout.setOnClickListener {
            auth.signOut()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode==imgRequestCode && resultCode==RESULT_OK && data != null){
            val imageData: Uri? = data.data;
            profilePic.setImageURI(imageData);
        }
    }
}