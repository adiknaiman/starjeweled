package com.github.axet.starjeweled.common;

import com.github.axet.starjeweled.Recognition;

public class Matrix {
    public int cx;
    public int cy;
    public String[] m;

    public Matrix(Recognition r) {
        int[] matrix = r.getMatrix();
        this.cx = r.getCX();
        this.cy = r.getCY();
        m = new String[matrix.length];
        for (int i1 = 0; i1 < matrix.length; i1++) {
            m[i1] = r.getType(matrix[i1]);
        }

    }

    public Matrix(Matrix m) {
        this.m = m.m.clone();
        this.cx = m.cx;
        this.cy = m.cy;
    }

    public Matrix(String[] m, int cx, int cy) {
        this.cx = cx;
        this.cy = cy;
        this.m = m.clone();
    }

    public void move(MatrixPoint p1, MatrixPoint p2) {
        if (p2.x < 0 || p2.y < 0 || p2.x >= cx || p2.y >= cy)
            return;

        String old = m[p2.y * cx + p2.x];
        m[p2.y * cx + p2.x] = m[p1.y * cx + p1.x];
        m[p1.y * cx + p1.x] = old;
    }

    public void print() {
        for (int y = 0; y < cy; y++) {
            for (int x = 0; x < cx; x++) {
                System.out.print(m[y * cx + x] + " - ");
            }
            System.out.println();
        }

        System.out.println();
        System.out.println();

    }
}
