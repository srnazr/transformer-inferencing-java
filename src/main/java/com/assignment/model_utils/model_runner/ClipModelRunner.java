package com.assignment.model_utils.model_runner;

import com.assignment.utils.gen.ClipSimilarity;

public class ClipModelRunner implements ModelRunner<ClipInput> {

    private final ClipVisionModelRunner visionRunner;
    private final ClipTextModelRunner textRunner;

    public ClipModelRunner() {
        visionRunner = new ClipVisionModelRunner();
        textRunner = new ClipTextModelRunner();
    }

    @Override
    public float[][] modelRunner(ClipInput data) {
        float[][] imageOutput = visionRunner.modelRunner(data.getImageData());
        float[][] textOutput = textRunner.modelRunner(data.getTextTokenIds());

        float[] imageEmbedding = imageOutput[0];
        float[] textEmbedding = textOutput[0];

        double similarity = ClipSimilarity.cosineSimilarity(imageEmbedding, textEmbedding);

        System.out.println("Similarity: " + similarity);

        return new float[][] {imageEmbedding, textEmbedding};
    }
}