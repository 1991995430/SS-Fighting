package com.ss.song.test.t0421;

public class TestThread1 {
    public static void main(String[] args) throws InterruptedException {

        Number number = new Number();
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1000000; i++) {
                    number.autoIncrement();
                }
            }
        });
        thread1.start();

        for (int i = 0; i < 1000000; i++) {
            number.autoIncrement();
        }
        thread1.join();
        System.out.println(number.getNum());
    }
}
