package com.assignment.utils.imgUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LoadImg {
    public static BufferedImage loadImg(String path) {
        BufferedImage newImg = null;
        try {
            File newFile = new File(path);
            newImg = ImageIO.read(newFile);
            System.out.println("Image loaded successfully!");
            return (newImg);
        }
        catch (IOException e) {
            System.out.println("Invalid Image Path! :(");
            return(null);
        }
    }
}
