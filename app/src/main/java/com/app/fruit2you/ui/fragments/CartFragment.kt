package com.app.fruit2you.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.fruit2you.R
import com.app.fruit2you.data.database.entities.FruitItem
import com.app.fruit2you.ui.Fruit2YouViewModel
import com.app.fruit2you.ui.Fruit2YouViewModelFactory
import com.app.fruit2you.utilities.FruitsAdapter
import com.flutterwave.raveandroid.RavePayActivity
import com.flutterwave.raveandroid.RaveUiManager
import com.flutterwave.raveandroid.rave_java_commons.Meta
import com.flutterwave.raveandroid.rave_java_commons.RaveConstants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.cart_fragment.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.lang.System.currentTimeMillis


class CartFragment: Fragment(R.layout.cart_fragment), KodeinAware {
    override val kodein by kodein()
    private val factory: Fruit2YouViewModelFactory by instance()
    private lateinit var auth: FirebaseAuth
    private lateinit var fstore: FirebaseFirestore
    private var phone: String = ""
    private var email: String = ""
    private var firstName=""
    private var lastName=""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()
        val userID = auth.currentUser?.uid


        if (userID!=null){
            val documentReference: DocumentReference = fstore.collection("users").document(userID)
            documentReference.addSnapshotListener { documentSnapshot, e ->
                if (documentSnapshot != null) {
                    phone= documentSnapshot.getString("phone").toString()
                }
                if (documentSnapshot != null) {
                    val fName = documentSnapshot.getString("fName").toString()
                }
                if (documentSnapshot != null) {
                    email = documentSnapshot.getString("email").toString()
                }
            }
        }


        val publicKey = "FLWPUBK_TEST-54e64b93d20fc322f03ea1e51303634d-X"
        val encryptionKey = "FLWSECK_TESTd17a7a57aacf"
        val narration = "payment for fruit"
        var txRef: String
        //val country = "NG"
        val currency = "NGN"

        val homeFragment = HomeFragment()
        val viewModel = ViewModelProviders.of(this, factory).get(Fruit2YouViewModel::class.java)
        val adapter = FruitsAdapter(listOf(), viewModel)
        cartRecyclerView.layoutManager = LinearLayoutManager(activity)
        cartRecyclerView.adapter = adapter


        fun makePayment(a:Meta){
            viewModel.priceOfCartItems().observe(viewLifecycleOwner, Observer <Int> {
                val totalAmount = it
                txRef = currentTimeMillis().toString()+"_"+auth.currentUser?.uid.toString()


                RaveUiManager(this).setAmount(totalAmount.toDouble())
                    .setCurrency(currency)
                    .setPhoneNumber(phone, true)
                     .setEmail(email)
                    //.setfName(firstName)
                    //.setlName(lastName)
                    .acceptUssdPayments(true)
                    .setNarration(narration)
                    .setPublicKey(publicKey)
                    .setEncryptionKey(encryptionKey)
                    .setTxRef(txRef)
                    .acceptAccountPayments(true)
                    .acceptCardPayments(true)
                    .acceptMpesaPayments(false)
                    .acceptGHMobileMoneyPayments(false)
                    .onStagingEnv(false)
                    .setMeta(mutableListOf(a))
                    //.allowSaveCardFeature(true)
                    //.withTheme(R.style.DefaultPayTheme)
                    .initialize()
            })


        }

        fun setCurrentFragment(fragment: Fragment){
            parentFragmentManager.beginTransaction().apply{
                replace(R.id.flFragment, fragment)
                addToBackStack(null)
                commit()
            }
        }
        viewModel.numberOfCartItems().observe(viewLifecycleOwner, Observer <Int> {

            val x = it
            if (x==null){
                checkout.visibility =  View.GONE
                addItems.visibility = View.VISIBLE
                address.visibility = View.GONE
                addresstitle.visibility = View.GONE
            }
        })

        address.setOnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true)

            when (event.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_UP -> v.parent.requestDisallowInterceptTouchEvent(false)
            }
            false
        }



        addItems.setOnClickListener {
            setCurrentFragment(homeFragment)
            activity?.bottomNavigationView?.selectedItemId =  R.id.miHome
        }


        viewModel.getAllShoppingItems().observe(viewLifecycleOwner, Observer <List<FruitItem>> {
            adapter.items=it
            adapter.notifyDataSetChanged()
        })


        checkout.setOnClickListener {
            val deliveryAddress = address.text.toString().trim()
            checkout.isEnabled = false
            if(deliveryAddress.isNotEmpty()){
                val a = Meta("address",deliveryAddress)
                makePayment(a)
            }
            else{
                Toast.makeText(activity,"Please enter your delivery address",Toast.LENGTH_LONG).show()
                checkout.isEnabled = true
            }

        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (requestCode == RaveConstants.RAVE_REQUEST_CODE && intent != null) {
            val message = intent.getStringExtra("response")
            if (resultCode == RavePayActivity.RESULT_SUCCESS) {
               //Log.d("TAG", message)
                Toast.makeText(activity, "payment successful", Toast.LENGTH_LONG).show()
            } else if (resultCode == RavePayActivity.RESULT_ERROR) {
                Toast.makeText(activity, "ERROR $message", Toast.LENGTH_LONG).show()
            } else if (resultCode == RavePayActivity.RESULT_CANCELLED) {
                Toast.makeText(activity, "CANCELLED $message", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkout.isEnabled = true
    }
}