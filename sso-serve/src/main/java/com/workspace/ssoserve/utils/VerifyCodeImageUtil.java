package com.workspace.ssoserve.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class VerifyCodeImageUtil {
    private static final int lineSize = 80; //验证码中夹杂的干扰线数量

    private static int width = 112; //验证码的宽

    private static int height = 42; //验证码的高

    private static Font font = new Font("Times New Roman", Font.PLAIN, 30);

    private static Random random = new Random();

    static {
        // 解决生成图片异常问题
        // 如果有异常，安装下面软件
        //   yum install libgcc.i686 --setopt=protected_multilib=false
        //   yum grouplist
        //   yum groupinstall Fonts
        // 重新启动服务即可
        System.setProperty("java.awt.headless", "true");
    }

    public static BufferedImage generateImage(String verifyCode) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();
        g.fillRect(0, 0, width, height);
        g.setFont(font);
        drawInterferingLine(g);
        drawString(g, verifyCode);
        g.dispose();
        return image;
    }

    private static void drawInterferingLine(Graphics g) {
        for (int i = 0; i < lineSize; i++) {
            g.setColor(getRandomColor(105, 189));
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(20);
            int yl = random.nextInt(10);
            g.drawLine(x, y, x + xl, y + yl);
        }
    }

    private static void drawString(Graphics g, String randomStr) {
        g.translate(random.nextInt(3), random.nextInt(6));
        char[] chars = randomStr.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            g.setColor(getRandomColor(108, 190));
            g.drawString(String.valueOf(chars[i]), 25 * i + random.nextInt(10), 25);
        }
    }

    private static Color getRandomColor(int fc, int bc) {
        int tmpFc = Math.min(fc, 255);
        int tmpBc = Math.min(bc, 255);
        int r = tmpFc + random.nextInt(tmpBc - tmpFc - 16);
        int g = tmpFc + random.nextInt(tmpBc - tmpFc - 14);
        int b = tmpFc + random.nextInt(tmpBc - tmpFc - 12);
        return new Color(r, g, b);
    }
}
