package com.assignment.utils.imgUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ResizeImg {
    public static BufferedImage resizeImg(BufferedImage input, int height, int width) {
        BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = newImg.createGraphics();
        graphics2D.drawImage(input,0,0, width, height, null);
        graphics2D.dispose();
        return (newImg);
    }
}
