package com.github.axet.starjeweled;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.github.axet.starjeweled.common.Matrix;
import com.github.axet.starjeweled.common.MoveMatrix;

/**
 * Hello world!
 * 
 * https://bitbucket.org/Cixelyn/starjeweled-bot
 */

public class App {

    public static void run() {
        Capture c = new Capture();
        // BufferedImage img =
        // c.load("/Users/axet/source/starjeweled/starjeweled/big6.png");
        BufferedImage img = c.capture();
        // c.write(img, "capture.png");

        // int r0 = img.getRGB(1225, 180);
        // int r1 = img.getRGB(1532, 568);
        // int r0 = img.getRGB(1168, 137);
        // int r1 = img.getRGB(1520, 566);

        Lookup l = new Lookup();
        BufferedImage i = l.filterMask(img);
        // c.write(i, "mask.png");
        BufferedImage i2 = l.filterNoise(i);
        // c.write(i2, "noise.png");
        Rectangle r = l.getBounds(i2);
        // BufferedImage i3 = l.crop(i2, r);
        // c.write(i3, "bounds.png");

        Recognition rr = new Recognition(img, r);

        rr.print();

        Matrix m = new Matrix(rr);
        SimpleAI a = new SimpleAI(m);
        ArrayList<MoveMatrix> ppp = a.getMove();

        try {
            Robot robo = new Robot();
            for (MoveMatrix p : ppp) {
                p.m2.print();

                Rectangle mr = rr.getRect(p.p1.x, p.p1.y);
                Point pp = rr.getMiddle(mr);
                robo.mouseMove(pp.x, pp.y);
                robo.mousePress(InputEvent.BUTTON1_MASK);
                Thread.sleep(50);
                robo.mouseRelease(InputEvent.BUTTON1_MASK);
                Thread.sleep(200);

                mr = rr.getRect(p.p2.x, p.p2.y);
                pp = rr.getMiddle(mr);
                robo.mouseMove(pp.x, pp.y);
                robo.mousePress(InputEvent.BUTTON1_MASK);
                Thread.sleep(50);
                robo.mouseRelease(InputEvent.BUTTON1_MASK);
                Thread.sleep(200);
            }

            robo.mouseMove(500, 500);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        while (true) {
            try {
                run();
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();

                try {
                    Thread.sleep(5000);
                } catch (Exception ignore) {

                }
            }
        }
    }

}
