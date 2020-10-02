package com.app.fruit2you.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.fruit2you.data.FruitsDao
import com.app.fruit2you.data.database.entities.FruitItem


@Database(
    entities = [FruitItem::class],
    version = 5
)
abstract class Fruit2YouDatabase: RoomDatabase() {
    abstract fun getFruitsDao(): FruitsDao
    companion object{
        @Volatile
        private var instance: Fruit2YouDatabase? = null

        private val LOCK = Any()

        operator fun invoke(context: Context) = instance
            ?: synchronized(LOCK){
                instance
                    ?: createDatabase(
                        context
                    ).also { instance =it }
            }

        private fun createDatabase(context: Context)
                = Room.databaseBuilder(context.applicationContext, Fruit2YouDatabase::class.java, "fruit2youDB.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}