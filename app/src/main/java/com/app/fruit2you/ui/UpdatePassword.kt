package com.app.fruit2you.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.app.fruit2you.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_change_details.*
import kotlinx.android.synthetic.main.update_password.*
import kotlinx.android.synthetic.main.update_password.update

class UpdatePassword : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var fstore: FirebaseFirestore
    private lateinit var x: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_password)
        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()

        if(auth.currentUser!=null){
            val userId = auth.currentUser!!.uid
            val documentRef = fstore.collection("users").document(userId)
            documentRef.addSnapshotListener {snapshot, e->
                val fstorePassword = snapshot?.getString("password").toString().trim()
                x = fstorePassword
            }


            update.setOnClickListener {
                val oldPassword = currentPassword.text.toString().trim()
                val updatedPassword = newPassword.text.toString().trim()

                if(oldPassword!=x){
                    currentPassword.error = "Old password incorrect"
                    return@setOnClickListener
                }
                if(updatedPassword.length<6){
                    newPassword.error = "Password must be at least 6 characters"
                    return@setOnClickListener
                }

                else{
                    val userID = auth.currentUser!!.uid
                    val documentRef = fstore.collection("users").document(userID)
                    documentRef.addSnapshotListener { snapshot, e ->
                        val email = snapshot?.getString("email")
                        val password = snapshot?.getString("password")

                        if (email != null && password != null) {
                            auth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        auth.currentUser!!.updatePassword(updatedPassword)
                                            .addOnCompleteListener { task ->
                                                if (task.isSuccessful) {
                                                    val user = hashMapOf<String, Any>()
                                                    user["password"] = updatedPassword

                                                    val documentReference =
                                                        fstore.collection("users").document(userID)
                                                    documentReference.set(user, SetOptions.merge())
                                                    Toast.makeText(applicationContext, "Password updated successfully",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                } else {
                                                    // password update failed
                                                    Toast.makeText(
                                                        applicationContext,
                                                        "Please check details and try again",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                    } else {
                                        Toast.makeText(applicationContext, "Authentication failed",
                                            Toast.LENGTH_LONG).show()
                                    }
                                }
                        }
                    }

                }
            }
        }

    }
}