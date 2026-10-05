package com.assignment.utils.imgUtils;

import java.awt.image.BufferedImage;

public class RescaleImg {
    public static float[][][] rescaleImg(BufferedImage input, float scaleFactor) {
        int width = input.getWidth();
        int height = input.getHeight();

        float[][][] pixels = new float[height][width][3];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                int pixel = input.getRGB(x, y);

                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8) & 0xFF;
                int b = pixel & 0xFF;

                pixels[y][x][0] = r * scaleFactor;
                pixels[y][x][1] = g * scaleFactor;
                pixels[y][x][2] = b * scaleFactor;
            }
        }

        return pixels;
    }
}
