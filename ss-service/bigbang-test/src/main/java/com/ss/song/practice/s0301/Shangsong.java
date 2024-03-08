package com.ss.song.practice.s0301;

import org.junit.Test;

public class Shangsong extends Song{
    @Override
    public void aa() {
        super.aa();
        System.out.println("basss");
    }

    @Test
    public void bb() {
        aa();
    }
}
