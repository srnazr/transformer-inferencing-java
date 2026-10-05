package com.assignment.preprocessor_utils.preprocessors;

import com.assignment.utils.imgUtils.LoadImg;
import com.assignment.utils.imgUtils.NormalizeImg;
import com.assignment.utils.imgUtils.RescaleImg;
import com.assignment.utils.imgUtils.ResizeCropImg;
import com.assignment.utils.tensorUtils.AddBatchDim;
import com.assignment.utils.tensorUtils.FlattenData;
import com.assignment.utils.tensorUtils.HwcToChw;

import java.awt.image.BufferedImage;

public class ClipPreprocessor implements Preprocessors{
    @Override
    public float[] preprocess() {
        int resizeFactor = 224;

        int cropHeight = 224;
        int cropWidth = 224;

        float scaleFactor = 1F/255;

        float [][][] channels;

        float[] mean = {0.48145466F, 0.4578275F, 0.40821073F};
        float[] std = {0.26862954F, 0.26130258F, 0.27577711F};

        float[][][][] batch;

        float[] flattenedData;

        BufferedImage output = LoadImg.loadImg("C:\\Users\\User\\Desktop\\Multimedia DBs & Image Proc\\Loading Transformers\\transformers\\images\\dog.jpg");
        output = ResizeCropImg.resizeCropImg(output, resizeFactor, cropWidth, cropHeight);
        channels = RescaleImg.rescaleImg(output, scaleFactor);
        channels = NormalizeImg.normalizeImg(channels, mean, std);

        channels = HwcToChw.hwcToChw(channels);
        batch = AddBatchDim.addBatchDim(channels);
        flattenedData = FlattenData.flattenData(batch);

        return(flattenedData);
    }
}