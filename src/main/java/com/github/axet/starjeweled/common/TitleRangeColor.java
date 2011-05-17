package com.github.axet.starjeweled.common;

public class TitleRangeColor extends RangeColor {

    public String title;

    public TitleRangeColor(int min, int max, String title) {
        super(min, max);

        this.title = title;
    }

}
