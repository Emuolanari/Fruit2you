package com.app.fruit2you.other

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.fruit2you.R
import com.app.fruit2you.data.database.entities.FruitItem
import com.app.fruit2you.ui.Fruit2YouViewModel
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fruit_item.view.*
import kotlinx.android.synthetic.main.fruit_item.view.tvQuantity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

class FruitsAdapter(
    var items: List<FruitItem>,
    private val viewModel: Fruit2YouViewModel
): RecyclerView.Adapter<FruitsAdapter.FruitsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FruitsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fruit_item, parent, false)
        return FruitsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: FruitsViewHolder, position: Int) {
        val curFruitItem = items[position]
        val  myImage = holder.itemView.fruitImage
        val fstore: FirebaseFirestore = FirebaseFirestore.getInstance()
        val docRef = fstore.collection("items").document(curFruitItem.name)
        docRef.addSnapshotListener{snapshot, e->
            try{
                val url = URL(snapshot?.getString("imageURL"))
                Glide.with(myImage.context)
                    .load(url)
                    .into(myImage)
            }
            catch (e:MalformedURLException){

            }

        }
        holder.itemView.tvName.text = curFruitItem.name
        holder.itemView.tvQuantity.text = "${curFruitItem.quantity}"
        holder.itemView.amount.text = "${curFruitItem.amount}"




        holder.itemView.ivDelete.setOnClickListener {
            viewModel.delete(curFruitItem)
        }

        holder.itemView.ivPlus.setOnClickListener {
            curFruitItem.quantity++
            curFruitItem.amount = curFruitItem.quantity * curFruitItem.priceOfOne
            viewModel.upsert(curFruitItem)
        }

        holder.itemView.ivMinus.setOnClickListener {
            if(curFruitItem.quantity > 1) {
                curFruitItem.quantity--
                curFruitItem.amount = curFruitItem.quantity * curFruitItem.priceOfOne
                viewModel.upsert(curFruitItem)
            }
        }
    }

    inner class FruitsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}