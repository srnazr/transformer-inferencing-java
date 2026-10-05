package com.assignment.outputs.output_interpreter;

public class ClipInterpreter implements OutputInterpreter {

    @Override
    public void interpret(float[][] output) {

        if (output == null || output.length == 0) {
            System.out.println("No CLIP output received!");
            return;
        }

        float[] imageEmbedding = output[0];

        System.out.println("==== CLIP IMAGE EMBEDDING ====");
        System.out.println("Embedding dimensions: " + imageEmbedding.length);

        System.out.println("First 10 values:");

        int valuesToPrint = Math.min(10, imageEmbedding.length);

        for (int i = 0; i < valuesToPrint; i++) {
            System.out.println(imageEmbedding[i]);
        }
    }
}