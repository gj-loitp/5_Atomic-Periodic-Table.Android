package com.mckimquyen.atomicPeriodicTable

import android.app.Application
import android.widget.Toast
import com.mckimquyen.atomicPeriodicTable.util.setupApplovinAd

//TODO firebase

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
//ad applovin

//done
class RoyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        this.setupApplovinAd()
        if (BuildConfig.DEBUG) {
            Toast.makeText(this, "$packageName onCreate", Toast.LENGTH_SHORT).show()
        }
    }
}
