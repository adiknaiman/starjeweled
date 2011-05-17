package com.github.axet.starjeweled;

import java.awt.Point;
import java.util.ArrayList;
import java.util.TreeMap;

import com.github.axet.starjeweled.common.Matrix;
import com.github.axet.starjeweled.common.MatrixPoint;
import com.github.axet.starjeweled.common.MoveMatrix;

public class SimpleAI {

    static public class NoMove extends RuntimeException {
    }

    Matrix m;

    public SimpleAI(Matrix matrix) {
        this.m = matrix;
    }

    void addBest(ArrayList<MoveMatrix> best, MoveMatrix mm) {
        if (mm.len < 3)
            return;

        boolean wasCross = false;
        boolean wasErased = false;

        for (int i = 0; i < best.size(); i++) {
            if (mm.checkCross(best.get(i))) {
                if (mm.len > best.get(i).len) {
                    best.remove(i);
                    i = 0;
                    wasErased = true;
                }
                wasCross = true;
            }
        }

        if (wasCross && !wasErased)
            return;

        best.add(mm);
    }

    public ArrayList<MoveMatrix> getMove() {
        ArrayList<MoveMatrix> bestMove = new ArrayList<MoveMatrix>();

        for (int y = 0; y < m.cy; y++) {
            for (int x = 0; x < m.cx; x++) {
                MatrixPoint p1 = new MatrixPoint(x, y);
                MatrixPoint p2 = new MatrixPoint(x - 1, y);
                MoveMatrix m1 = new MoveMatrix(m, p1, p2);
                addBest(bestMove, m1);

                MatrixPoint p22 = new MatrixPoint(x + 1, y);
                MoveMatrix m2 = new MoveMatrix(m, p1, p22);
                addBest(bestMove, m2);

                MatrixPoint p23 = new MatrixPoint(x, y - 1);
                MoveMatrix m3 = new MoveMatrix(m, p1, p23);
                addBest(bestMove, m3);

                MatrixPoint p24 = new MatrixPoint(x, y + 1);
                MoveMatrix m4 = new MoveMatrix(m, p1, p24);
                addBest(bestMove, m4);
            }
        }

        if (bestMove.size() == 0)
            throw new NoMove();

        return bestMove;
    }

}
