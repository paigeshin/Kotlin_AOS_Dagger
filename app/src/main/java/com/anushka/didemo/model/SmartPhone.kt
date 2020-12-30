package com.anushka.didemo.model

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

/*
    SmartPhone has three main dependency. `Battery`, `SIMCard`, `MemoryCard`
*/

@Singleton
class SmartPhone @Inject constructor(val battery: Battery, val simCard: SIMCard, val memoryCard: MemoryCard) {

    init {
        battery.getPower()
        simCard.getConnection()
        memoryCard.getSpaceAvailablity()
        Log.i("MYTAG", "SmartPhone Constructed")
    }

    fun makeACallWithRecording() {
        Log.i("MYTAG", "Calling.....")
    }
}

