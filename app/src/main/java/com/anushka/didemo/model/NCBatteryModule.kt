package com.anushka.didemo.model

import dagger.Binds
import dagger.Module


// Defined Module for interface types.
@Module
abstract class NCBatteryModule {

    @Binds
    abstract fun bindsNCBattery(nickelCadmiumBattery: NickelCadmiumBattery): Battery

}