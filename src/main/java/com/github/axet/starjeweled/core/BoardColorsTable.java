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

public class BoardColorsTable {

    static public class WrongBounds extends RuntimeException {
        private static final long serialVersionUID = 4629573475279995934L;

        public WrongBounds(String s) {
            super(s);
        }

    }

    TreeMap<String, TitleRangeColor> colorSet;
    // first color fillup, do not group colors far then this distance
    int colorDisLimit = 50;
    // max distance between loaded / generated colors in table
    int colorMaxLimit;

    public BoardColorsTable(int[] matrix) {
        colorSet = fillColors(matrix);

        ArrayList<String> groups = getGroups();
        if (groups.size() != 0)
            throw new WrongBounds("not enougth color groups, mens wrong board bounds or more elements on screen");

        colorMaxLimit = getNearestLimit() / 2;
    }

    TreeMap<String, TitleRangeColor> fillColors(int[] matrix) {
        colorSet = new TreeMap<String, TitleRangeColor>();
        for (int a : matrix) {
            fillColor(colorSet, a);
        }
        return colorSet;
    }

    void fillColor(TreeMap<String, TitleRangeColor> colorSet, int color) {
        boolean found = false;
        for (TitleRangeColor r : colorSet.values()) {
            int dist = r.getDistance(color);
            if (dist < colorDisLimit) {
                r.merge(color);
                found = true;
            }
        }
        if (!found) {
            fillNewGroup(colorSet, color);
        }
    }

    ArrayList<String> getGroups() {
        ArrayList<String> groups = new ArrayList<String>(Arrays.asList(Matrix.TITLES));

        for (TitleRangeColor r : colorSet.values()) {
            groups.remove(r.title);
        }

        return groups;
    }

    void fillNewGroup(TreeMap<String, TitleRangeColor> colorSet, int color) {
        ArrayList<String> groups = getGroups();

        if (groups.size() == 0)
            throw new WrongBounds("too many color groups, mens wrong board bounds or more elements on screen");

        String name = groups.remove(0);
        colorSet.put(name, new TitleRangeColor(new RangeColor(color, color), name));
    }

    int getNearestLimit() {
        int val = -1;

        TitleRangeColor[] values = colorSet.values().toArray(new TitleRangeColor[0]);
        
        for (int x1 = 0; x1 < values.length; x1++) {
            for (int x2 = 0; x2 < values.length; x2++) {
                if (x1 == x2)
                    continue;

                int dist = values[x1].getDistance(values[x2].min);

                if (val == -1)
                    val = dist;

                val = Math.min(val, dist);
            }
        }

        return val;
    }

    public TitleRangeColor getColor(String name) {
        return colorSet.get(name);
    }

}
