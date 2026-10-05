package com.assignment.utils.tensorUtils;

public class HwcToChw {
    public static float[][][] hwcToChw(float[][][] channels){
        int height = channels.length;
        int width = channels[0].length;
        int channelNum = channels[0][0].length;

        float[][][] output = new float[channelNum][height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                for (int k = 0; k < channelNum; k++) {
                    output[k][i][j] = channels[i][j][k];
                }
            }
        }

        return (output);
    }
}
