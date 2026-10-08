package com.assignment.utils.imgUtils;

import java.awt.image.BufferedImage;

public class ResizeCropImg {
    public static BufferedImage resizeCropImg(BufferedImage input, int resizeFactor, int cropHeight, int cropWidth) {
        BufferedImage output;

        int originalWidth = input.getWidth();
        int originalHeight = input.getHeight();

        int newWidth;
        int newHeight;

        if (originalWidth < originalHeight) {
            newWidth = resizeFactor;
            newHeight = (int) Math.ceil((double) originalHeight * resizeFactor / originalWidth);
        } else {
            newHeight = resizeFactor;
            newWidth = (int) Math.ceil((double) originalWidth * resizeFactor / originalHeight);
        }

        output = ResizeImg.resizeImg(input, newHeight, newWidth);

        if (cropWidth > output.getWidth())
            cropWidth = output.getWidth();
        if (cropHeight > output.getHeight())
            cropHeight = output.getHeight();

        int x = (output.getWidth() - cropWidth) / 2;
        int y = (output.getHeight() - cropHeight) / 2;

        output = output.getSubimage(x, y, cropWidth, cropHeight);

        return (output);
    }
}
