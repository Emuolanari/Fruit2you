package com.app.fruit2you.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.lifecycle.LifecycleCoroutineScope
import com.app.fruit2you.R
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


                update.setOnClickListener {
                    val newName = nameField.text.toString().trim().toUpperCase(Locale.getDefault())
                    val newPhone = phoneField.text.toString().trim()
                    val newEmail = emailField.text.toString().trim()
                    if (newName.isEmpty()||!newName.matches(nameRegex)){
                        nameField.error = "Please enter your full name"
                        return@setOnClickListener
                    }
                    if (newPhone.isEmpty()||!newPhone.matches(phoneRegex)){
                        phoneField.error = "Please enter your correct phone number"
                        return@setOnClickListener
                    }

                    if(newEmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()){
                        emailField.error = "Please enter your valid email address"
                        return@setOnClickListener
                    }

                    else{
                        val userId = auth.currentUser!!.uid
                        val documentRef = fstore.collection("users").document(userId)
                        documentRef.addSnapshotListener {snapshot, e->
                            val email = snapshot?.getString("email")
                            val password = snapshot?.getString("password")
                            if (email!=null && password!=null){
                                auth.signInWithEmailAndPassword(email,password)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            // Sign in success now update email
                                            auth.currentUser!!.updateEmail(newEmail)
                                                .addOnCompleteListener{ task ->
                                                    if (task.isSuccessful) {
                                                        val user = hashMapOf<String, Any>()
                                                        user["fName"] = newName
                                                        user["email"] = newEmail
                                                        user["phone"] = newPhone

                                                        val documentReference = fstore.collection("users").document(userId)
                                                        documentReference.set(user, SetOptions.merge())
                                                        Toast.makeText(this,"Details updated successfully", Toast.LENGTH_SHORT).show()
                                                        return@addOnCompleteListener
                                                    }else{
                                                        // email update failed
                                                        Toast.makeText(this,"Please check details and try again", Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                        } else {
                                            Toast.makeText(applicationContext,"Authentication failed", Toast.LENGTH_LONG).show()
                                        }
                                    }

                            }

                        }
                    }
                    return@setOnClickListener
                }
            }
        }

    }
}
