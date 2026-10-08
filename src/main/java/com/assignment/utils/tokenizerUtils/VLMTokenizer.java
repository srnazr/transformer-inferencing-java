package com.assignment.utils.tokenizerUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VLMTokenizer {

    private static final int IMAGE_TOKEN_ID = 49190;
    private static final int EOS_TOKEN_ID = 49279;

    private static final int BOS_TOKEN_ID = 0;
    private static final int MAX_LENGTH = 2048;

    private final Map<String, Integer> vocab;
    private final Map<String, Integer> specialTokens;

    public VLMTokenizer() throws IOException {

        ObjectMapper mapper =
                new ObjectMapper();

        File vocabFile = new File("...\\models\\smolvlm_onnx\\vocab.json");

        vocab = mapper.readValue(vocabFile, new TypeReference<Map<String, Integer>>() {});

        specialTokens = new HashMap<>();

        specialTokens.put("<image>", IMAGE_TOKEN_ID);

        specialTokens.put("<end_of_utterance>", EOS_TOKEN_ID);

        specialTokens.put("<|im_start|>", BOS_TOKEN_ID);
    }

    public long[] tokenize(String prompt) {
        List<Long> ids = new ArrayList<>();

        String[] parts = prompt.split("(<image>|<end_of_utterance>|<\\|im_start\\|>)", -1);

        int currentIndex = 0;

        while (currentIndex < parts.length) {
            String part = parts[currentIndex];

            if (!part.isEmpty()) {
                tokenizeNormalText(part, ids);
            }

            if (currentIndex < parts.length - 1) {
                String specialToken = findSpecialTokenBetween(prompt, parts, currentIndex);

                if (specialToken != null) {
                    Integer id = specialTokens.get(specialToken);

                    if (id != null) {
                        ids.add((long) id);
                    }
                }
            }

            currentIndex++;
        }

        if (ids.isEmpty() || ids.get(ids.size() - 1) != EOS_TOKEN_ID) {
            ids.add((long) EOS_TOKEN_ID);
        }

        if (ids.size() > MAX_LENGTH) {
            ids = new ArrayList<>(ids.subList(0, MAX_LENGTH));
        }

        long[] result = new long[ids.size()];

        for (int i = 0; i < ids.size(); i++) {
            result[i] = ids.get(i);
        }

        return result;
    }

    private void tokenizeNormalText(String text, List<Long> output) {
        text = Normalizer.normalize(text, Normalizer.Form.NFC);

        text = text.toLowerCase();

        String[] words = text.trim().split("\\s+");

        for (String word : words) {
            Integer id = vocab.get(word);

            if (id != null) {
                output.add((long) id);
            }
        }
    }

    private String findSpecialTokenBetween(
            String prompt,
            String[] parts,
            int index
    ) {

        String[] tokens = {
                "<image>",
                "<end_of_utterance>",
                "<|im_start|>"
        };

        int currentPosition = 0;

        for (int i = 0; i <= index; i++) {
            currentPosition += parts[i].length();

            if (i == index) {
                for (String token : tokens) {
                    if (prompt.startsWith(token, currentPosition)) {
                        return token;
                    }
                }
            }

            if (i < index) {
                for (String token : tokens) {
                    if (prompt.startsWith(token, currentPosition)) {
                        currentPosition += token.length();
                        break;
                    }
                }
            }
        }

        return null;
    }
}