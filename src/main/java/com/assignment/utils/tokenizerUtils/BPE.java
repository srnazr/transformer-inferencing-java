package com.assignment.utils.tokenizerUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BPE {
    public static List<String> bpe(List<String> symbols, Map<String, Integer> rankedMerges) {

        List<String> current = new ArrayList<>(symbols);

        while (current.size() > 1) {

            int bestIndex = -1;
            int bestRank = Integer.MAX_VALUE;

            for (int i = 0; i < current.size() - 1; i++) {

                String pair = current.get(i) + " " + current.get(i + 1);

                Integer rank = rankedMerges.get(pair);

                if (rank != null && rank < bestRank) {
                    bestRank = rank;
                    bestIndex = i;
                }
            }
            if (bestIndex == -1)
                break;

            String merged = current.get(bestIndex) + current.get(bestIndex + 1);

            current.set(bestIndex, merged);
            current.remove(bestIndex + 1);
        }
        return current;
    }
}