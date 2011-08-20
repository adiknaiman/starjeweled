package com.github.axet.starjeweled.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.axet.starjeweled.common.Matrix;
import com.github.axet.starjeweled.common.MatrixPoint;
import com.github.axet.starjeweled.common.RangeColor;
import com.github.axet.starjeweled.core.BoardColorsTable;
import com.github.axet.starjeweled.core.Recognition;

public class ColoredPane extends JPanel {

    private static final long serialVersionUID = 89452596762525986L;

    JPanel aspect = new JPanel();

    JLabel[] labels;
    int cx, cy;

    MatrixPoint p1;
    int p1rgb;
    MatrixPoint p2;
    int p2rgb;

    public ColoredPane() {
        setLayout(new AspectRatioLayout());
        add(aspect);
    }

    public void init(int cx, int cy) {
        this.cx = cx;
        this.cy = cy;

        aspect.removeAll();

        GridLayout gl = new GridLayout(cy, cx);

        aspect.setLayout(gl);

        labels = new JLabel[cy * cx];

        for (int i = 0; i < labels.length; i++) {
            JLabel l = new JLabel();
            labels[i] = l;

            aspect.add(l);
        }

        aspect.validate();
    }

    public void draw(BoardColorsTable colors, Matrix m) {
        for (int y = 0; y < m.cy; y++) {
            for (int x = 0; x < m.cx; x++) {
                String s = m.get(x, y);
                RangeColor r = colors.getColor(s);
                if (r == null) {
                    setColor(new MatrixPoint(x, y), Color.BLACK.getRGB());
                } else {
                    setColor(new MatrixPoint(x, y), r.average());
                }
            }
        }
    }

    void setColor(MatrixPoint p, int rgb) {
        int pos = cx * p.y + p.x;
        JLabel l = labels[pos];
        l.setOpaque(true);
        l.setBackground(new Color(rgb));
        l.repaint();
    }

    int getColor(MatrixPoint p) {
        int pos = cx * p.y + p.x;
        JLabel l = labels[pos];
        return l.getBackground().getRGB();
    }

    public void clearMove() {
        if (p1 != null) {
            setColor(p1, p1rgb);
            p1 = null;
        }

        if (p2 != null) {
            setColor(p2, p2rgb);
            p2 = null;
        }
    }

    public void move(MatrixPoint p1, MatrixPoint p2) {
        clearMove();

        this.p1 = p1;
        this.p1rgb = getColor(p1);
        this.p2 = p2;
        this.p2rgb = getColor(p2);

        setColor(p1, Color.WHITE.getRGB());
        setColor(p2, Color.WHITE.getRGB());
    }

}
