package com.github.axet.starjeweled;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;

public class User {

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

    void location(Point p) {
        System.out.println(p);
        System.out.println();
        if (prev == null) {
            prev = p;
            return;
        } else {
            Point location = MouseInfo.getPointerInfo().getLocation();

            if (!location.equals(prev)) {
                System.out.println("current!=prev " + location + " prev: " + prev);
                System.out.println();
                throw new RuntimeException("mouse move!! location was changes");
            }
            prev = p;
        }
    }

    public void move(Point p) {
        location(p);
        robot.mouseMove(p.x, p.y);
    }

    public void click(Point p) {
        location(p);
        robot.mouseMove(p.x, p.y);
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
