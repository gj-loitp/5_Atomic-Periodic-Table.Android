package com.roy.science

import android.app.Application
import android.widget.Toast

//TODO firebase
//TODO ad

//done
//ic launcher
//rate app
//more app
//share app
//policy
//manifest ad id
//leak canary
//keystore
class LApp : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Toast.makeText(this, "$packageName onCreate", Toast.LENGTH_SHORT).show()
        }
    }
}
