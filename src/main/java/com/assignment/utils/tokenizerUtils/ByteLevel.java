package com.assignment.utils.tokenizerUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ByteLevel {

    private static final char[] BYTE_TO_UNICODE =
            createByteToUnicode();

    private static char[] createByteToUnicode() {

        List<Integer> bytes = new ArrayList<>();
        List<Integer> unicode = new ArrayList<>();

        for (int i = 33; i <= 126; i++) {
            bytes.add(i);
            unicode.add(i);
        }

        for (int i = 161; i <= 172; i++) {
            bytes.add(i);
            unicode.add(i);
        }

        for (int i = 174; i <= 255; i++) {
            bytes.add(i);
            unicode.add(i);
        }

        int extra = 0;

        for (int i = 0; i < 256; i++) {

            if (!bytes.contains(i)) {
                bytes.add(i);
                unicode.add(256 + extra);
                extra++;
            }
        }

        char[] mapping = new char[256];

        for (int i = 0; i < bytes.size(); i++) {
            mapping[bytes.get(i)] =
                    (char) unicode.get(i).intValue();
        }

        return mapping;
    }

    public static List<String> encode(String text) {

        byte[] bytes =
                text.getBytes(StandardCharsets.UTF_8);

        List<String> output = new ArrayList<>();

        for (byte value : bytes) {

            int unsignedByte = value & 0xFF;

            output.add(
                    String.valueOf(
                            BYTE_TO_UNICODE[unsignedByte]
                    )
            );
        }

        return output;
    }
}