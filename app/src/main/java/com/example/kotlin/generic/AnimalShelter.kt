package com.example.kotlin.generic

import com.example.kotlin.generic.Animal

/**
 * Description: <AnimalShelter><br></br>
 * Author:      mxdl<br></br>
 * Date:        2023/7/21<br></br>
 * Version:     V1.0.0<br></br>
 * Update:     <br></br>
</AnimalShelter> */
class AnimalShelter<T : Animal>(var t: T) {
    val animal: Animal
        get() = t
}