package com.example.kotlin.generic;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: <Test1><br>
 * Author:      mxdl<br>
 * Date:        2023/7/21<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
 */
public class Test1<t extends Animal> {
    public static void main(String[] args) {
        Test1 test1 = new Test1();
        AnimalShelter<Dog> dog = new AnimalShelter(new Dog("Dog"));
        AnimalShelter<? extends Animal> animal = dog;

        test1.getAnimalShelter(dog);

        List list = new ArrayList();
        list.add("aa");
        list.add(1);
    }
    public <T extends AnimalShelter<? extends Animal>> T getAnimalShelter(T t){
        return t;
    }
}
