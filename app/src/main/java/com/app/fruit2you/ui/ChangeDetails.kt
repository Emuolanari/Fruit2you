package com.app.fruit2you.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.app.fruit2you.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_change_details.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class ChangeDetails : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var fstore: FirebaseFirestore

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
            }

            update.setOnClickListener {
                val newName = nameField.text.toString().trim().toUpperCase(Locale.getDefault())
                val newPhone = phoneField.text.toString().trim()
                val newEmail = emailField.text.toString().trim()
                if (newName.isEmpty()||!newName.matches(nameRegex)){
                    nameField.error = "Enter your full name"
                    return@setOnClickListener
                }
                if (newPhone.isEmpty()||!newPhone.matches(phoneRegex)){
                    phoneField.error = "Please enter your correct phone number"
                    return@setOnClickListener
                }

                if(newEmail.isEmpty()){
                    emailField.error = "Email field can't be left empty"
                    return@setOnClickListener
                }

                else{
                    GlobalScope.launch {
                        val userID = auth.currentUser!!.uid
                        val documentReference = fstore.collection("users").document(userID)
                        val user = hashMapOf<String, Any>()
                        user["fName"] = newName
                        user["email"] = newEmail
                        user["phone"] = newPhone

                        documentReference.set(user)
                    }

                }
            }
        }

    }
}
