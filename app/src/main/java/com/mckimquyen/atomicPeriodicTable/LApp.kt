package com.mckimquyen.atomicPeriodicTable

import android.app.Application
import android.widget.Toast

//TODO firebase
//TODO ad applovin
//TODO ic launcher
//TODO rate app
//TODO more app
//TODO share app
//TODO policy
//TODO manifest ad id
//TODO leak canary
//TODO keystore

//done
class LApp : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Toast.makeText(this, "$packageName onCreate", Toast.LENGTH_SHORT).show()
        }
    }
}
