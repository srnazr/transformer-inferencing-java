package com.assignment.model_utils.model_runner;

import ai.onnxruntime.*;

import java.nio.FloatBuffer;
import java.nio.LongBuffer;
import java.util.*;

public class VLMModelRunner implements ModelRunner<VLMInput> {
    private static final String MODEL_PATH = "...\\models\\smolvlm_onnx\\onnx\\decoder_model_merged.onnx";

    private static final int IMAGE_TOKEN_ID = 49190;
    private static final int EOS_TOKEN_ID = 49279;

    private static final int NUM_KV_HEADS = 3;
    private static final int HEAD_DIM = 64;
    private static final int NUM_LAYERS = 30;

    private static final int MAX_NEW_TOKENS = 100;

    private final VLMVisionEncoder visionEncoder;
    private final VLMEmbedTokens embedTokens;

    public VLMModelRunner() {
        visionEncoder = new VLMVisionEncoder();
        embedTokens = new VLMEmbedTokens();
    }

    @Override
    public float[][] modelRunner(VLMInput data) {
        try {
            OrtEnvironment env = OrtEnvironment.getEnvironment();
            OrtSession.SessionOptions options = new OrtSession.SessionOptions();

            OrtSession decoder = env.createSession(MODEL_PATH, options);

            long[] inputIds = data.getInputIds();

            float[][][] inputEmbeddings = embedTokens.embed(inputIds);

            float[][][] imageFeatures = visionEncoder.encode(data.getImageData());

            mergeImageEmbeddings(inputIds, inputEmbeddings, imageFeatures);

            long[][] attentionMask = new long[1][inputIds.length];

            Arrays.fill(attentionMask[0], 1L);

            long[][] positionIds = new long[1][inputIds.length];

            for (int i = 0; i < inputIds.length; i++) {
                positionIds[0][i] = i;
            }

            Map<String, OnnxTensor> pastKeyValues = createEmptyKVCache(env);

            List<Long> generatedTokens = new ArrayList<>();

            float[][][] currentEmbeddings = inputEmbeddings;

            for (int step = 0; step < MAX_NEW_TOKENS; step++) {

                Map<String, OnnxTensor> decoderInputs = new HashMap<>();

                decoderInputs.put("inputs_embeds", createFloatTensor(env, currentEmbeddings));

                decoderInputs.put("attention_mask", createLongTensor(env, attentionMask));

                decoderInputs.put("position_ids", createLongTensor(env, positionIds));

                decoderInputs.putAll(pastKeyValues);

                OrtSession.Result result = decoder.run(decoderInputs);

                float[][][] logits = (float[][][]) result.get("logits").get().getValue();

                int nextToken = argmax(logits[0][logits[0].length - 1]);

                generatedTokens.add((long) nextToken);

                updateKVCache(result, pastKeyValues);

                result.close();

                if (nextToken == EOS_TOKEN_ID) {
                    break;
                }

                long[] nextInput = {nextToken};

                float[][][] nextEmbedding = embedTokens.embed(nextInput);

                currentEmbeddings = nextEmbedding;

                attentionMask = new long[][]{{1L}};

                positionIds = new long[][]{{positionIds[0][positionIds[0].length - 1] + 1}};
            }

            decoder.close();

            float[][] output = new float[1][generatedTokens.size()];

            for (int i = 0; i < generatedTokens.size(); i++) {
                output[0][i] = generatedTokens.get(i).floatValue();
            }

            return output;

        } catch (Exception e) {
            System.out.println("Error running SmolVLM! :(");
            e.printStackTrace();

            return null;
        }
    }

    private void mergeImageEmbeddings(long[] inputIds, float[][][] textEmbeddings, float[][][] imageFeatures) {
        int imageIndex = 0;

        for (int i = 0; i < inputIds.length; i++) {
            if (inputIds[i] == IMAGE_TOKEN_ID) {

                if (imageIndex >= imageFeatures[0].length) {
                    break;
                }

                textEmbeddings[0][i] = imageFeatures[0][imageIndex];

                imageIndex++;
            }
        }
    }

    private int argmax(float[] logits) {

        int bestIndex = 0;
        float bestValue = logits[0];

        for (int i = 1; i < logits.length; i++) {
            if (logits[i] > bestValue) {
                bestValue = logits[i];
                bestIndex = i;
            }
        }

        return bestIndex;
    }

    private Map<String, OnnxTensor> createEmptyKVCache(OrtEnvironment env) throws OrtException {

        Map<String, OnnxTensor> cache = new HashMap<>();

        for (int layer = 0; layer < NUM_LAYERS; layer++) {

            for (String type : new String[]{"key", "value"}) {
                String name = "past_key_values." + layer + "." + type;

                long[] shape = {1, NUM_KV_HEADS, 0, HEAD_DIM};

                cache.put(name, OnnxTensor.createTensor(env, FloatBuffer.wrap(new float[0]), shape));
            }
        }

        return cache;
    }

    private OnnxTensor createFloatTensor(
            OrtEnvironment env,
            float[][][] data
    ) throws OrtException {

        int sequenceLength =
                data[0].length;

        int hiddenSize =
                data[0][0].length;

        float[] flat =
                new float[
                        sequenceLength
                                * hiddenSize
                        ];

        int index = 0;

        for (int i = 0;
             i < sequenceLength;
             i++) {

            for (int j = 0;
                 j < hiddenSize;
                 j++) {

                flat[index++] =
                        data[0][i][j];
            }
        }

        long[] shape = {
                1,
                sequenceLength,
                hiddenSize
        };

        return OnnxTensor.createTensor(
                env,
                java.nio.FloatBuffer.wrap(flat),
                shape
        );
    }

    private OnnxTensor createLongTensor(
            OrtEnvironment env,
            long[][] data
    ) throws OrtException {

        int rows =
                data.length;

        int cols =
                data[0].length;

        long[] flat =
                new long[rows * cols];

        int index = 0;

        for (long[] row : data) {

            for (long value : row) {
                flat[index++] = value;
            }
        }

        long[] shape = {
                rows,
                cols
        };

        return OnnxTensor.createTensor(
                env,
                LongBuffer.wrap(flat),
                shape
        );
    }

    private void updateKVCache(
            OrtSession.Result result,
            Map<String, OnnxTensor> cache
    ) throws OrtException {

        for (int layer = 0;
             layer < NUM_LAYERS;
             layer++) {

            for (String type :
                    new String[]{"key", "value"}) {

                String name =
                        "present."
                                + layer
                                + "."
                                + type;

                OnnxValue value =
                        result.get(name);

                if (value != null) {

                    OnnxTensor tensor =
                            (OnnxTensor) value;

                    OnnxTensor old =
                            cache.put(
                                    name.replace(
                                            "present",
                                            "past_key_values"
                                    ),
                                    tensor
                            );

                    if (old != null) {
                        old.close();
                    }
                }
            }
        }
    }
}