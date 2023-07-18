package com.example.kotlin.generic;

/**
 * Description: <TestJava><br>
 * Author:      mxdl<br>
 * Date:        2023/7/18<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
 */
import java.util.ArrayList;
import java.util.List;

public class GenericExample<T> {
    private T value;

    public GenericExample(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public static void main(String[] args) {
        GenericExample<String> example = new GenericExample<>("Hello");
        List list = new ArrayList();

        list.add(example);

        GenericExample retrievedExample = (GenericExample) list.get(0);
        Object value = retrievedExample.getValue();

        System.out.println(value);  // 输出：Hello
    }
}
