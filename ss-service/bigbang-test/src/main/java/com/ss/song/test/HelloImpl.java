package com.ss.song.test;

public class HelloImpl implements Hello{
    @Override
    public void sayHello() {
        System.out.println("我被调用了，hello~");
    }
}
