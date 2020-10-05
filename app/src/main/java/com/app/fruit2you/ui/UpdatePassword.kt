package com.app.fruit2you.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.app.fruit2you.R
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.update_password.*
import kotlinx.android.synthetic.main.update_password.update

class UpdatePassword : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var fstore: FirebaseFirestore
    private lateinit var emailText: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_password)
        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()

        if(auth.currentUser!=null){
            val userId = auth.currentUser!!.uid
            val documentRef = fstore.collection("users").document(userId)
            documentRef.addSnapshotListener {snapshot, e->
                val email = snapshot?.getString("email").toString().trim()
                emailText = email
            }


            update.setOnClickListener {
                update.isEnabled = false
                val oldPassword = currentPassword.text.toString().trim()
                val updatedPassword = newPassword.text.toString().trim()

                if (oldPassword.isEmpty()){
                    currentPassword.error = "Please enter your current password"
                }

                if(updatedPassword.length<6){
                    newPassword.error = "Password must be at least 6 characters"
                    update.isEnabled = true
                    return@setOnClickListener
                }

                else{
                    val  thisUser = auth.currentUser
                    val credential: AuthCredential = EmailAuthProvider
                            .getCredential(emailText, oldPassword)

                    // Prompt the user to re-provide their sign-in credentials
                    thisUser?.reauthenticate(credential)?.addOnCompleteListener {task->

                        if (task.isSuccessful) {
                            thisUser.updatePassword(updatedPassword).addOnCompleteListener {task->
                                    if (task.isSuccessful) {
                                        Toast.makeText(this,"Password updated successfully",Toast.LENGTH_SHORT)
                                            .show()
                                        update.isEnabled = true
                                    } else {
                                        Toast.makeText(this,"Password not updated, check details and try again",
                                            Toast.LENGTH_SHORT)
                                            .show()
                                        update.isEnabled = true
                                    }

                            }
                        } else {
                            Toast.makeText(this,"Authentication failed",
                                Toast.LENGTH_SHORT)
                                .show()
                            update.isEnabled = true
                        }

                    }
                    update.isEnabled = true
                }
            }
        }

    }
}