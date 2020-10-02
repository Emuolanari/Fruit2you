package com.app.fruit2you.other

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.app.fruit2you.R
import com.app.fruit2you.data.database.entities.FruitItem
import com.app.fruit2you.ui.Fruit2YouViewModel
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import java.net.URL
import java.security.AccessController.getContext

open class ProductsAdapter(options: FirestoreRecyclerOptions<Product>, val viewModel: Fruit2YouViewModel):
FirestoreRecyclerAdapter<Product, ProductsAdapter.ProductHolder>(options){

    @Override
    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ProductHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.products, parent, false)
               return ProductHolder(v)
    }

    @Override
    override fun onBindViewHolder(holder: ProductHolder, position: Int, model: Product) {
        holder.name.text = model.getName()
        holder.description.text = model.getDescription()
        holder.amount.text= model.getAmount()
        val url = URL(model.getImageURL())
        Glide.with(holder.imageURL.context)
            .load(url)
            .into(holder.imageURL)

        holder.buy.setOnClickListener {
            if (position!=RecyclerView.NO_POSITION){
                val fruit = FruitItem(holder.name.text.toString() ,holder.amount.text.toString().toInt(), 1)
                viewModel.upsert(fruit)
                Snackbar.make(holder.amount,"Item added to Cart",Snackbar.LENGTH_SHORT).show()

            }
            //Toast.makeText(holder.amount.context,holder.name.text.toString()+ " "+holder.amount.text.toString().toDouble(),Toast.LENGTH_LONG).show()
        }
    }

    inner class ProductHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.name)
        var description: TextView = itemView.findViewById(R.id.description)
        var amount: TextView = itemView.findViewById(R.id.amount)
        var imageURL: ImageView = itemView.findViewById(R.id.imageURL)
        var buy: Button = itemView.findViewById(R.id.buyItem)
    }

}