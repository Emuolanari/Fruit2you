package com.app.fruit2you.ui

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.app.fruit2you.R
import com.google.firebase.auth.FirebaseAuth
import com.scottyab.aescrypt.AESCrypt
import kotlinx.android.synthetic.main.activity_login.*

@Suppress("NAME_SHADOWING")
class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    val tag = "TAG"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        val mAlertDialog = findViewById<TextView>(R.id.forgot)
        mAlertDialog.setOnClickListener {
            val mAlertDialog = AlertDialog.Builder(this@LoginActivity)
            val resetMail = EditText(this)
            mAlertDialog.setTitle("Reset Password")
            mAlertDialog.setMessage("Enter registered email to receive password reset link")
            mAlertDialog.setIcon(R.mipmap.ic_launcher)
            mAlertDialog.setView(resetMail)
            mAlertDialog.setPositiveButton("send"){dialog: DialogInterface?, which: Int ->
                val mail = resetMail.text.toString()
                auth.sendPasswordResetEmail(mail).addOnSuccessListener {
                    Toast.makeText(this@LoginActivity,"Reset link sent to email",Toast.LENGTH_LONG).show()
                }.addOnFailureListener() {
                    Toast.makeText(this@LoginActivity,"Failed. "+ it.message,Toast.LENGTH_LONG).show()
                }

            }
            mAlertDialog.setNegativeButton("cancel"){ dialog: DialogInterface?, which: Int ->
                dialog?.dismiss()

            }
            mAlertDialog.create().show()
        }
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
        val passwd = passwordField.text.toString().trim()
        val encryptedPasswd = AESCrypt.encrypt(passwd,passwd)

        if(TextUtils.isEmpty(passwd))
            passwordField.error= "Please enter your password"

        if(TextUtils.isEmpty(mail))
        email.error = "Please enter your email address"


        else{
            auth.signInWithEmailAndPassword(mail, encryptedPasswd)
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
