package com.app.fruit2you.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app.fruit2you.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_change_details.*

class ChangeDetails : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var fstore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_details)
        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()


        if(auth.currentUser!=null){
            val userId = auth.currentUser!!.uid
            val documentRef = fstore.collection("users").document(userId)
            documentRef.addSnapshotListener {snapshot, e->
                nameField.setText(snapshot?.getString("fName"))
                emailField.setText(snapshot?.getString("email"))
                phoneField.setText(snapshot?.getString("phone"))
            }
        }

    }
}
