package com.assignment.preprocessor_utils.preprocessorsFactory;

import com.assignment.preprocessor_utils.preprocessors.ClipPreprocessor;
import com.assignment.preprocessor_utils.preprocessors.Preprocessors;
import com.assignment.preprocessor_utils.preprocessors.VLMPreprocessor;
import com.assignment.preprocessor_utils.preprocessors.ViTPreprocessor;

public class PreprocessorsFactory {
    public static Preprocessors create(String type) {
        if ("vit".equals(type))
            return (new ViTPreprocessor());
        else if ("clip".equals(type))
            return (new ClipPreprocessor());
        else if ("vlm".equals(type))
            return (new VLMPreprocessor());
        else
            return (null);
    }
}
