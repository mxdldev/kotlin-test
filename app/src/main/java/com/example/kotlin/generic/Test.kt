package com.example.kotlin.generic

import android.app.Activity
import android.app.Person

/**
 * Description: <Test><br>
 * Author:      mxdl<br>
 * Date:        2023/7/18<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
 */
inline fun <reified T> printType(item: T) {
    val typeName = T::class.simpleName
    println("Type of $item is $typeName")
}

class Container<T>(val item: T)

fun main() {
    var dog: AnimalShelter1<Dog> = AnimalShelter1<Dog>()
    var dog1: AnimalShelter1<Cat> = AnimalShelter1<Cat>()
}
