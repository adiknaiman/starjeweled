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
                MoveMatrix m1 = makeMove(p1, p2);
                addBest(bestMove, m1);

                MatrixPoint p22 = new MatrixPoint(x + 1, y);
                MoveMatrix m2 = makeMove(p1, p22);
                addBest(bestMove, m2);

                MatrixPoint p23 = new MatrixPoint(x, y - 1);
                MoveMatrix m3 = makeMove(p1, p23);
                addBest(bestMove, m3);

                MatrixPoint p24 = new MatrixPoint(x, y + 1);
                MoveMatrix m4 = makeMove(p1, p24);
                addBest(bestMove, m4);
            }
        }

        if (bestMove.size() == 0)
            throw new NoMove();

        return bestMove;
    }

    MoveMatrix makeMove(MatrixPoint p1, MatrixPoint p2) {
        MoveMatrix mm = new MoveMatrix(m, p1, p2);

        if (mm.m2.move(p1, p2))
            fillLength(p1, p2, mm);

        return mm;
    }

    void fillLength(MatrixPoint p1, MatrixPoint p2, MoveMatrix mm) {
        int xl = 0;
        ArrayList<Point> xfill = new ArrayList<Point>();
        int yl = 0;
        ArrayList<Point> yfill = new ArrayList<Point>();

        Matrix m = mm.m2;

        for (int y = 0; y < m.cy; y++) {
            int l = 0;
            int x = 0;
            String ls = m.m[y * m.cx + x];
            ArrayList<Point> fill = new ArrayList<Point>();
            for (; x < m.cx; x++) {
                if (ls.equals(m.m[y * m.cx + x])) {
                    l++;
                    fill.add(new Point(x, y));
                } else {
                    if (l > xl) {
                        xl = l;
                        xfill = fill;
                    }

                    ls = m.m[y * m.cx + x];
                    fill = new ArrayList<Point>();
                    fill.add(new Point(x, y));
                    l = 1;
                }
            }
            if (l > xl) {
                xl = l;
                xfill = fill;
            }
        }

        for (int x = 0; x < m.cx; x++) {
            int l = 0;
            int y = 0;
            String ls = m.m[y * m.cx + x];
            ArrayList<Point> fill = new ArrayList<Point>();
            for (; y < m.cy; y++) {
                if (ls.equals(m.m[y * m.cx + x])) {
                    l++;
                    fill.add(new Point(x, y));
                } else {
                    if (l > yl) {
                        yl = l;
                        yfill = fill;
                    }

                    ls = m.m[y * m.cx + x];
                    fill = new ArrayList<Point>();
                    fill.add(new Point(x, y));
                    l = 1;
                }
            }
            if (l > yl) {
                yl = l;
                yfill = fill;
            }
        }

        int len = Math.max(xl, yl);
        if (len < 3)
            return;

        mm.len = len;

        if (xl > yl) {
            fill(m, p1, p2, xfill);
            return;
        } else {
            fill(m, p1, p2, yfill);
            return;
        }
    }

    void fill(Matrix m, MatrixPoint p1, MatrixPoint p2, ArrayList<Point> fill) {
        for (Point p : fill)
            m.m[p.y * m.cx + p.x] = MoveMatrix.TITLE_MATCH;

        m.m[p1.y * m.cx + p1.x] = MoveMatrix.TITLE_MATCH;
        m.m[p2.y * m.cx + p2.x] = MoveMatrix.TITLE_MATCH;
    }

}
