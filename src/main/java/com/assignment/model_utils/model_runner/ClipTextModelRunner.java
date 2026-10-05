package com.assignment.model_utils.model_runner;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtSession;

import java.nio.LongBuffer;
import java.util.Collections;

public class ClipTextModelRunner implements ModelRunner<long[]> {

    private static final String MODEL_PATH = "C:\\Users\\User\\Desktop\\Multimedia DBs & Image Proc\\Loading Transformers\\transformers\\models\\clip_onnx\\onnx\\text_model.onnx";

    @Override
    public float[][] modelRunner(long[] data) {
        try {
            OrtEnvironment env = OrtEnvironment.getEnvironment();

            OrtSession.SessionOptions sessionOptions = new OrtSession.SessionOptions();

            OrtSession session = env.createSession(MODEL_PATH, sessionOptions);

            long[] dimensions = {1, 77};

            OnnxTensor tensor = OnnxTensor.createTensor(env, LongBuffer.wrap(data), dimensions);

            OrtSession.Result result = session.run(Collections.singletonMap("input_ids", tensor));

            float[][] output = (float[][]) result.get(0).getValue();

            result.close();
            tensor.close();
            session.close();

            return output;

        } catch (Exception e) {
            System.out.println("Error running CLIP text ONNX model! :(");

            e.printStackTrace();

            return null;
        }
    }
}