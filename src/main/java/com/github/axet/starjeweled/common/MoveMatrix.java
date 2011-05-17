package com.github.axet.starjeweled.common;

import java.awt.Point;
import java.util.ArrayList;

public class MoveMatrix extends Matrix {

    public MatrixPoint p1;
    public MatrixPoint p2;
    public Matrix moveMatrix;
    public int len;
    public String seqTitle;

    public static String TITLE_MATCH = "X";

    public MoveMatrix(Matrix m, MatrixPoint p1, MatrixPoint p2) {
        super(m);
        this.moveMatrix = new Matrix(m);
        this.p1 = p1;
        this.p2 = p2;
        this.len = 0;

        if (moveMatrix.move(p1, p2))
            fillLength(p1, p2);
    }

    /**
     * check if other matrix has cross elements after move
     * 
     * @param m
     * @return
     */
    public boolean checkCross(MoveMatrix m) {
        for (int i = 0; i < moveMatrix.matrix.length; i++) {
            if (moveMatrix.matrix[i].equals(TITLE_MATCH)) {
                if (moveMatrix.matrix[i].equals(m.moveMatrix.matrix[i]))
                    return true;
            }
        }
        return false;
    }

    void fillLength(MatrixPoint p1, MatrixPoint p2) {
        int xMaxLen = 0;
        String xTitle = null;
        int yMaxLen = 0;
        String yTitle = null;

        for (int y = 0; y < moveMatrix.cy; y++) {
            int x = 0;
            int seqLen = 0;
            String seqTitle = moveMatrix.get(x, y);
            for (; x < moveMatrix.cx; x++) {
                if (seqTitle.equals(moveMatrix.get(x, y)) && !seqTitle.equals(Matrix.TITLE_UNKNOWN)) {
                    seqLen++;
                } else {
                    if (seqLen > xMaxLen) {
                        xMaxLen = seqLen;
                        xTitle = seqTitle;
                    }

                    seqTitle = moveMatrix.get(x, y);
                    seqLen = 1;
                }
            }
            if (seqLen > xMaxLen) {
                xMaxLen = seqLen;
                xTitle = seqTitle;
            }
        }

        for (int x = 0; x < moveMatrix.cx; x++) {
            int y = 0;
            int seqLen = 0;
            String seqTitle = moveMatrix.get(x, y);
            for (; y < moveMatrix.cy; y++) {
                if (seqTitle.equals(moveMatrix.get(x, y)) && !seqTitle.equals(Matrix.TITLE_UNKNOWN)) {
                    seqLen++;
                } else {
                    if (seqLen > yMaxLen) {
                        yMaxLen = seqLen;
                        yTitle = seqTitle;
                    }

                    seqTitle = moveMatrix.get(x, y);
                    seqLen = 1;
                }
            }
            if (seqLen > yMaxLen) {
                yMaxLen = seqLen;
                yTitle = seqTitle;
            }
        }

        int len = Math.max(xMaxLen, yMaxLen);
        if (len < 3)
            return;

        this.len = len;

        if (xMaxLen > yMaxLen)
            seqTitle = xTitle;
        if (xMaxLen < yMaxLen)
            seqTitle = yTitle;

        fillSeq();

        moveMatrix.set(p1.x, p1.y, MoveMatrix.TITLE_MATCH);
        moveMatrix.set(p2.x, p2.y, MoveMatrix.TITLE_MATCH);
    }

    /**
     * Sometimes by one move we can collect 2 sequinces, one what we have found
     * and another - by accident. This function will help us found it.
     */
    void fillSeq() {
        // we cant change moveMatrix directly.
        // some combinations like:
        // * ***
        // *
        // *
        //
        // ****
        // *
        // *
        // may cause double sequinces and symbol X may hide those changes.
        Matrix change = new Matrix(moveMatrix);

        for (int y = 0; y < moveMatrix.cy; y++) {
            int x = 0;
            int seqLen = 0;
            String seqTitle = moveMatrix.get(x, y);
            for (; x < moveMatrix.cx; x++) {
                if (seqTitle.equals(moveMatrix.get(x, y)) && !seqTitle.equals(Matrix.TITLE_UNKNOWN)) {
                    seqLen++;
                } else {
                    if (seqLen >= 3) {
                        for (int xx = x - seqLen; xx < x; xx++) {
                            change.set(xx, y, MoveMatrix.TITLE_MATCH);
                        }
                    }

                    seqTitle = moveMatrix.get(x, y);
                    seqLen = 1;
                }
            }
            if (seqLen >= 3) {
                for (int xx = x - seqLen; xx < x; xx++) {
                    change.set(xx, y, MoveMatrix.TITLE_MATCH);
                }
            }
        }

        for (int x = 0; x < moveMatrix.cx; x++) {
            int y = 0;
            int seqLen = 0;
            String seqTitle = moveMatrix.get(x, y);
            for (; y < moveMatrix.cy; y++) {
                if (seqTitle.equals(moveMatrix.get(x, y)) && !seqTitle.equals(Matrix.TITLE_UNKNOWN)) {
                    seqLen++;
                } else {
                    if (seqLen >= 3) {
                        for (int yy = y - seqLen; yy < y; yy++) {
                            change.set(x, yy, MoveMatrix.TITLE_MATCH);
                        }
                    }

                    seqTitle = moveMatrix.get(x, y);
                    seqLen = 1;
                }
            }
            if (seqLen >= 3) {
                for (int yy = y - seqLen; yy < y; yy++) {
                    change.set(x, yy, MoveMatrix.TITLE_MATCH);
                }
            }
        }

        moveMatrix = change;
    }

}
