package com.assignment.utils.tensorUtils;

public class FlattenData {
    public static float[] flattenData(float[][][][] input) {
        int batch = input.length;
        int channels = input[0].length;
        int height = input[0][0].length;
        int width = input[0][0][0].length;

        long size = batch * channels * height * width;

        float[] flattenedData = new float[Math.toIntExact(size)];

        int idx = 0;

        for (int i = 0; i < channels; i++) {
            for (int j = 0; j < height; j++) {
                for (int k = 0; k < width; k++) {
                    flattenedData[idx] = input[0][i][j][k];
                    idx++;
                }
            }
        }

        return (flattenedData);
    }
}
