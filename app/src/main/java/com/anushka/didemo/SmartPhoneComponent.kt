package com.anushka.didemo

import com.anushka.didemo.model.MemoryCardModule
import com.anushka.didemo.model.NCBatteryModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [MemoryCardModule::class, NCBatteryModule::class])
interface SmartPhoneComponent {

//    fun getSmartPhone(): SmartPhone

    // inject dependencies to `MainActivity`
    fun inject(mainActivity: MainActivity)

}