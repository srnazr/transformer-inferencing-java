package com.assignment.model_utils.model_runner;

public class ClipInput {

    private final float[] imageData;
    private final long[] textTokenIds;

    public ClipInput(float[] imageData, long[] textTokenIds) {
        this.imageData = imageData;
        this.textTokenIds = textTokenIds;
    }

    public float[] getImageData() {
        return imageData;
    }

    public long[] getTextTokenIds() {
        return textTokenIds;
    }
}