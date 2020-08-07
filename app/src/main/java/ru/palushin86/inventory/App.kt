package ru.palushin86.inventory

import ru.palushin86.inventory.db.AppDatabase

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        database = AppDatabase
            .getInstance(this)
    }

    companion object {
        lateinit var database: AppDatabase
    }
}