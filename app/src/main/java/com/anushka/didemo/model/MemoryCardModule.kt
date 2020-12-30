package com.anushka.didemo.model

import android.util.Log
import dagger.Module
import dagger.Provides

//Add module to the class
@Module
class MemoryCardModule(val memorySize: Int) {

    //define Provider Function
    @Provides
    fun providesMemoryCard(): MemoryCard {
        Log.i("MYTAG", "Size of the memory is $memorySize")
        return MemoryCard()
    }

}