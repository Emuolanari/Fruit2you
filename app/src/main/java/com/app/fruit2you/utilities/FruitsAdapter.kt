package com.app.fruit2you.utilities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.fruit2you.R
import com.app.fruit2you.data.database.entities.FruitItem
import com.app.fruit2you.ui.Fruit2YouViewModel
import kotlinx.android.synthetic.main.fruit_item.view.*
import kotlinx.android.synthetic.main.fruit_item.view.tvQuantity

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

        if(curFruitItem.name=="strawberry pack"){
            myImage.setImageResource(R.drawable.strawberry)
        }
        else if(curFruitItem.name=="coconut pack"){
            myImage.setImageResource(R.drawable.coconut)
        }
        else if(curFruitItem.name=="mango pack"){
            myImage.setImageResource(R.drawable.mangoes)
        }
        else{
            myImage.setImageResource(R.drawable.oranges)
        }

        holder.itemView.ivDelete.setOnClickListener {
            viewModel.delete(curFruitItem)
        }

        holder.itemView.ivPlus.setOnClickListener {
            curFruitItem.quantity++
            curFruitItem.amount = curFruitItem.quantity * 500
            viewModel.upsert(curFruitItem)
        }

        holder.itemView.ivMinus.setOnClickListener {
            if(curFruitItem.quantity > 0) {
                curFruitItem.quantity--
                curFruitItem.amount = curFruitItem.quantity * 500
                viewModel.upsert(curFruitItem)
            }
        }
    }

    inner class FruitsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}