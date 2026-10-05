package com.assignment.outputs.output_interpreter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

public class ViTInterpreter implements OutputInterpreter{
    private static final String CONFIG_PATH = "C:\\Users\\User\\Desktop\\Multimedia DBs & Image Proc\\Loading Transformers\\transformers\\models\\vit_onnx\\config.json";

    @Override
    public void interpret(float[][] output) {
        float[] logits = output[0];

        int highestIdx = 0;
        float highestLogit = logits[0];

        for (int i = 1; i < logits.length; i++) {
            if (logits[i] > highestLogit) {
                highestLogit = logits[i];
                highestIdx = i;
            }
        }

        double[] probabilities = new double[logits.length];
        double sum = 0.0;

        for (int i = 0; i < logits.length; i++) {
            probabilities[i] = Math.exp(logits[i] - highestLogit);
            sum += probabilities[i];
        }

        for (int i = 0; i < logits.length; i++) {
            probabilities[i] /= sum;
        }

        double confidence = probabilities[highestIdx];

        String label = getLabel(highestIdx);

        System.out.println("==== PREDICTION ====");
        System.out.println("Class index: " + highestIdx);
        System.out.println("Label: " + label);
        System.out.printf("Confidence: %.2f%%%n", confidence * 100);
    }

    private String getLabel(int idx) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            JsonNode config = mapper.readTree(new File(CONFIG_PATH));

            JsonNode id2label = config.get("id2label");

            return id2label.get(String.valueOf(idx)).asText();

        } catch (Exception e) {
            System.out.println("Error reading config.json! :(");

            return "Unknown";
        }
    }
}
