package com.assignment.model_utils.model_runner;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtSession;

import java.nio.FloatBuffer;

public class VLMVisionEncoder {

    private static final String MODEL_PATH =
            "...\\models\\smolvlm_onnx\\onnx\\vision_encoder.onnx";

    public float[][][] encode(float[] imageData) {
        try {

            OrtEnvironment env = OrtEnvironment.getEnvironment();
            OrtSession.SessionOptions options = new OrtSession.SessionOptions();
            OrtSession session = env.createSession(MODEL_PATH, options);

            long[] imageDimensions = {1, 3, 512, 512};

            OnnxTensor pixelTensor = OnnxTensor.createTensor(env, FloatBuffer.wrap(imageData), imageDimensions);

            boolean[][] attentionMask = new boolean[1][1];

            attentionMask[0][0] = true;

            OnnxTensor maskTensor = OnnxTensor.createTensor(env, attentionMask);

            OrtSession.Result result = session.run(java.util.Map.of("pixel_values", pixelTensor, "pixel_attention_mask", maskTensor));

            Object value = result.get("image_features").get().getValue();

            result.close();
            pixelTensor.close();
            maskTensor.close();
            session.close();

            return (float[][][]) value;

        } catch (Exception e) {
            System.out.println("Error running VLM vision encoder! :(");
            e.printStackTrace();
            return null;
        }
    }
}