package com.assignment.utils.tokenizerUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClipTokenizer {

    private static final long BOS_TOKEN = 49406;
    private static final long EOS_TOKEN = 49407;

    private static final int MAX_LENGTH = 77;

    private final Map<String, Integer> vocab;
    private final Map<String, Integer> rankedMerges;

    public ClipTokenizer() throws IOException {
        List<String> merges = LoadMerges.getMerges();

        rankedMerges = LoadMerges.loadMerges(merges);

        vocab = LoadVocab.loadVocab();
    }

    public long[] tokenize(String text) {
        text = normalize(text);

        List<String> words = ClipPreTokenizer.tokenize(text);

        List<Long> tokenIds = new ArrayList<>();

        tokenIds.add(BOS_TOKEN);

        for (String word : words) {

            List<String> byteLevelTokens = ByteLevel.encode(word);

            if (!byteLevelTokens.isEmpty()) {
                int last = byteLevelTokens.size() - 1;

                byteLevelTokens.set(last, byteLevelTokens.get(last) + "</w>");
            }

            List<String> bpeTokens = BPE.bpe(byteLevelTokens, rankedMerges);

            for (String token : bpeTokens) {

                Integer id = vocab.get(token);

                if (id == null) {
                    id = (int) EOS_TOKEN;
                }

                tokenIds.add((long) id);
            }
        }

        tokenIds.add(EOS_TOKEN);

        if (tokenIds.size() > MAX_LENGTH) {
            tokenIds = new ArrayList<>(tokenIds.subList(0, MAX_LENGTH));
            tokenIds.set(MAX_LENGTH - 1, EOS_TOKEN);
        }

        while (tokenIds.size() < MAX_LENGTH) {
            tokenIds.add(EOS_TOKEN);
        }

        long[] result = new long[MAX_LENGTH];

        for (int i = 0; i < MAX_LENGTH; i++) {
            result[i] = tokenIds.get(i);
        }

        return result;
    }

    private String normalize(String text) {
        text = java.text.Normalizer.normalize(text, java.text.Normalizer.Form.NFC);

        text = text.replaceAll("\\s+", " ");

        text = text.toLowerCase(java.util.Locale.ROOT);

        return text;
    }
}