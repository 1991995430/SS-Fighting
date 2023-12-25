package com.ss.song.test;

/**
 * author shangsong 2023/12/7
 */
public class CallbackImpl implements ChannDataAA{
    @Override
    public void channelData(int a, int b, String c) {
        System.out.println(a + " -- " + b + " -- " + c);
    }
}
