package com.app.fruit2you.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LifecycleCoroutineScope
import com.app.fruit2you.R
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_change_details.*
import java.util.*

class ChangeDetails : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var fstore: FirebaseFirestore
    private lateinit var emailText: String
    private lateinit var passWord: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_details)
        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()
        val phoneRegex:Regex ="^[0][7-9][0-1]([0-9]{8})$".toRegex()
        val nameRegex = "^[\\p{L} .'-]+$".toRegex()


        if(auth.currentUser!=null){
            val userId = auth.currentUser!!.uid
            val documentRef = fstore.collection("users").document(userId)
            documentRef.addSnapshotListener {snapshot, e->
                nameField.setText(snapshot?.getString("fName"))
                emailField.setText(snapshot?.getString("email"))
                phoneField.setText(snapshot?.getString("phone"))
                emailText = snapshot?.getString("email").toString().trim()
                passWord = snapshot?.getString("password").toString().trim()

            }
        }

        update.setOnClickListener {
            update.isEnabled = false
            val newName = nameField.text.toString().trim().toUpperCase(Locale.getDefault())
            val newPhone = phoneField.text.toString().trim()
            val newEmail = emailField.text.toString().trim()
            if (newName.isEmpty()||!newName.matches(nameRegex)){
                nameField.error = "Enter your full name"
                return@setOnClickListener
            }
            if (newPhone.isEmpty()||!newPhone.matches(phoneRegex)){
                phoneField.error = "Enter your correct phone number starting with 0"
                update.isEnabled = true
                return@setOnClickListener
            }

            if(newEmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()){
                emailField.error = "Enter your valid email address"
                update.isEnabled = true
                return@setOnClickListener
            }

            else{
                val  thisUser = auth.currentUser
                val userId = auth.currentUser!!.uid
                val credential: AuthCredential = EmailAuthProvider
                    .getCredential(emailText, passWord)

                thisUser?.reauthenticate(credential)?.addOnCompleteListener {task->

                    if (task.isSuccessful) {
                        thisUser.updateEmail(newEmail).addOnCompleteListener {task->
                            if (task.isSuccessful) {
                                val user = hashMapOf<String, Any>()
                                user["fName"] = newName
                                user["email"] = newEmail
                                user["phone"] = newPhone

                                val documentReference = fstore.collection("users").document(userId)
                                documentReference.set(user, SetOptions.merge())
                                Toast.makeText(this,"Details updated successfully",Toast.LENGTH_SHORT)
                                    .show()
                                update.isEnabled = true
                            } else {
                                Toast.makeText(this,"Error, details not updated, check details and try again",
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

            }
        }


    }
}
