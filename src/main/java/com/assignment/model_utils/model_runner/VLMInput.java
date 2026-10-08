package com.assignment.model_utils.model_runner;

public class VLMInput {

    private final float[] imageData;
    private final long[] inputIds;

    public VLMInput(
            float[] imageData,
            long[] inputIds
    ) {
        this.imageData = imageData;
        this.inputIds = inputIds;
    }

    public float[] getImageData() {
        return imageData;
    }

    public long[] getInputIds() {
        return inputIds;
    }
}