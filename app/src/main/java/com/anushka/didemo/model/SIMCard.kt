package com.anushka.didemo.model

import android.util.Log
import javax.inject.Inject

/*
    SIMcard has one dependency. `ServiceProvider`
*/
class SIMCard @Inject constructor(var serviceProvider: ServiceProvider) {

    init {
        Log.i("MYTAG","SIM Card Constructed")
    }

    fun getConnection(){
        serviceProvider.getServiceProvider()
    }

}
