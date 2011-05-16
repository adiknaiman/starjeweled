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
 */

public class App {
    Capture capture;
    Rectangle rectangle;
    User user;

    public void init() {
        user = new User();
        user.reset();

        capture = new Capture();
        // BufferedImage img =
        // capture.write.load("/Users/axet/source/starjeweled/starjeweled/big6.png");
        BufferedImage desktopImage;
        desktopImage = capture.capture();
        // capture.write.write(img, "capture.png");

        Lookup lookup = new Lookup();
        BufferedImage i = lookup.filterMask(desktopImage);
        // capture.write.write(i, "mask.png");
        BufferedImage i2 = lookup.filterNoise(i);
        // capture.write.write(i2, "noise.png");
        rectangle = lookup.getBounds(i2);
        // BufferedImage i3 = l.crop(i2, r);
        // capture.write.write(i3, "bounds.png");
    }

    public void run() {
        BufferedImage desktopRegion = capture.capture(rectangle);
        // capture.write(desktopRegion, "board.png");
        Recognition rr = new Recognition(desktopRegion);

        Matrix m = new Matrix(rr);
        SimpleAI a = new SimpleAI(m);
        ArrayList<MoveMatrix> ppp = a.getMove();

        for (MoveMatrix p : ppp) {
            Rectangle mr = rr.getRect(p.p1.x, p.p1.y);
            Point pp = rr.getMiddle(mr);
            pp.x += rectangle.x;
            pp.y += rectangle.y;
            user.click(pp);

            mr = rr.getRect(p.p2.x, p.p2.y);
            pp = rr.getMiddle(mr);
            pp.x += rectangle.x;
            pp.y += rectangle.y;
            user.click(pp);
        }

        user.move(new Point(rectangle.x + rectangle.width + 10, rectangle.y + rectangle.height + 10));
    }

    public static void main(String[] args) {

        App app = new App();

        boolean reinit = true;
        while (true) {
            try {
                if (reinit)
                    app.init();
                app.run();
                Thread.sleep(1500);
            } catch (Exception e) {
                e.printStackTrace();

                try {
                    Thread.sleep(5000);
                } catch (Exception ignore) {
                }

                reinit = true;
            }
        }
    }

}
