package com.app.fruit2you.ui.fragments

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.fruit2you.R
import com.app.fruit2you.data.database.entities.FruitItem
import com.app.fruit2you.other.Product
import com.app.fruit2you.other.ProductsAdapter
import com.app.fruit2you.ui.Fruit2YouViewModel
import com.app.fruit2you.ui.Fruit2YouViewModelFactory
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.home_fragment.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class HomeFragment: Fragment(R.layout.home_fragment), KodeinAware {
    private lateinit var adapter:ProductsAdapter
    private lateinit var fstore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    override val kodein by kodein()
    private val factory: Fruit2YouViewModelFactory by instance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fstore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        setUpRecyclerView()

    }

    private fun setUpRecyclerView() {
        val productRef: CollectionReference =
            fstore.collection("items")
        val query =
            productRef.orderBy("name", Query.Direction.DESCENDING)
        val options: FirestoreRecyclerOptions<Product> = FirestoreRecyclerOptions.Builder<Product>()
            .setQuery(query, Product::class.java)
            .build()
        val viewModel = ViewModelProvider(this, factory).get(Fruit2YouViewModel::class.java)
        adapter = ProductsAdapter(options, viewModel)
        rvProducts.layoutManager = LinearLayoutManager(activity)
        rvProducts.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

}