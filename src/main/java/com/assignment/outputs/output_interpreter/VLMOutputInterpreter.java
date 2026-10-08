package com.assignment.outputs.output_interpreter;

import com.assignment.utils.tokenizerUtils.LoadVocab;

import java.util.Map;

public class VLMOutputInterpreter
        implements OutputInterpreter {

    private static final int EOS_TOKEN_ID =
            49279;

    private final Map<String, Integer> vocab;

    public VLMOutputInterpreter() {

        vocab =
                LoadVocab.loadVocab();
    }

    @Override
    public void interpret(
            float[][] output
    ) {

        if (output == null
                || output.length == 0) {

            System.out.println(
                    "No VLM output received!"
            );

            return;
        }

        System.out.println(
                "==== SmolVLM OUTPUT ===="
        );

        StringBuilder text =
                new StringBuilder();

        for (float value :
                output[0]) {

            int tokenId =
                    (int) value;

            if (tokenId == EOS_TOKEN_ID) {
                break;
            }

            String token =
                    findToken(
                            tokenId
                    );

            if (token != null) {
                text.append(token);
            }
        }

        System.out.println(
                text.toString()
        );
    }

    private String findToken(
            int tokenId
    ) {

        for (Map.Entry<String, Integer> entry :
                vocab.entrySet()) {

            if (entry.getValue() == tokenId) {
                return entry.getKey();
            }
        }

        return null;
    }
}