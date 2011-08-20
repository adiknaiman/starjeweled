package com.github.axet.starjeweled.core;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.TreeSet;

import com.github.axet.starjeweled.common.Matrix;
import com.github.axet.starjeweled.common.RangeColor;
import com.github.axet.starjeweled.common.TitleRangeColor;

/**
 * Class looking for matrix elements and help calculate its average colors.
 * 
 * @author axet
 * 
 */

public class Recognition {

    BufferedImage img;
    Rectangle r;

    BoardColorsTable table;

    // number of horizontal element on board
    int cx = 8;
    // number of vertical element on board
    int cy = 8;
    // matrix of average color
    public int[] matrix;
    // width in pixels of board cell
    int cx_step;
    // Height in pixels of board cell
    int cy_step;
    // step in pixels from cell bounds
    int cell_bounds = 5;

    public Recognition(BufferedImage img, Rectangle r) {
        this.img = img;
        this.r = r;

        cx_step = r.width / cx;
        cy_step = r.height / cy;

        matrix = getMatrix();
    }

    public Recognition(BufferedImage img) {
        this.img = img;
        this.r = new Rectangle(img.getWidth(), img.getHeight());

        cx_step = r.width / cx;
        cy_step = r.height / cy;

        matrix = getMatrix();
    }

    /**
     * get working image width.
     * 
     * @return image width
     */
    public int getCX() {
        return cx;
    }

    /**
     * get working image height.
     * 
     * @return image height
     */
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

    public int[] getMatrix() {
        int[] matrix = new int[cx * cy];

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

    /**
     * Get average color for a rectangle.
     * 
     * @param img
     * @param rrr
     * @return
     */
    int getAverage(BufferedImage img, Rectangle rrr) {
        Rectangle rr = new Rectangle(rrr);

        rr.x += cell_bounds;
        rr.y += cell_bounds;
        rr.width -= cell_bounds * 2;
        rr.height -= cell_bounds * 2;

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

}
