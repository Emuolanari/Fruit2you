package com.app.fruit2you.ui.fragments


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.fruit2you.R
import com.app.fruit2you.other.Order
import com.app.fruit2you.other.OrdersAdapter
import com.app.fruit2you.ui.Fruit2YouViewModel
import com.app.fruit2you.ui.Fruit2YouViewModelFactory
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.order_fragment.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class OrderFragment: Fragment(R.layout.order_fragment), OrdersAdapter.OrdersListener, KodeinAware{
    override val kodein by kodein()
    private val factory: Fruit2YouViewModelFactory by instance()
    private lateinit var fstore:FirebaseFirestore
    private lateinit var adapter:OrdersAdapter
    private lateinit var auth:FirebaseAuth
    private lateinit var userId:String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fstore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        userId = auth.currentUser!!.uid
        val viewModel = ViewModelProvider(this, factory).get(Fruit2YouViewModel::class.java)
        val homeFragment = HomeFragment()
        val cartFragment = CartFragment()

        setUpRecyclerView()

        orderButton.setOnClickListener {
            viewModel.numberOfCartItems().observe(viewLifecycleOwner, Observer <Int> {
                val x = it
                if (x==null){
                    setCurrentFragment(homeFragment)
                    activity?.bottomNavigationView?.selectedItemId =  R.id.miHome
                }
                else{
                    setCurrentFragment(cartFragment)
                    activity?.bottomNavigationView?.selectedItemId =  R.id.miCart
                }
            })
        }
    }

    private fun setCurrentFragment(fragment: Fragment){
        parentFragmentManager.beginTransaction().apply{
            replace(R.id.flFragment, fragment)
            addToBackStack(null)
            commit()
        }
    }

    private fun setUpRecyclerView() {
        val orderRef: CollectionReference =
            fstore.collection("users").document(userId).collection("orders")
        val query =
            orderRef.orderBy("date", Query.Direction.DESCENDING)
        val options: FirestoreRecyclerOptions<Order> = FirestoreRecyclerOptions.Builder<Order>()
            .setQuery(query, Order::class.java)
            .build()
        adapter = OrdersAdapter(options, this)
        //orderRecyclerView.setHasFixedSize(true)
        orderRecyclerView.layoutManager = LinearLayoutManager(activity)
        orderRecyclerView.adapter = adapter
    }



    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun handleDeleteItem(snapshot: DocumentSnapshot?) {
        val documentReference = snapshot?.reference
        val order: Order? = snapshot?.toObject(Order::class.java)
        documentReference?.delete()
        Snackbar.make(orderRecyclerView, "order details deleted", Snackbar.LENGTH_LONG)
            .setAction("Undo") {
                if (order != null) {
                    documentReference?.set(order)
                }
            }
    }
}