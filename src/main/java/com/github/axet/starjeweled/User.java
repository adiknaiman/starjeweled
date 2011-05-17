package com.github.axet.starjeweled;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.Random;

public class User {

    static class MouseMove extends RuntimeException {
        public MouseMove(String s) {
            super(s);
        }
    }

    Point prev;
    Robot robot;

    public User() {
        try {
            robot = new Robot();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void reset() {
        prev = null;
    }

    /**
     * Before move mouse to location p, call this function.
     * 
     * @param p
     *            - new coords
     */
    void location(Point p) {
        if (prev == null) {
            prev = new Point(p);
            return;
        } else {
            Point location = MouseInfo.getPointerInfo().getLocation();

            if (!location.equals(prev)) {
                throw new MouseMove("mouse move!! location was changes");
            }
            prev = new Point(p);
        }
    }

    public void move(Point p) {
        mouseFollow(p);
    }

    public Point random(Point p, int size) {
        p = new Point(p);
        p.x += Math.random() * size;
        p.y += Math.random() * size;
        return p;
    }

    void mouseFollow(Point p2) {
        Point p1 = new Point(MouseInfo.getPointerInfo().getLocation());

        Point p = new Point(p1);

        int distance = (int) Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));

        int steps = distance / 5;
        double k = 1. / steps;

        for (int i = 0; i < steps; i++) {
            p.x = (int) (p1.x + i * k * (p2.x - p1.x));
            p.y = (int) (p1.y + i * k * (p2.y - p1.y));

            location(p);
            robot.mouseMove(p.x, p.y);

            try {
                Thread.sleep(5);
            } catch (Exception ignore) {
            }
        }

        location(p2);
        robot.mouseMove(p2.x, p2.y);
    }

    public void click(Point p) {
        mouseFollow(p);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        try {
            Thread.sleep(50);
        } catch (Exception ignore) {
        }
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        try {
            Thread.sleep(200);
        } catch (Exception ignore) {
        }
    }
}
