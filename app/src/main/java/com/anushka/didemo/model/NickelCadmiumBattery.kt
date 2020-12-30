package com.anushka.didemo.model

import android.util.Log
import javax.inject.Inject

// NickelCadmiumBattery is Battery because it implemented `interface` Battery
// However, Dagger does not know it.
// We need to do one additional task.
// Add Module.
class NickelCadmiumBattery @Inject constructor(): Battery {

    override fun getPower() {
        Log.i("MYTAG", " Power from NickelCadmiumBattery")
    }

}