package com.github.axet.starjeweled.common;

public class MoveMatrix extends Matrix {

    public MatrixPoint p1;
    public MatrixPoint p2;

    public Matrix m2;

    public int len;

    public MoveMatrix(Matrix m, Matrix m2, MatrixPoint p1, MatrixPoint p2, int len) {
        super(m);
        this.m2 = new Matrix(m2);
        this.p1 = p1;
        this.p2 = p2;
        this.len = len;
    }

    public MoveMatrix(Matrix m, MatrixPoint p1, MatrixPoint p2) {
        super(m);
        this.m2 = new Matrix(m);
        this.p1 = p1;
        this.p2 = p2;
        this.len = 0;
    }

    /**
     * check if other matrix has cross elements after move
     * 
     * @param m
     * @return
     */
    public boolean checkCross(MoveMatrix m) {        
        for (int i = 0; i < m2.m.length; i++) {
            if (m2.m[i].equals("X")) {
                if (m2.m[i].equals(m.m2.m[i]))
                    return true;
            }
        }

        return false;

    }
}
