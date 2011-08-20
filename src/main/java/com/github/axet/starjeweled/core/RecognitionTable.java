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

public class RecognitionTable {

    static public class UnknownColor extends RuntimeException {
        private static final long serialVersionUID = 8507259522017015317L;

        public UnknownColor(String s) {
            super(s);
        }

        public UnknownColor(int rgb) {
            super("Unknown " + String.format("%02x", rgb));
        }
    }

    public BoardColorsTable colors;
    public Recognition r;

    public RecognitionTable(Recognition r, BoardColorsTable colors) {
        this.r = r;
        this.colors = colors;
    }

    public void print() {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                System.out.print(getType(r.matrix[y * 8 + x]) + " - ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
    }

    public String getType(int rgb) {
        for (TitleRangeColor c : colors.colorSet) {
            if (c.inRange(rgb))
                return c.title;
        }

        throw new UnknownColor(rgb);
    }

    public String getNearest(int rgb, int max) {
        max = Math.min(max, colors.colorMaxLimit);

        TreeMap<Integer, TitleRangeColor> map = new TreeMap<Integer, TitleRangeColor>();

        for (TitleRangeColor c : colors.colorSet) {
            int i = c.getDistance(rgb);
            map.put(i, c);
        }

        TitleRangeColor c = map.firstEntry().getValue();
        if (c.getDistance(rgb) > max)
            throw new UnknownColor(rgb);
        return c.title;
    }
}
