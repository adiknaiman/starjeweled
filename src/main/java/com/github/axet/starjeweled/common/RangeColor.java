package com.github.axet.starjeweled.common;

public class RangeColor {
    int min;
    int max;

    public RangeColor(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public boolean inRange(int rgb) {
        int r1 = rgb & 0xff0000;
        int g1 = rgb & 0x00ff00;
        int b1 = rgb & 0x0000ff;

        int rl = min & 0xff0000;
        int gl = min & 0x00ff00;
        int bl = min & 0x0000ff;

        int rh = max & 0xff0000;
        int gh = max & 0x00ff00;
        int bh = max & 0x0000ff;

        return (r1 >= rl && r1 <= rh) && (g1 >= gl && g1 <= gh) && (b1 >= bl && b1 <= bh);
    }

    public int getDistance(int rgb) {
        int r1 = rgb & 0xff0000;
        int g1 = rgb & 0x00ff00;
        int b1 = rgb & 0x0000ff;

        int rl = min & 0xff0000;
        int gl = min & 0x00ff00;
        int bl = min & 0x0000ff;

        int rh = max & 0xff0000;
        int gh = max & 0x00ff00;
        int bh = max & 0x0000ff;

        if ((r1 >= rl && r1 <= rh) && (g1 >= gl && g1 <= gh) && (b1 >= bl && b1 <= bh))
            return 0;

        int total = 0;

        if (r1 < rl)
            total += rl - r1;

        if (r1 > rh)
            total += r1 - rh;

        if (g1 < gl)
            total += gl - g1;

        if (g1 > gh)
            total += g1 - gh;

        if (b1 < bl)
            total += bl - b1;

        if (b1 > bh)
            total += b1 - bh;
        
        return total;
    }
}
