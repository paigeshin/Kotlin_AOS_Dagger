package com.anushka.didemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.anushka.didemo.model.MemoryCardModule
import com.anushka.didemo.model.SmartPhone
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject lateinit var smartPhone: SmartPhone

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as SmartPhoneApplication)
            .smartPhoneComponent
            .inject(this)


//        DaggerSmartPhoneComponent
//            .create()
//            .getSmartPhone()
//            .makeACallWithRecording()

//        DaggerSmartPhoneComponent.create().inject(this)
//        smartPhone.makeACallWithRecording()


        //When some constructors has arguments...
//        DaggerSmartPhoneComponent.builder()
//            .memoryCardModule(MemoryCardModule(1000))
//            .build()
//            .inject(this)


    }



}
