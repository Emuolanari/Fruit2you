package com.app.fruit2you.data.database.entities
import androidx.lifecycle.LiveData
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Shopping_Items")
data class FruitItem(
    @PrimaryKey
    var name: String,
    var amount: Int,
    var priceOfOne: Int,
    var quantity: Int) {

}