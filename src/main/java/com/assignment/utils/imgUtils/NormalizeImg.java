package com.assignment.utils.imgUtils;

public class NormalizeImg {
    public static float[][][] normalizeImg(float[][][] pixels, float[] mean, float[] std) {
        int height = pixels.length;
        int width = pixels[0].length;

        float[][][] normalized = new float[height][width][3];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                for (int channel = 0; channel < 3; channel++) {

                    normalized[y][x][channel] = (pixels[y][x][channel] - mean[channel]) / std[channel];
                }
            }
        }

        return normalized;
    }
}
