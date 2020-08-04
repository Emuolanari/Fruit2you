package com.app.fruit2you

import android.app.Application
import com.app.fruit2you.data.database.Fruit2YouDatabase
import com.app.fruit2you.data.repositories.Fruit2YouRepository
import com.app.fruit2you.ui.Fruit2YouViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class Fruit2YouApplication: Application(), KodeinAware {
    override val kodein: Kodein = Kodein.lazy {
        import(androidXModule(this@Fruit2YouApplication))
        bind() from singleton { Fruit2YouDatabase(instance()) }
        bind() from singleton { Fruit2YouRepository(instance()) }
        bind() from provider {Fruit2YouViewModelFactory(instance())}
    }
}