package com.assignment.model_utils.model_runner;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtSession;

import java.nio.LongBuffer;
import java.util.Collections;

public class VLMEmbedTokens {

    private static final String MODEL_PATH =
            "...\\models\\smolvlm_onnx\\onnx\\embed_tokens.onnx";

    public float[][][] embed(long[] inputIds) {
        try {
            OrtEnvironment env = OrtEnvironment.getEnvironment();

            OrtSession.SessionOptions options = new OrtSession.SessionOptions();

            OrtSession session = env.createSession(MODEL_PATH, options);

            long[] dimensions = {1, inputIds.length};

            OnnxTensor tensor = OnnxTensor.createTensor(env, LongBuffer.wrap(inputIds), dimensions);

            OrtSession.Result result = session.run(Collections.singletonMap("input_ids", tensor));

            float[][][] embeddings = (float[][][]) result.get(0).getValue();

            result.close();
            tensor.close();
            session.close();

            return embeddings;

        } catch (Exception e) {
            System.out.println("Error running VLM embedding model! :(");
            e.printStackTrace();

            return null;
        }
    }
}