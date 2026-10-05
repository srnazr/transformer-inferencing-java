package com.assignment.preprocessor_utils.preprocessors;

import com.assignment.utils.imgUtils.LoadImg;
import com.assignment.utils.imgUtils.NormalizeImg;
import com.assignment.utils.imgUtils.RescaleImg;
import com.assignment.utils.imgUtils.ResizeImg;
import com.assignment.utils.tensorUtils.AddBatchDim;
import com.assignment.utils.tensorUtils.FlattenData;
import com.assignment.utils.tensorUtils.HwcToChw;

import java.awt.image.BufferedImage;

public class ViTPreprocessor implements Preprocessors {
    @Override
    public float[] preprocess() {
        int height = 224;
        int width = 224;

        float scaleFactor = 1F/255;

        float[][][] channels;

        float[] mean = {0.5F, 0.5F, 0.5F};
        float[] std = {0.5F, 0.5F, 0.5F};

        float[][][][] batch;

        float[] flattenedData;

        BufferedImage output = LoadImg.loadImg("C:\\Users\\User\\Desktop\\Multimedia DBs & Image Proc\\Loading Transformers\\transformers\\images\\cat.jpg");
        output = ResizeImg.resizeImg(output, height, width);
        channels = RescaleImg.rescaleImg(output, scaleFactor);
        channels = NormalizeImg.normalizeImg(channels, mean, std);

        channels = HwcToChw.hwcToChw(channels);
        batch = AddBatchDim.addBatchDim(channels);
        flattenedData = FlattenData.flattenData(batch);

        return(flattenedData);
    }
}
