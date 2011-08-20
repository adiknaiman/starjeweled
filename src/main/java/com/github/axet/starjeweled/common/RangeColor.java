package com.github.axet.starjeweled.common;

public class RangeColor {
    public int min;
    public int max;

    public RangeColor(RangeColor range) {
        this.min = range.min;
        this.max = range.max;
    }

    public RangeColor(int min, int max) {
        this.min = min;
        this.max = max;
    }

    /**
     * if rgb1 > rgb2 return true
     * 
     * @param rgb1
     * @param rgb2
     * @return
     */
    public static boolean isGr(int rgb1, int rgb2) {
        int r1 = rgb1 & 0xff0000;
        int g1 = rgb1 & 0x00ff00;
        int b1 = rgb1 & 0x0000ff;

        int r2 = rgb2 & 0xff0000;
        int g2 = rgb2 & 0x00ff00;
        int b2 = rgb2 & 0x0000ff;

        return (r1 > r2) || (g1 > g2) || (b1 > b2);
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
        int r1 = (rgb & 0xff0000) >> 16;
        int g1 = (rgb & 0x00ff00) >> 8;
        int b1 = (rgb & 0x0000ff) >> 0;

        int rl = (min & 0xff0000) >> 16;
        int gl = (min & 0x00ff00) >> 8;
        int bl = (min & 0x0000ff) >> 0;

        int rh = (max & 0xff0000) >> 16;
        int gh = (max & 0x00ff00) >> 8;
        int bh = (max & 0x0000ff) >> 0;

        if ((r1 >= rl && r1 <= rh) && (g1 >= gl && g1 <= gh) && (b1 >= bl && b1 <= bh))
            return 0;

        int total = 0;

        if (r1 < rl)
            total = rl - r1;

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

    public void merge(int rgb) {
        int r1 = (rgb & 0xff0000) >> 16;
        int g1 = (rgb & 0x00ff00) >> 8;
        int b1 = (rgb & 0x0000ff) >> 0;

        int rl = (min & 0xff0000) >> 16;
        int gl = (min & 0x00ff00) >> 8;
        int bl = (min & 0x0000ff) >> 0;

        int rh = (max & 0xff0000) >> 16;
        int gh = (max & 0x00ff00) >> 8;
        int bh = (max & 0x0000ff) >> 0;

        rl = Math.min(rl, r1);
        gl = Math.min(gl, g1);
        bl = Math.min(bl, b1);

        rh = Math.max(rh, r1);
        gh = Math.max(gh, g1);
        bh = Math.max(bh, b1);

        min = (rl << 16) | (gl << 8) | (bl);
        max = (rh << 16) | (gh << 8) | (bh);
    }

    int av(int l, int h) {
        return l + (h - l) / 2;
    }

    public int average() {
        int rl = (min & 0xff0000) >> 16;
        int gl = (min & 0x00ff00) >> 8;
        int bl = (min & 0x0000ff) >> 0;

        int rh = (max & 0xff0000) >> 16;
        int gh = (max & 0x00ff00) >> 8;
        int bh = (max & 0x0000ff) >> 0;

        return (av(rl, rh) << 16) | (av(gl, gh) << 8) | (av(bl, bh));
    }
}
