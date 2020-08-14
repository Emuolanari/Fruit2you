package com.app.fruit2you.ui.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.fruit2you.R
import com.app.fruit2you.data.database.entities.FruitItem
import com.app.fruit2you.ui.Fruit2YouViewModel
import com.app.fruit2you.ui.Fruit2YouViewModelFactory
import com.app.fruit2you.ui.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.home_fragment.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class HomeFragment: Fragment(R.layout.home_fragment), KodeinAware {
    override val kodein by kodein()
    private val factory: Fruit2YouViewModelFactory by instance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if(currentUser==null){
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }
        val viewModel = ViewModelProvider(this, factory).get(Fruit2YouViewModel::class.java)

        buyM.setOnClickListener {
            val mAlertDialog= AlertDialog.Builder(activity)
            val quantity = EditText(activity)
            quantity.inputType = InputType.TYPE_CLASS_NUMBER
            mAlertDialog.setTitle("Enter Quantity")
            mAlertDialog.setIcon(R.mipmap.ic_launcher)
            mAlertDialog.setView(quantity)
            mAlertDialog.setPositiveButton("ok"){ dialog: DialogInterface?, which: Int ->
                val qty: Int = quantity.text.toString().toInt()
                val amount:Int = qty * 500
                val fruit= FruitItem("mango pack",amount, qty)
                viewModel.upsert(fruit)
            }
            mAlertDialog.setNegativeButton("cancel"){ dialog: DialogInterface?, which: Int ->
                dialog?.dismiss()

            }
            mAlertDialog.create().show()

        }

        buyO.setOnClickListener {
            val oAlertDialog= AlertDialog.Builder(activity)
            val quantity = EditText(activity)
            quantity.inputType = InputType.TYPE_CLASS_NUMBER
            oAlertDialog.setTitle("Enter Quantity")
            oAlertDialog.setIcon(R.mipmap.ic_launcher)
            oAlertDialog.setView(quantity)
            oAlertDialog.setPositiveButton("ok"){ dialog: DialogInterface?, which: Int ->
                val qty: Int = quantity.text.toString().toInt()
                val amount:Int = qty * 500
                val fruit= FruitItem("orange pack",amount, qty)
                viewModel.upsert(fruit)
            }
            oAlertDialog.setNegativeButton("cancel"){ dialog: DialogInterface?, which: Int ->
                dialog?.dismiss()

            }
            oAlertDialog.create().show()
        }

        buyS.setOnClickListener {
            val sAlertDialog= AlertDialog.Builder(activity)
            val quantity = EditText(activity)
            quantity.inputType = InputType.TYPE_CLASS_NUMBER
            sAlertDialog.setTitle("Enter Quantity")
            //oAlertDialog.setMessage("Enter registered email to receive password reset link")
            sAlertDialog.setIcon(R.mipmap.ic_launcher)
            sAlertDialog.setView(quantity)
            sAlertDialog.setPositiveButton("ok"){ dialog: DialogInterface?, which: Int ->
                val qty: Int = quantity.text.toString().toInt()
                val amount:Int = qty * 500
                val fruit= FruitItem("strawberry pack",amount, qty)
                viewModel.upsert(fruit)
            }
            sAlertDialog.setNegativeButton("cancel"){ dialog: DialogInterface?, which: Int ->
                dialog?.dismiss()

            }
            sAlertDialog.create().show()

        }

        buyC.setOnClickListener {
            val cAlertDialog= AlertDialog.Builder(activity)
            val quantity = EditText(activity)
            quantity.inputType = InputType.TYPE_CLASS_NUMBER
            cAlertDialog.setTitle("Enter Quantity")
            cAlertDialog.setIcon(R.mipmap.ic_launcher)
            cAlertDialog.setView(quantity)
            cAlertDialog.setPositiveButton("ok"){ dialog: DialogInterface?, which: Int ->
                val qty: Int = quantity.text.toString().toInt()
                val amount:Int = qty * 500
                val fruit= FruitItem("coconut pack",amount, qty)
                viewModel.upsert(fruit)
            }
            cAlertDialog.setNegativeButton("cancel"){ dialog: DialogInterface?, which: Int ->
                dialog?.dismiss()

            }
            cAlertDialog.create().show()
        }

    }

}