package com.ss.song.test;

import com.ss.song.interf.Flyable;

/**
 * author shangsong 2023/12/21
 */
public class FlyableDemo {

    public static void main(String[] args) {
        useFlyable((a, b) -> {
            System.out.println(a + b);
        });
    }

    public static void useFlyable(Flyable f) {
        f.fly(4, 6);
    }
}
