package com.assignment.model_utils.model_runner;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtSession;

import java.nio.FloatBuffer;
import java.util.Collections;

public class ViTModelRunner implements ModelRunner<float []> {
    private static final String MODEL_PATH = "C:\\Users\\User\\Desktop\\Multimedia DBs & Image Proc\\Loading Transformers\\transformers\\models\\vit_onnx\\model.onnx";

    @Override
    public float[][] modelRunner(float[] data) {

        try {
            OrtEnvironment env = OrtEnvironment.getEnvironment();
            OrtSession.SessionOptions sessionOptions = new OrtSession.SessionOptions();
            OrtSession session = env.createSession(MODEL_PATH, sessionOptions);

            long[] dimensions = {1, 3, 224, 224};

            OnnxTensor tensor = OnnxTensor.createTensor(env, FloatBuffer.wrap(data), dimensions);

            OrtSession.Result result = session.run(Collections.singletonMap("pixel_values", tensor));

            float [][] output = (float[][]) result.get(0).getValue();

            result.close();
            tensor.close();
            session.close();

            return (output);

        } catch (Exception e) {
            System.out.println("Error running ONNX model! :(");
        }
        return null;
    }
}