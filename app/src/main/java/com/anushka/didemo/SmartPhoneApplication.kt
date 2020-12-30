package com.anushka.didemo

import android.app.Application
import com.anushka.didemo.model.MemoryCardModule

class SmartPhoneApplication: Application() {

    lateinit var smartPhoneComponent: SmartPhoneComponent

    override fun onCreate() {
        smartPhoneComponent = initDagger()
        super.onCreate()


    }

    private fun initDagger(): SmartPhoneComponent = DaggerSmartPhoneComponent.builder()
            .memoryCardModule(MemoryCardModule(1000))
            .build()


}