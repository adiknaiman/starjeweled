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
    MatrixPoint p2;

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
                    setColor(x, y, Color.BLACK.getRGB());
                } else {
                    setColor(x, y, r.average());
                }
            }
        }
    }

    void setColor(int x, int y, int rgb) {
        int pos = cx * y + x;
        JLabel l = labels[pos];
        l.setOpaque(true);
        l.setBackground(new Color(rgb));
        l.repaint();
    }

    void setLabel(int x, int y, String text) {
        int pos = cx * y + x;
        JLabel l = labels[pos];
        l.setText(text);
        l.setHorizontalTextPosition(JLabel.CENTER);
        l.setVerticalTextPosition(JLabel.CENTER);
        l.repaint();
    }

    public void clearMove() {
        if (p1 != null) {
            setColor(p1.x, p1.y, Color.BLACK.getRGB());
            p1 = null;
        }

        if (p2 != null) {
            setColor(p2.x, p2.y, Color.BLACK.getRGB());
            p2 = null;
        }
    }

    public void move(MatrixPoint p1, MatrixPoint p2) {
        clearMove();

        this.p1 = p1;
        this.p2 = p2;

        setColor(p1.x, p1.y, Color.WHITE.getRGB());
        setColor(p2.x, p2.y, Color.WHITE.getRGB());
    }

}
