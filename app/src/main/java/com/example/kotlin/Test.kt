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
    repeat(ceil(440/100f).toInt()){
        //println("ok")
    }
    var queue = LinkedList<Int>()
    queue.add(1)
    queue.add(2)
    queue.add(3)
    queue.add(4)
    queue.add(5)
    println(queue.poll())
    println(queue.poll())
    println(queue.poll())
    println(queue.poll())
    println(queue.poll())
    println(queue.poll())
}