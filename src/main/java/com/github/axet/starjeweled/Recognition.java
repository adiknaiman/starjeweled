package com.github.axet.starjeweled;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

import com.github.axet.starjeweled.common.Matrix;
import com.github.axet.starjeweled.common.RangeColor;
import com.github.axet.starjeweled.common.TitleRangeColor;

public class Recognition {

    static public class UnknownColor extends RuntimeException {
        public UnknownColor(String s) {
            super(s);
        }

        public UnknownColor(int rgb) {
            super("Unknown " + String.format("%02x", rgb));
        }
    }

    BufferedImage img;
    Rectangle r;

    ArrayList<TitleRangeColor> colorSet;

    int cx = 8;
    int cy = 8;
    int[] matrix;
    int cx_step;
    int cy_step;

    {
        colorSet = new ArrayList<TitleRangeColor>();
        colorSet.add(new TitleRangeColor(0x650505, 0x9a311d, Matrix.TITLE_RED));
        colorSet.add(new TitleRangeColor(0x404030, 0x58565a, Matrix.TITLE_SKULL));
        colorSet.add(new TitleRangeColor(0x401040, 0x60326a, Matrix.TITLE_PURPL));
        colorSet.add(new TitleRangeColor(0x606539, 0x858a45, Matrix.TITLE_YELLOW));
        colorSet.add(new TitleRangeColor(0x297528, 0x53a535, Matrix.TITLE_GREEN));
        colorSet.add(new TitleRangeColor(0x2080a0, 0x49afd9, Matrix.TITLE_BLUE));
    }

    public Recognition(BufferedImage img, Rectangle r) {
        this.img = img;
        this.r = r;

        cx_step = r.width / cx;
        cy_step = r.height / cy;

        matrix = new int[cx * cy];

        getMatrix();
    }

    public Recognition(BufferedImage img) {
        this.img = img;
        this.r = new Rectangle(img.getWidth(), img.getHeight());

        cx_step = r.width / cx;
        cy_step = r.height / cy;

        matrix = new int[cx * cy];

        getMatrix();
    }

    public int getCX() {
        return cx;
    }

    public int getCY() {
        return cy;
    }

    public Rectangle getRect(int x, int y) {
        Rectangle rr = new Rectangle(r);
        rr.x += x * cx_step;
        rr.y += y * cy_step;
        rr.width = cx_step;
        rr.height = cy_step;

        return rr;
    }

    public Point getMiddle(Rectangle r) {
        return new Point(r.x + r.width / 2, r.y + r.height / 2);
    }

    public void print() {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                System.out.print(getType(matrix[y * 8 + x]) + " - ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
    }

    public int[] getMatrix() {
        for (int y = 0; y < cy; y++) {
            for (int x = 0; x < cx; x++) {
                matrix[y * cx + x] = getAverage(img, getRect(x, y));
            }
        }

        return matrix;
    }

    public static BufferedImage getImageFromArray(int[] pixels, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, width, height, pixels, 0, width);
        return image;
    }

    int getAverage(BufferedImage img, Rectangle rrr) {
        Rectangle rr = new Rectangle(rrr);

        rr.x += 5;
        rr.y += 5;
        rr.width -= 10;
        rr.height -= 10;

        int[] buf = new int[rr.width * rr.height];
        img.getRGB(rr.x, rr.y, rr.width, rr.height, buf, 0, rr.width);

        int average = 0;

        int r = 0;
        int g = 0;
        int b = 0;

        for (int y = 0; y < rr.height; y++) {
            for (int x = 0; x < rr.width; x++) {
                int c = buf[y * rr.width + x];

                int cr = (c >> 16) & 0xff;
                int cg = (c >> 8) & 0xff;
                int cb = (c >> 0) & 0xff;

                r = r + cr;
                g = g + cg;
                b = b + cb;
            }
        }

        int c = rr.width * rr.height;

        r = (r / c) & 0xff;
        g = (g / c) & 0xff;
        b = (b / c) & 0xff;

        average = (r << 16) | (g << 8) | (b);

        return average;
    }

    public String getType(int rgb) {
        for (TitleRangeColor c : colorSet) {
            if (c.inRange(rgb))
                return c.title;
        }

        throw new UnknownColor(rgb);
    }

    public String getNearest(int rgb, int max) {
        TreeMap<Integer, TitleRangeColor> map = new TreeMap<Integer, TitleRangeColor>();

        for (TitleRangeColor c : colorSet) {
            int i = c.getDistance(rgb);
            map.put(i, c);
        }

        TitleRangeColor c = map.firstEntry().getValue();
        if (c.getDistance(rgb) > max)
            throw new UnknownColor(rgb);
        return c.title;
    }
}
