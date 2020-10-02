package com.app.fruit2you.other

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
        holder.itemView.tvName.text = curFruitItem.name
        holder.itemView.tvQuantity.text = "${curFruitItem.quantity}"
        holder.itemView.amount.text = "${curFruitItem.amount}"
        var fstore: FirebaseFirestore = FirebaseFirestore.getInstance()
        val docRef = fstore.collection("items").document(curFruitItem.name)
        docRef.addSnapshotListener{snapshot, e->
            val imageSrc = snapshot?.getString("imageURL")
            val url = URL(imageSrc)
            Glide.with(holder.itemView.context)
                .load(url)
                .into(myImage)
        }


        holder.itemView.ivDelete.setOnClickListener {
            viewModel.delete(curFruitItem)
        }

        holder.itemView.ivPlus.setOnClickListener {
            curFruitItem.quantity++
            curFruitItem.amount = curFruitItem.quantity * 500.00
            viewModel.upsert(curFruitItem)
        }

        holder.itemView.ivMinus.setOnClickListener {
            if(curFruitItem.quantity > 0) {
                curFruitItem.quantity--
                curFruitItem.amount = curFruitItem.quantity * 500.00
                viewModel.upsert(curFruitItem)
            }
        }
    }

    inner class FruitsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}