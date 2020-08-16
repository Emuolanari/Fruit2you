package com.app.fruit2you.other

open class Order {
    private var date: String? = null

    private  var items: String? = null

    private  var ref: String? = null


    constructor() {}

    constructor(date: String?, items: String?, ref: String?) {
        this.date = date
        this.items = items
        this.ref = ref
    }

    fun getDate(): String? {
        return date
    }

    fun getItems(): String? {
        return items
    }

    fun getRef(): String? {
        return ref
    }
}