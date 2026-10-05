package com.assignment.utils.tokenizerUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class LoadVocab {
    public static Map<String, Integer> loadVocab() {
        ObjectMapper mapper = new ObjectMapper();

        File vocab = new File("C:\\Users\\User\\Desktop\\Multimedia DBs & Image Proc\\Loading Transformers\\transformers\\models\\clip_onnx\\vocab.json");

        try {
            Map<String, Integer> vocabMap =
                    mapper.readValue(
                            vocab,
                            new TypeReference<Map<String, Integer>>() {}
                    );
            return vocabMap;
        }
        catch (IOException e) {
            System.out.println("Error loading vocab! :(");
        }
        return Map.of();
    }
}