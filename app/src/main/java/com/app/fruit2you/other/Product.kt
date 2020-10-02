package com.app.fruit2you.other

open class Product {
    private  var amount: String? = null

    private  var description: String? = null

    private var imageURL: String? = null

    private  var name: String? = null


    constructor() {}

    constructor(imageURL: String?, description: String?, amount: String?, name:String?) {
        this.amount = amount
        this.description = description
        this.imageURL = imageURL
        this.name = name
    }

    fun getImageURL(): String? {
        return imageURL
    }

    fun getDescription(): String? {
        return description
    }

    fun getAmount(): String? {
        return amount
    }

    fun getName(): String?{
        return name
    }
}