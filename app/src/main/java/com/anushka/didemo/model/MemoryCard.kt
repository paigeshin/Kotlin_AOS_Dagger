package com.anushka.didemo.model

import android.util.Log

// Assumes this memory card can't take any argument
class MemoryCard {
    init {
        Log.i("MYTAG","Memory Card Constructed")
    }

    fun getSpaceAvailablity(){
        Log.i("MYTAG","Memory space available")
    }
}