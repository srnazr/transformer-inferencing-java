package com.assignment.utils.tokenizerUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadMerges {
    public static List<String> getMerges() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File tokenConfig = new File("C:\\Users\\User\\Desktop\\Multimedia DBs & Image Proc\\Loading Transformers\\transformers\\models\\clip_onnx\\tokenizer.json");

        JsonNode rootNode = mapper.readTree(tokenConfig);

        JsonNode mergesNode = rootNode.get("model").get("merges");

        return mapper.convertValue(
                mergesNode,
                new TypeReference<List<String>>() {}
        );
    }

    public static Map<String, Integer> loadMerges(List<String> merges) {
        Map<String, Integer> rankedMerges = new HashMap<>();

        for (int i = 0; i < merges.size(); i++)
            rankedMerges.put(merges.get(i), i);

        return rankedMerges;
    }
}