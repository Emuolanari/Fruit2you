package com.app.fruit2you.ui.fragments


import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.fruit2you.R
import com.app.fruit2you.data.database.entities.FruitItem
import com.app.fruit2you.ui.Fruit2YouViewModel
import com.app.fruit2you.ui.Fruit2YouViewModelFactory
import com.app.fruit2you.ui.LoginActivity
import com.app.fruit2you.other.FruitsAdapter
import com.flutterwave.raveandroid.RavePayActivity
import com.flutterwave.raveandroid.RaveUiManager
import com.flutterwave.raveandroid.rave_java_commons.Meta
import com.flutterwave.raveandroid.rave_java_commons.RaveConstants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.cart_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.lang.System.currentTimeMillis
import java.text.SimpleDateFormat
import java.util.*


class CartFragment: Fragment(R.layout.cart_fragment), KodeinAware {
    override val kodein by kodein()
    private val factory: Fruit2YouViewModelFactory by instance()
    private lateinit var auth: FirebaseAuth
    private lateinit var fstore: FirebaseFirestore
    private var phone: String = ""
    private var email: String = ""
    private var fName=""
    private var lName=""
    private lateinit var itemsString: String
    private lateinit var newString: String
    private lateinit var txRef: String
    private val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss a", Locale.UK)
    private val currentDate = sdf.format(Date())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        //val source = UUID.randomUUID().toString()
        //val currentUser = auth.currentUser
        /*if(currentUser==null){
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }*/
        fstore = FirebaseFirestore.getInstance()
        val userID = auth.currentUser?.uid


        if (userID!=null){
            val documentReference: DocumentReference = fstore.collection("users").document(userID)
            documentReference.addSnapshotListener { documentSnapshot, e ->
                if (documentSnapshot != null) {
                    phone= documentSnapshot.getString("phone").toString()
                    val fullName = documentSnapshot.getString("fName").toString()
                    val nameArray = fullName.split(" ")
                    if (nameArray.size>1){
                        fName = nameArray[0]
                        lName = nameArray[1]
                    }
                    else{
                        fName = nameArray[0]
                    }

                    email = documentSnapshot.getString("email").toString()
                    phone = documentSnapshot.getString("phone").toString()
                }
            }
        }


        val publicKey = "FLWPUBK_TEST-54e64b93d20fc322f03ea1e51303634d-X"
        val encryptionKey = "FLWSECK_TESTd17a7a57aacf"
        val narration = "payment for fruits"
        //val country = "NG" deprecated
        val currency = "NGN"

        val homeFragment = HomeFragment()
        val viewModel = ViewModelProvider(this, factory).get(Fruit2YouViewModel::class.java)
        val adapter = FruitsAdapter(listOf(), viewModel)
        cartRecyclerView.layoutManager = LinearLayoutManager(activity)
        cartRecyclerView.adapter = adapter

        fun makePayment(a:Meta,b:Meta){
            viewModel.priceOfCartItems().observe(viewLifecycleOwner, Observer <Int> {
                val totalAmount = it
                //txRef = currentTimeMillis().toString()+ "_" +auth.currentUser?.uid.toString()
                val prepareTxRef = UUID.randomUUID().toString()
                txRef = prepareTxRef.take(11)

                
                RaveUiManager(this).setAmount(totalAmount.toDouble())
                    .setCurrency(currency)
                    .setPhoneNumber(phone, true)
                    .setEmail(email)
                    .setfName(fName)
                    .setlName(lName)
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
                    .setMeta(mutableListOf(a,b))
                    .allowSaveCardFeature(true)
                    .withTheme(R.style.DefaultTheme)
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

        viewModel.getAllShoppingItems().observe(viewLifecycleOwner, Observer <List<FruitItem>> {
            val items = it
            val separator = ","
            val numberRegex:Regex ="priceOfOne=[0-9]+([,])".toRegex()


            itemsString = items.joinToString(separator)
            newString = itemsString.replace("FruitItem(name=", "\n")
            newString = newString.replace("amount=", "amount:₦")
            newString= newString.replace("quantity=", "quantity:")
            newString= newString.replace(")", "")
            //newString= newString.replace("priceOfOne=", "")
            newString = numberRegex.replace(newString,"")
            newString = newString.trim()
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
                val b = Meta("items",itemsString)
                makePayment(a,b)
            }
            else{
                Toast.makeText(activity,"Please enter your delivery address",Toast.LENGTH_SHORT).show()
                checkout.isEnabled = true
            }

        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        val orderFragment = OrderFragment()
        val cartFragment = CartFragment()
        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        fun setCurrentFragment(fragment: Fragment){
            parentFragmentManager.beginTransaction().apply{
                replace(R.id.flFragment, fragment)
                addToBackStack(null)
                commit()
            }
        }
        if (requestCode == RaveConstants.RAVE_REQUEST_CODE && intent != null) {
            val viewModel = ViewModelProvider(this, factory).get(Fruit2YouViewModel::class.java)

            val message = intent.getStringExtra("response")
            if (resultCode == RavePayActivity.RESULT_SUCCESS) {
                Toast.makeText(activity, "payment successful", Toast.LENGTH_SHORT).show()
                if (uid!=null){
                    GlobalScope.launch(Dispatchers.IO){
                        val deliveryAddress = address.text.toString().trim()
                        val collectionRef =
                            fstore.collection("users").document(uid).collection("orders")
                        val order = hashMapOf<String, Any>()
                        order["ref"] = txRef
                        order["items"] = newString
                        order["date"] = currentDate
                        order["address"] = deliveryAddress

                        collectionRef.add(order)
                        viewModel.nukeTable()
                    }
                    setCurrentFragment(orderFragment)
                    activity?.bottomNavigationView?.selectedItemId =  R.id.miOrder
                }

            } else if (resultCode == RavePayActivity.RESULT_ERROR) {
                Toast.makeText(activity, "ERROR $message", Toast.LENGTH_SHORT).show()
                setCurrentFragment(cartFragment)
                activity?.bottomNavigationView?.selectedItemId =  R.id.miCart
            } else if (resultCode == RavePayActivity.RESULT_CANCELLED) {
                Toast.makeText(activity, "CANCELLED $message", Toast.LENGTH_SHORT).show()
                setCurrentFragment(cartFragment)
                activity?.bottomNavigationView?.selectedItemId =  R.id.miCart
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, intent)
        }

    }


    override fun onResume() {
        super.onResume()
        checkout.isEnabled = true
    }


}