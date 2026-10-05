package com.assignment.utils.tokenizerUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClipPreTokenizer {

    private static final Pattern PATTERN = Pattern.compile(
            "<\\|startoftext\\|>" +
                    "|<\\|endoftext\\|>" +
                    "|'s|'t|'re|'ve|'m|'ll|'d" +
                    "|[\\p{L}]+" +
                    "|[\\p{N}]" +
                    "|[^\\s\\p{L}\\p{N}]+"
    );

    public static List<String> tokenize(String text) {

        List<String> tokens = new ArrayList<>();

        Matcher matcher = PATTERN.matcher(text);

        while (matcher.find()) {
            tokens.add(matcher.group());
        }

        return tokens;
    }
}