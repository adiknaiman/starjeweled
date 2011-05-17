package com.github.axet.starjeweled.common;

import java.awt.Point;
import java.util.ArrayList;

public class MoveMatrix extends Matrix {

    public MatrixPoint p1;
    public MatrixPoint p2;
    public Matrix moveMatrix;
    public int len;

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
        int xPos = -1;
        int xPosY = -1;
        int yMaxLen = 0;
        int yPos = -1;
        int yPosX = -1;

        for (int y = 0; y < moveMatrix.cy; y++) {
            int x = 0;
            int seqLen = 0;
            String seqTitle = moveMatrix.get(x,y);
            for (; x < moveMatrix.cx; x++) {
                if (seqTitle.equals(moveMatrix.get(x,y)) && !seqTitle.equals(Matrix.TITLE_UNKNOWN)) {
                    seqLen++;
                } else {
                    if (seqLen > xMaxLen) {
                        xMaxLen = seqLen;
                        xPos = x - seqLen;
                        xPosY = y;
                    }

                    seqTitle = moveMatrix.get(x,y);
                    seqLen = 1;
                }
            }
            if (seqLen > xMaxLen) {
                xMaxLen = seqLen;
                xPos = x - 1 - seqLen;
                xPosY = y;
            }
        }

        for (int x = 0; x < moveMatrix.cx; x++) {
            int y = 0;
            int seqLen = 0;
            String seqTitle = moveMatrix.get(x,y);
            for (; y < moveMatrix.cy; y++) {
                if (seqTitle.equals(moveMatrix.get(x,y)) && !seqTitle.equals(Matrix.TITLE_UNKNOWN)) {
                    seqLen++;
                } else {
                    if (seqLen > yMaxLen) {
                        yMaxLen = seqLen;
                        yPos = y - seqLen;
                        yPosX = x;
                    }

                    seqTitle = moveMatrix.get(x,y);
                    seqLen = 1;
                }
            }
            if (seqLen > yMaxLen) {
                yMaxLen = seqLen;
                yPos = y - 1 - seqLen;
                yPosX = x;
            }
        }

        int len = Math.max(xMaxLen, yMaxLen);
        if (len < 3)
            return;

        this.len = len;

        if (xMaxLen > yMaxLen) {
            for (int x = xPos; x < xPos + xMaxLen; x++)
                moveMatrix.set(x, xPosY, MoveMatrix.TITLE_MATCH);
        } else {
            for (int y = yPos; y < yPos + yMaxLen; y++)
                moveMatrix.set(yPosX, y, MoveMatrix.TITLE_MATCH);
        }
        moveMatrix.set(p1.x, p1.y, MoveMatrix.TITLE_MATCH);
    }

}
