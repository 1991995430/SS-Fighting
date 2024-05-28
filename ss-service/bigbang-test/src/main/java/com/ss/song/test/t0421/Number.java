package com.ss.song.test.t0421;

public class Number {
    int num = 0;

    public int getNum() {
        return num;
    }

    public void autoIncrement() {
        //synchronized (this) {
            num++;
        //}
    }
}
