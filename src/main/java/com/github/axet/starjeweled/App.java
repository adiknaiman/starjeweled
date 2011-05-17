package com.github.axet.starjeweled;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

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
        BufferedImage desktopImage;
        // desktopImage =
        // capture.load("/Users/axet/source/starjeweled/starjeweled/big6.png");
        desktopImage = capture.capture();
        // capture.write(desktopImage, "capture.png");

        Lookup lookup = new Lookup();
        BufferedImage i = lookup.filterMask(desktopImage);
        // capture.write(i, "mask.png");
        BufferedImage i2 = lookup.filterNoise(i);
        // capture.write(i2, "noise.png");
        rectangle = lookup.getBounds(i2);
        BufferedImage i3 = lookup.crop(i2, rectangle);
        // capture.write(i3, "bounds.png");
    }

    public void run() {
        BufferedImage desktopRegion = capture.capture(rectangle);
        // capture.write(desktopRegion, "board.png");
        Recognition rr = new Recognition(desktopRegion);

        Matrix m = new Matrix(rr);
        SimpleAI a = new SimpleAI(m);
        ArrayList<MoveMatrix> ppp = a.getMove();

        int missClick = 10;

        for (MoveMatrix p : ppp) {
            Rectangle mr = rr.getRect(p.p1.x, p.p1.y);
            Point pp = rr.getMiddle(mr);
            pp.x += rectangle.x;
            pp.y += rectangle.y;
            user.click(user.random(pp, missClick));

            mr = rr.getRect(p.p2.x, p.p2.y);
            pp = rr.getMiddle(mr);
            pp.x += rectangle.x;
            pp.y += rectangle.y;
            user.click(user.random(pp, missClick));
        }

        Point home = new Point(rectangle.x + rectangle.width + 10, rectangle.y + rectangle.height + 10);
        user.move(user.random(home, missClick));
    }

    static public String exceptionString(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }

    static public void exception(App app, JTextArea text, Exception e, int time) {
        e.printStackTrace();

        text.select(0, 0);
        text.replaceSelection(new Date() + ": " + exceptionString(e));

        try {
            Thread.sleep(time);
        } catch (Exception ignore) {
        }
    }

    public static void main(String[] args) {

        JFrame frame = new JFrame("Starjeweled");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(600, 300));
        frame.setAlwaysOnTop(true);
        frame.getRootPane().setLayout(new BorderLayout());

        JTextArea text = new JTextArea();
        text.setEditable(false);
        JScrollPane pScroll = new JScrollPane(text, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        frame.getRootPane().add(pScroll, BorderLayout.CENTER);

        frame.setVisible(true);

        App app = new App();

        boolean reinit = true;
        while (true) {
            try {
                if (reinit)
                    app.init();
                app.run();
            } catch (Lookup.NotFound e) {
                exception(app, text, e, 5000);
                reinit = true;
            } catch (User.MouseMove e) {
                exception(app, text, e, 5000);
                reinit = true;
            } catch (Matrix.UnknownColorFlood e) {
                exception(app, text, e, 1000);
                reinit = true;
            } catch (Recognition.UnknownColor e) {
                exception(app, text, e, 1000);
                reinit = true;
            } catch (Exception e) {
                exception(app, text, e, 1000);
                reinit = true;
            }
        }
    }

}
