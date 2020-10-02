package com.app.fruit2you.data.database.entities
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Shopping_Items")
data class FruitItem(
    @PrimaryKey
    var name: String,
    var amount: Double,
    var quantity: Int) {

}