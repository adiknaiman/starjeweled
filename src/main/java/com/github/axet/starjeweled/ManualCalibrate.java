package com.github.axet.starjeweled;

import java.awt.image.BufferedImage;

import com.github.axet.starjeweled.core.Capture;

public class ManualCalibrate {

    public static void main(String[] args) {
    	Capture capture = new Capture();
        BufferedImage desktopImage;
        desktopImage = capture.capture();
        capture.save(desktopImage, "calibrate.png");
    }
    
}
