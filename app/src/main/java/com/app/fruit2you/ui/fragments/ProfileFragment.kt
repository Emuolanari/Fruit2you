package com.app.fruit2you.ui.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.app.fruit2you.R
import com.app.fruit2you.ui.ChangeDetails
import com.app.fruit2you.ui.LoginActivity
import com.app.fruit2you.ui.UpdatePassword
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.profile_fragment.*
import java.io.ByteArrayOutputStream


class ProfileFragment: Fragment(R.layout.profile_fragment) {
    private val imgRequestCode = 10001
    private val auth = FirebaseAuth.getInstance()
    private val user = FirebaseAuth.getInstance().currentUser
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val userId = auth.currentUser?.uid

        if (user!!.photoUrl != null) {
            Glide.with(this)
                .load(user.photoUrl)
                .into(profilePic)
        }
        profilePic.setOnClickListener {
            profilePic.setImageBitmap(null)
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent,"pick an image"), imgRequestCode)
        }

        signout.setOnClickListener {
            signout.isEnabled =false
            auth.signOut()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }

        changeDetails.setOnClickListener {
            changeDetails.isEnabled = false
            val intent = Intent(activity, ChangeDetails::class.java)
            startActivity(intent)
        }

        changePassword.setOnClickListener {
            changePassword.isEnabled = false
            val intent = Intent(activity, UpdatePassword::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode==imgRequestCode && resultCode==RESULT_OK && data != null){
            val imageData: Uri? = data.data
            profilePic.setImageURI(imageData)
            val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, imageData)
            if (bitmap != null) {
                handleUpload(bitmap)
            }
        }

    }

    private fun handleUpload(bitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val upid = FirebaseAuth.getInstance().currentUser!!.uid
        val reference = FirebaseStorage.getInstance().reference
            .child("profileImages")
            .child("$upid.jpeg")
        reference.putBytes(baos.toByteArray())
            .addOnSuccessListener { getDownloadUrl(reference) }
            .addOnFailureListener { e -> Log.e("TAG", "onFailure:", e.cause) }
    }

    private fun getDownloadUrl(reference: StorageReference) {
        reference.downloadUrl.addOnSuccessListener { uri ->
            Log.d("TAG", "onSuccess: $uri")
            setUserProfileUrl(uri)
        }
    }

    private fun setUserProfileUrl(uri: Uri) {
        val user = FirebaseAuth.getInstance().currentUser
        val request = UserProfileChangeRequest.Builder()
            .setPhotoUri(uri)
            .build()
        user!!.updateProfile(request)
            .addOnSuccessListener {
                Toast.makeText(activity, "Profile photo updated successfully", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(activity, "Image failed to load...", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onResume() {
        super.onResume()
        changePassword.isEnabled = true
        signout.isEnabled =true
        changeDetails.isEnabled = true
    }


}