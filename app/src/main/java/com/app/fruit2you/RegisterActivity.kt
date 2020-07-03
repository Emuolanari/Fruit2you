package com.app.fruit2you

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*


class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setContentView(R.layout.activity_register)
        loginHere.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        button.setOnClickListener { formValidation() }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser!=null){
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

    }

    private fun formValidation(){
        val phoneRegex:Regex ="[0][7-9][0-1]([0-9]{8})".toRegex()
        val nameRegex = "^[\\p{L} .'-]+$".toRegex()
        val name=fullName.text.toString().trim().toUpperCase(Locale.getDefault())
        val mail = email.text.toString().trim()
        val phon = phone.text.toString().trim()
        val passwd = password.text.toString().trim()

        if(TextUtils.isEmpty(name)||!name.matches(nameRegex)){
            fullName.error = "Please enter your full name"
            return
        }


        if(TextUtils.isEmpty(mail)) {
            email.error = "Please enter a valid email address"
            return
        }

        if (TextUtils.isEmpty(phon)||!phon.matches(phoneRegex)) {
            phone.error = "Please enter your phone number"
            return
        }

        if(TextUtils.isEmpty(passwd)){
            password.error= "Please choose a password"
            return
        }

        if(passwd.length<6){
            password.error= "Please enter a password up to 6 characters"
            return
        }

        if(phon.length!=11) {
            phone.error = "Please enter valid phone number starting with 0 e.g 080..."
            return
        }


        else{
            auth.createUserWithEmailAndPassword(mail, passwd)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG", "createUserWithEmail:success")
                        Toast.makeText(this,"Sign up successful",Toast.LENGTH_SHORT).show()
                        val intent = Intent(this,LoginActivity::class.java)
                        startActivity(intent)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.d("TAG", "createUserWithEmail:failure"+ task.exception)
                        Toast.makeText(this, "failed "+task.exception,
                            Toast.LENGTH_LONG).show()
                    }

                    // ...
                }
        }


    }
}
