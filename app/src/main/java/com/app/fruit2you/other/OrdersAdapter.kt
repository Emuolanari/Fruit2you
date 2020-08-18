package com.app.fruit2you.other


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.app.fruit2you.R
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot


open class OrdersAdapter(options: FirestoreRecyclerOptions<Order>, var olistener: OrdersListener) :
    FirestoreRecyclerAdapter<Order, OrdersAdapter.OrderHolder>(options) {


    @Override
    override fun onBindViewHolder(holder: OrderHolder, position: Int, model: Order) {
        holder.date.text = model.getDate()
        //java.lang.String.valueOf(model.getDate())
        holder.items.text = model.getItems()
        holder.reference.text = model.getRef()
        holder.delete.setOnClickListener {
            if (position!=RecyclerView.NO_POSITION){
                holder.deleteItem()
            }
        }
    }

    @Override
    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): OrderHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.orders, parent, false)
        return OrderHolder(v)
    }

    inner class OrderHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var date: TextView = itemView.findViewById(R.id.date)
        var items: TextView = itemView.findViewById(R.id.items)
        var reference: TextView = itemView.findViewById(R.id.reference)
        var delete: ImageView = itemView.findViewById(R.id.delete)

        fun deleteItem() {
            olistener.handleDeleteItem(snapshots.getSnapshot(adapterPosition))
        }

    }

    interface OrdersListener {
        fun handleDeleteItem(snapshot: DocumentSnapshot?)
    }

}