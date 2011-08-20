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
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import com.github.axet.starjeweled.common.Matrix;
import com.github.axet.starjeweled.common.MoveMatrix;
import com.github.axet.starjeweled.core.BoardColorsTable;
import com.github.axet.starjeweled.core.Capture;
import com.github.axet.starjeweled.core.Lookup;
import com.github.axet.starjeweled.core.Recognition;
import com.github.axet.starjeweled.core.RecognitionTable;
import com.github.axet.starjeweled.core.SimpleAI;
import com.github.axet.starjeweled.core.User;
import com.github.axet.starjeweled.ui.Output;

/**
 * Hello world!
 * 
 */

public class App {
    Capture capture;
    Rectangle rectangle;
    BoardColorsTable colorTable;
    User user;

    public void init() {
        user = new User();
        user.reset();

        capture = new Capture();
        BufferedImage desktopImage;
        // desktopImage =
        // capture.load("/Users/axet/Desktop//Screen Shot 2011-08-19 at 18.55.22.png");
        desktopImage = capture.capture();
        // capture.write(desktopImage, "capture.png");

        Lookup lookup = new Lookup();
        BufferedImage i = lookup.filterMask(desktopImage);
        // capture.write(i, "mask.png");
        BufferedImage i2 = lookup.filterNoise(i);
        // capture.write(i2, "noise.png");
        rectangle = lookup.getBounds(i2);
        // BufferedImage i3 = lookup.crop(i2, rectangle);
        // capture.write(i3, "bounds.png");

        Recognition rr = new Recognition(desktopImage, rectangle);
        colorTable = new BoardColorsTable(rr.matrix);
    }

    public void run() {
        // BufferedImage desktopRegion =
        // capture.load("/Users/axet/Desktop//Screen Shot 2011-08-19 at 18.55.22.png",
        // rectangle);
        BufferedImage desktopRegion = capture.capture(rectangle);
        // capture.write(desktopRegion, "board.png");
        Recognition rr = new Recognition(desktopRegion);
        RecognitionTable rtable = new RecognitionTable(rr, colorTable);

        Matrix m = new Matrix(rtable);
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

    static public void exception(App app, Output out, Exception e, int time) {
        e.printStackTrace();

        out.begin(new Date() + ": " + exceptionString(e));

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

        Output out = new Output();

        frame.getRootPane().add(out, BorderLayout.CENTER);

        out.end("<br/>");
        out.end("Welcome to Starjweled screen analyser");
        out.end("<br/>");
        out.end("If you wana know how this script working please goto:<br/><a href='https://github.com/axet/starjeweled/wiki/Analyse'>https://github.com/axet/starjeweled/wiki/Analyse</a>");
        out.end("<br/>");
        out.end("If you have some problems please visit Bugs FAQ page:<br/><a href='https://github.com/axet/starjeweled/wiki/Bugs'>https://github.com/axet/starjeweled/wiki/Bugs</a>");
        out.end("<br/>");

        frame.setVisible(true);

        App app = new App();

        boolean reinit = true;
        while (true) {
            try {
                if (reinit) {
                    app.init();
                    reinit = false;
                }
                app.run();
            } catch (Lookup.NotFound e) {
                exception(app, out, e, 5000);
                reinit = true;
            } catch (User.MouseMove e) {
                app.user.reset();
                exception(app, out, e, 5000);
            } catch (BoardColorsTable.WrongBounds e) {
                exception(app, out, e, 1000);
                reinit = true;
            } catch (Matrix.UnknownColorFlood e) {
                exception(app, out, e, 1000);
                reinit = true;
            } catch (RecognitionTable.UnknownColor e) {
                exception(app, out, e, 1000);
            } catch (Exception e) {
                exception(app, out, e, 1000);
            }
        }
    }
}
