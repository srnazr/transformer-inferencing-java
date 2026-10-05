package com.assignment.utils.tensorUtils;

public class AddBatchDim {
    public static float[][][][] addBatchDim(float [][][] input) {
        int channelNum = input.length;
        int height = input[0].length;
        int width = input[0][0].length;

        float[][][][] output = new float[1][channelNum][height][width];

        // we are adding the batch number = number of images to be processed
        // since we are only processing 1 img so bach num = 1 & i = 0
        // for (int i = 0; i < batchSize; i++) --> index output as output[i][j][k][l]
        for (int i = 0; i < channelNum; i++) {
            for (int j = 0; j < height; j++) {
                for (int k = 0; k < width; k++) {
                    output[0][i][j][k] = input[i][j][k];
                }
            }
        }

        return (output);
    }
}
