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

/**
 * Hello world!
 * 
 */

public class App {
    Capture capture;
    Rectangle rectangle;
    User user;

    public static class Output implements HyperlinkListener {
        JTextPane pane;
        HTMLEditorKit kit = new HTMLEditorKit();
        HTMLDocument doc = new HTMLDocument();

        public Output(JTextPane pane) {
            pane.setEditorKit(kit);
            pane.setDocument(doc);

            pane.addHyperlinkListener(this);

            doc.getStyleSheet().addRule("A {color:blue;text-decoration:underline}");

        }

        public void begin(String html) {
            try {
                Element[] roots = doc.getRootElements();
                Element body = null;
                for (int i = 0; i < roots[0].getElementCount(); i++) {
                    Element element = roots[0].getElement(i);
                    if (element.getAttributes().getAttribute(StyleConstants.NameAttribute) == HTML.Tag.BODY) {
                        body = element;
                        break;
                    }
                }
                kit.insertHTML(doc, body.getStartOffset(), "<pre>" + html + "</pre>", 0, 0, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public void end(String html) {
            try {
                kit.insertHTML(doc, doc.getLength(), html, 0, 0, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public void hyperlinkUpdate(HyperlinkEvent arg0) {
            try {

                if (arg0.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    java.awt.Desktop.getDesktop().browse(new URI(arg0.getURL().toString()));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

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

        JTextPane text = new JTextPane();
        Output out = new Output(text);
        text.setEditable(false);

        JScrollPane pScroll = new JScrollPane(text, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        frame.getRootPane().add(pScroll, BorderLayout.CENTER);

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
                if (reinit)
                    app.init();
                app.run();
            } catch (Lookup.NotFound e) {
                exception(app, out, e, 5000);
                reinit = true;
            } catch (User.MouseMove e) {
                exception(app, out, e, 5000);
            } catch (Matrix.UnknownColorFlood e) {
                exception(app, out, e, 1000);
                reinit = true;
            } catch (Recognition.UnknownColor e) {
                exception(app, out, e, 1000);
            } catch (Exception e) {
                exception(app, out, e, 1000);
            }
        }
    }
}
