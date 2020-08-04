package com.app.fruit2you.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.app.fruit2you.R
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    val tag = "TAG"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        signin.setOnClickListener {formValidation() }
        signupnow.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser!=null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun formValidation(){
        val mail = email.text.toString().trim()
        val passwd = password.text.toString().trim()

        if(TextUtils.isEmpty(passwd))
            password.error= "Please enter your password"

        if(TextUtils.isEmpty(mail))
        email.error = "Please enter your email address"


        else{
            auth.signInWithEmailAndPassword(mail, passwd)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(baseContext, "login failed." + task.exception,
                            Toast.LENGTH_SHORT).show()
                    }

                }
        }


    }

    override fun onBackPressed() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}
