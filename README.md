# Kotlin_AOS_Dagger

# Dependency

```kotlin
//Dagger, you must apply `kotlin-kapt` plugin
implementation 'com.google.dagger:dagger:2.27'
kapt 'com.google.dagger:dagger-compiler:2.27'
```

# Dependency Inversion Principle

- Entities(Objects/Instances) must depend on abstractions not on concretions

# Dependency Injection

- The process of constructing required dependencies(objects/entities/instances) outside of the dependent object and providing them to the dependent object when needed.
- In software engineering, dependency injection is a technique in which an object receives other objects that it depends on. These other objects are called dependencies. In the typical "using" relationship the receiving object is called a client and the passed (that is, "injected") object is called a service.

# Example

![https://s3-us-west-2.amazonaws.com/secure.notion-static.com/b4fe3250-dc9c-4d08-9909-54f819e2cd51/Untitled.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/b4fe3250-dc9c-4d08-9909-54f819e2cd51/Untitled.png)

```kotlin
/*
    SmartPhone has three main dependency. `Battery`, `SIMCard`, `MemoryCard`
*/
class SmartPhone(val battery: Battery, val simCard: SIMCard, val memoryCard: MemoryCard) {

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
```

```kotlin
/*
    SIMcard has one dependency. `ServiceProvider`
*/
class SIMCard(private  val serviceProvider: ServiceProvider) {

    init {
        Log.i("MYTAG","SIM Card Constructed")
    }

    fun getConnection(){
        serviceProvider.getServiceProvider()
    }
}
```

# Constructor Injection

```kotlin
val battery = Battery()
val memoryCard = MemoryCard()
val serviceProvider = ServiceProvider()
val simCard = SIMCard(serviceProvider)
val smartPhone = SmartPhone(battery, simCard, memoryCard)
smartPhone.makeACallWithRecording()

val smartPhone = SmartPhone(
    Battery(),
    SIMCard(ServiceProvider()),
    MemoryCard()
)
    .makeACallWithRecording()
```

# Method Injection

```kotlin
class SIMCard() {

    private lateinit var serviceProvider: ServiceProvider

    init {
        Log.i("MYTAG","SIM Card Constructed")
    }

    /*** Method Injection, without argument ***/
    fun setServiceProvider(serviceProvider: ServiceProvider) {
        this.serviceProvider = serviceProvider
    }

    fun getConnection(){
        serviceProvider.getServiceProvider()
    }
}
```

# Field Injection

```kotlin
/*
    SIMcard has one dependency. `ServiceProvider`
*/
class SIMCard() {

    //Field Injection
    public lateinit var serviceProvider: ServiceProvider

    init {
        Log.i("MYTAG","SIM Card Constructed")
    }

    fun getConnection(){
        serviceProvider.getServiceProvider()
    }
}
```

# Constructor Inject Annotation

### Constructor Injection

```kotlin
class ServiceProvider @Inject constructor() {
    init {
        Log.i("MYTAG","Service Provider Constructed")
    }

    fun getServiceProvider(){
        Log.i("MYTAG","Service provider connected")
    }
}
```

### Create `Component`

```kotlin
@Component
interface SmartPhoneComponent {

    fun getSmartPhone(): SmartPhone

}
```

### With auto-generated Factory classes, you can use access directly to Object

![https://s3-us-west-2.amazonaws.com/secure.notion-static.com/c660fea1-15e2-48d7-93dc-438e9ad72a02/Untitled.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/c660fea1-15e2-48d7-93dc-438e9ad72a02/Untitled.png)

```kotlin
DaggerSmartPhoneComponent
            .create()
            .getSmartPhone()
            .makeACallWithRecording()
```

# Dagger 2 Modules

 ℹ️ Why the hell do we use modules?

⇒ 

When we are using the classes we don’t own, classes form third party libraries, we cannot open the class and add @Inject annotation to the constructor.

Also , there are other cases like Context object, in which case we cannot instantiate the dependency.

So, for that type of situations, We have to use modules and write provider functions to provide those dependencies.

now, Just for the demonstration of the concept, let’s assume that we don’t own this MemoryCard class.

Therefore , since we don’t own it we should not be able to add this @Inject annotation here.

Let’s remove it. For now, just think about this memory card class, as it comes from a third party library.

- Exterior Library

```kotlin
// Assumes this memory card can't take any argument
class MemoryCard {
    init {
        Log.i("MYTAG","Memory Card Constructed")
    }

    fun getSpaceAvailablity(){
        Log.i("MYTAG","Memory space available")
    }
}
```

- MemoryCardModule.kt

```kotlin
//Add module to the class
@Module
class MemoryCardModule {

    //define Provider Function
    @Provides
    fun providesMemoryCard(): MemoryCard {
        return MemoryCard()
    }

}
```

- MemoryCard.kt

```kotlin
// Assumes this memory card can't take any argument
class MemoryCard {
    init {
        Log.i("MYTAG","Memory Card Constructed")
    }

    fun getSpaceAvailablity(){
        Log.i("MYTAG","Memory space available")
    }
}
```

# Working With Interfaces

```kotlin
interface Battery {

    fun getPower()

}

// NickelCadmiumBattery is Battery because it implemented `interface` Battery
// However, Dagger does not know it.
// We need to do one additional task.
// Add Module.
class NickelCadmiumBattery @Inject constructor(): Battery {

    override fun getPower() {
        Log.i("MYTAG", " Power from NickelCadmiumBattery")
    }

}

// Defined Module for interface types.
@Module
abstract class NCBatteryModule {

    @Binds
    abstract fun bindsNCBattery(nickelCadmiumBattery: NickelCadmiumBattery): Battery

}

@Component(modules = [MemoryCardModule::class, NCBatteryModule::class])
interface SmartPhoneComponent {

    fun getSmartPhone(): SmartPhone

}

```

# Field Injection With Dagger2

```kotlin
@Component(modules = [MemoryCardModule::class, NCBatteryModule::class])
interface SmartPhoneComponent {

//    fun getSmartPhone(): SmartPhone

    // inject dependencies to `MainActivity`
    fun inject(mainActivity: MainActivity)

}

class MainActivity : AppCompatActivity() {

    @Inject lateinit var smartPhone: SmartPhone

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        DaggerSmartPhoneComponent
//            .create()
//            .getSmartPhone()
//            .makeACallWithRecording()

        DaggerSmartPhoneComponent.create().inject(this)
        smartPhone.makeACallWithRecording()

    }

}
```

# When there are arguments

```kotlin
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

class MainActivity : AppCompatActivity() {

    @Inject lateinit var smartPhone: SmartPhone

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        DaggerSmartPhoneComponent
//            .create()
//            .getSmartPhone()
//            .makeACallWithRecording()

//        DaggerSmartPhoneComponent.create().inject(this)
//        smartPhone.makeACallWithRecording()

        //When some constructors has arguments...
        DaggerSmartPhoneComponent.builder()
            .memoryCardModule(MemoryCardModule(1000))
            .build()
            .inject(this)

    }

}
```

# The Application Class

ℹ️ When do we create a subclass of the application class?

⇒ If there are tasks that need to run before the creation of first activity.

⇒ If there are immutable data or global objects that needs to be shared across all components.

### SmartPhoneApplication.kt which subclasses Application

```kotlin
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
```

### MainActivity

```kotlin
class MainActivity : AppCompatActivity() {

    @Inject lateinit var smartPhone: SmartPhone
		
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
		
				//Simplified
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
```

### Manifest

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.anushka.didemo">

    <application
        android:name=".SmartPhoneApplication"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
        <activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```

⇒ give application name with the custom created class.

# Singletone

### Object with Dependencies

```kotlin
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
```

### Component

```kotlin
@Singleton
@Component(modules = [MemoryCardModule::class, NCBatteryModule::class])
interface SmartPhoneComponent {

//    fun getSmartPhone(): SmartPhone

    // inject dependencies to `MainActivity`
    fun inject(mainActivity: MainActivity)

}
```

# Entire Code line by line

- ServiceProvider.kt

```kotlin
class ServiceProvider @Inject constructor() {
    init {
        Log.i("MYTAG","Service Provider Constructed")
    }

    fun getServiceProvider(){
        Log.i("MYTAG","Service provider connected")
    }
}
```

- SIMcard.kt

⇒ it has one dependency

```kotlin
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
```

- MemoryCard.kt

⇒ Simulate exterior libraries where we can't add `@Inject` annotation

```kotlin
// Assumes this memory card can't take any argument
class MemoryCard {
    init {
        Log.i("MYTAG","Memory Card Constructed")
    }

    fun getSpaceAvailablity(){
        Log.i("MYTAG","Memory space available")
    }
}
```

- MemoryCardModule.kt

```kotlin
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
```

- Batter.kt

⇒ Interface

```kotlin
interface Battery {

    fun getPower()

}
```

- NickelCadmiumBattery.kt which implements Battery

```kotlin
// NickelCadmiumBattery is Battery because it implemented `interface` Battery
// However, Dagger does not know it.
// We need to do one additional task.
// Add Module.
class NickelCadmiumBattery @Inject constructor(): Battery {

    override fun getPower() {
        Log.i("MYTAG", " Power from NickelCadmiumBattery")
    }

}
```

- NCBatteryModule.kt

⇒ Dagger2 does not know that `NickelCadmiumBattery.kt` is Battery

⇒ Create module for that

```kotlin
// Defined Module for interface types.
@Module
abstract class NCBatteryModule {

    @Binds
    abstract fun bindsNCBattery(nickelCadmiumBattery: NickelCadmiumBattery): Battery

}
```

- SmartPhone.kt ⇒ Final module

```kotlin
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
```

- SmartPhoneComponent.kt

```kotlin
@Singleton
@Component(modules = [MemoryCardModule::class, NCBatteryModule::class])
interface SmartPhoneComponent {

//    fun getSmartPhone(): SmartPhone

    // inject dependencies to `MainActivity`
    fun inject(mainActivity: MainActivity)

}
```

- SmartPhoneApplication.kt which controls whole activities & fragments

```kotlin
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
```

- Manifest

⇒ set application name with our own `SmartPhoneApplication`

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.anushka.didemo">

    <application
        android:name=".SmartPhoneApplication"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
        <activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```

- MainActivity.kt, Final usage

```kotlin
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
```