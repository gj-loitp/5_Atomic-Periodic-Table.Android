package com.mckimquyen.atomicPeriodicTable

import android.app.Application
import android.widget.Toast

//TODO firebase
//TODO ad applovin

//done
//rate app
//more app
//share app
//policy
//manifest ad id
//leak canary
//ic launcher
//splash screen xml
//keystore
//github 20 tester

//done
class RoyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Toast.makeText(this, "$packageName onCreate", Toast.LENGTH_SHORT).show()
        }
    }
}
