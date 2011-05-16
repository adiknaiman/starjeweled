package com.github.axet.starjeweled;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;

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

    void location(Point p) {
        if (prev == null) {
            prev = p;
            return;
        } else {
            Point location = MouseInfo.getPointerInfo().getLocation();

            if (!location.equals(prev)) {
                throw new MouseMove("mouse move!! location was changes");
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
