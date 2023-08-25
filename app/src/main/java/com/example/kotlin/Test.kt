package com.example.kotlin

import java.util.*
import kotlin.math.ceil

/**
 * Description: <Test><br>
 * Author:      mxdl<br>
 * Date:        2023/8/24<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
 */
class Test {}

fun main(args: Array<String>) {
    divideAndPrint(350, 100)
}

fun divideAndPrint(number: Int, divisor: Int) {
    var remainder = number
    while (remainder >= divisor) {
        println(divisor)
        remainder -= divisor
    }
    println(remainder)
}
