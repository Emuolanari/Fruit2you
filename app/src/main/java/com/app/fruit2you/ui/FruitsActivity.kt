package com.app.fruit2you.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.app.fruit2you.R


class FruitsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fruits)

        val orangeData = intent.getStringExtra("orange");
        val mangoData = intent.getStringExtra("mango");
        val coconutData = intent.getStringExtra("coconut");
        val strawberryData = intent.getStringExtra("strawberry");

        val image = R.id.fruitImage




    }
}
