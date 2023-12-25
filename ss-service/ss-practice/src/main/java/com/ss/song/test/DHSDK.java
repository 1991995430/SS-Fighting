package com.ss.song.test;

import com.ss.song.inter.IMacInfo;

/**
 * author shangsong 2023/12/5
 */
public class DHSDK {
    private static DHSDK singleton = null;

    private IMacInfo macInfo;

    private boolean isSampling = false;

    public static DHSDK getInstance() {
        if (singleton == null) {
            Class var0 = DHSDK.class;
            synchronized (DHSDK.class) {
                if (singleton == null) {
                    System.setProperty("jna.encoding", "GBK");
                    singleton = new DHSDK();
                }
            }
        }

        return singleton;
    }

    public void setDataRespond(IMacInfo macInfo) {
        this.macInfo = macInfo;
    }

    public boolean startSample() {
        isSampling = true;
        /*while (isSampling) {

        }*/
        float a = 2.3f;
        float[] aa = new float[20];
        for (int i = 0; i < 20; i++) {
            aa[i] = a + i;
            if (macInfo != null) {
                macInfo.getAllInfo(i, i + 12, aa);
            }
        }
        return true;
    }

    public boolean stopSample() {
        isSampling = false;

        return true;
    }

    public boolean getSampleStatue() {
        return this.isSampling;
    }
}
