package com.assignment.utils.gen;

public class ClipSimilarity {

    public static double cosineSimilarity(
            float[] imageEmbedding,
            float[] textEmbedding) {

        if (imageEmbedding.length != textEmbedding.length) {
            throw new IllegalArgumentException(
                    "Embeddings must have the same dimensions! :("
            );
        }

        double dotProduct = 0.0;
        double imageMagnitude = 0.0;
        double textMagnitude = 0.0;

        for (int i = 0; i < imageEmbedding.length; i++) {

            dotProduct += (double) imageEmbedding[i] * textEmbedding[i];

            imageMagnitude += (double) imageEmbedding[i] * imageEmbedding[i];

            textMagnitude += (double) textEmbedding[i] * textEmbedding[i];
        }

        if (imageMagnitude == 0.0 || textMagnitude == 0.0) {
            return 0.0;
        }

        return dotProduct / (Math.sqrt(imageMagnitude) * Math.sqrt(textMagnitude));
    }
}