package com.github.axet.starjeweled.ui;

import java.awt.*;

public class AspectRatioLayout implements LayoutManager {

    public AspectRatioLayout() {
    }

    public void addLayoutComponent(String arg0, Component arg1) {
    }

    public void layoutContainer(Container parent) {
        Insets insets = parent.getInsets();
        int maxWidth = parent.getWidth() - (insets.left + insets.right);
        int maxHeight = parent.getHeight() - (insets.top + insets.bottom);
        int nComps = parent.getComponentCount();

        if (nComps < 1)
            return;

        int min = Math.min(maxHeight, maxWidth);

        int x = (maxWidth - min) / 2;
        int y = (maxHeight - min) / 2;

        Component c = parent.getComponent(0);
        c.setBounds(x, y, min, min);
    }

    public Dimension minimumLayoutSize(Container arg0) {
        return new Dimension(0, 0);
    }

    public Dimension preferredLayoutSize(Container arg0) {
        return new Dimension(0, 0);
    }

    public void removeLayoutComponent(Component arg0) {
    }

}
