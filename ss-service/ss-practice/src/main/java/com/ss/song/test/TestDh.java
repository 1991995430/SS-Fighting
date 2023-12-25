package com.ss.song.test;

import com.ss.song.inter.IMacInfo;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


/**
 * author shangsong 2023/12/5
 */
public class TestDh extends Application implements IMacInfo {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //initApp();

        boolean isTrue = true;

        while (isTrue) {
            Scanner console = new Scanner(System.in);
            System.out.println("\nplease input your cmd:" +
                    "\n[0]init" +
                    "\n[1]get device info" +
                    "\n[2]set sample freq" +
                    "\n[3]start sample" +
                    "\n[4]stop sample" +
                    "\n[5]modify current param" +
                    "\n[6]balance channel" +
                    "\n[7]clear zero channel" +
                    "\n[8]import clear zero" +
                    "\n[9]quit"+
                    "\n[10]refindMac");
            int cmd = console.nextInt();
            switch (cmd) {
                case 0: {
                    initApp();
                }
                break;
                case 1: {
                    startSample();
                }
                break;
                case 2: {
                    stopSample();
                }
                break;
                case 3: {
                    System.out.println("采样状态：" + getSampleStatue());
                }
                break;
            }
        }

        if (startSample()) {
            System.out.println("采集数据完成");
        }

        stop();
    }

    private void printData() {
        System.out.println();
    }

    public void stop() {
        Platform.exit();
    }

    private void initApp() throws IOException {
        DHSDK.getInstance().setDataRespond(this);
    }

    private boolean startSample() {
        return DHSDK.getInstance().startSample();
    }

    private boolean stopSample() {
        return DHSDK.getInstance().stopSample();
    }

    private boolean getSampleStatue() {
        return DHSDK.getInstance().getSampleStatue();
    }

    @Override
    public void getAllInfo(int deviceId, int channelId, float[] data) {
        // 输出到指定文件
        String  result  =  arrayToString(data);
        String prefix = "deviceId:" + deviceId + " channelId:" + channelId + "::::";
        String finalData= prefix + result + "\n";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("D://output.txt", true))) {
            writer.write(finalData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  static  String  arrayToString(float[]  data)  {
        if  (data  ==  null  ||  data.length  ==  0)  {
            return  "";
        }

        StringBuilder  sb  =  new  StringBuilder();
        for  (int  i  =  0;  i  <  data.length  -  1;  i++)  {
            sb.append(data[i]).append(",");
        }
        sb.append(data[data.length  -  1]);
        return  sb.toString();
    }
}
