package com.github.axet.starjeweled.common;

import com.github.axet.starjeweled.Recognition;

public class Matrix {

    public static class UnknownColorFlood extends RuntimeException {
    }

    public int cx;
    public int cy;
    public String[] matrix;

    public static String TITLE_SKULL = "SKULL";
    public static String TITLE_RED = "RED";
    public static String TITLE_YELLOW = "YELLOW";
    public static String TITLE_GREEN = "GREEN";
    public static String TITLE_PURPL = "PURPL";
    public static String TITLE_BLUE = "BLUE";

    public static String TITLE_UNKNOWN = "UNKNOWN";

    public Matrix(Recognition r) {
        int[] matrix = r.getMatrix();
        this.cx = r.getCX();
        this.cy = r.getCY();
        this.matrix = new String[matrix.length];

        int errCount = 0;
        for (int i1 = 0; i1 < matrix.length; i1++) {
            try {
                this.matrix[i1] = r.getType(matrix[i1]);
            } catch (Recognition.UnknownColor ignore) {
                errCount++;
                try {
                    this.matrix[i1] = r.getNearest(matrix[i1], 150);
                } catch (Recognition.UnknownColor ignore2) {
                    this.matrix[i1] = Matrix.TITLE_UNKNOWN;
                }
            }
        }

        if (errCount > 30)
            throw new UnknownColorFlood();
    }

    public Matrix(Matrix m) {
        this.matrix = m.matrix.clone();
        this.cx = m.cx;
        this.cy = m.cy;
    }

    public Matrix(String[] m, int cx, int cy) {
        this.cx = cx;
        this.cy = cy;
        this.matrix = m.clone();
    }

    public boolean move(MatrixPoint p1, MatrixPoint p2) {
        if (p2.x < 0 || p2.y < 0 || p2.x >= cx || p2.y >= cy)
            return false;

        String old = matrix[p2.y * cx + p2.x];
        matrix[p2.y * cx + p2.x] = matrix[p1.y * cx + p1.x];
        matrix[p1.y * cx + p1.x] = old;

        return true;
    }

    public void set(MatrixPoint p, String val) {
        set(p.x, p.y, val);
    }

    public void set(int x, int y, String val) {
        matrix[y * cx + x] = val;
    }

    public String get(MatrixPoint p) {
        return get(p.x, p.y);
    }

    public String get(int x, int y) {
        return matrix[y * cx + x];
    }

    public void print() {
        for (int y = 0; y < cy; y++) {
            for (int x = 0; x < cx; x++) {
                System.out.print(matrix[y * cx + x] + " - ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
