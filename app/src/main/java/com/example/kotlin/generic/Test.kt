package com.example.kotlin.generic

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
    //printType("Hello")  // 输出：Type of Hello is String
    //printType(42)       // 输出：Type of 42 is Int

    val container = Container<String>("Hello")
    val item = container.item // 在编译后的字节码中，item的类型为Any

    if (item is String) {
        val length = item.length // 编译器插入了强制类型转换
        println("Length: $length")
    }
}
