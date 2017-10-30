package com.icthh.xm.lep.script;

import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Scanner;

/**
 * The {@link StringUtils} class.
 */
public final class StringUtils {

    private static final String NEW_LINE_MARKER = "\\n";

    /**
     * Generates text preview.
     *
     * @param text             original text
     * @param linesCount       preview lines count
     * @param ignoreEmptyLines flag to ignore blank lines while counting preview
     * @param maxLength        max char length for preview (can be  {@code null} if this restriction is not needed)
     * @return text preview string
     */
    public static String getPreview(String text, int linesCount, boolean ignoreEmptyLines, Integer maxLength) {
        if (maxLength != null && maxLength < 0) {
            throw new IllegalArgumentException("maxLength can't be negative");
        }

        if (text == null) {
            return "<null>";
        }

        if (text.isEmpty() || linesCount <= 0) {
            return "";
        }

        Scanner scanner = new Scanner(text);
        return getPreview(linesCount, ignoreEmptyLines, maxLength, scanner);
    }

    /**
     * Generates text preview.
     *
     * @param is               original text input stream
     * @param encoding         input stream encoding
     * @param linesCount       preview lines count
     * @param ignoreEmptyLines flag to ignore blank lines while counting preview
     * @param maxLength        max char length for preview (can be  {@code null} if this restriction is not needed)
     * @return text preview string
     */
    public static String getPreview(InputStream is, String encoding, int linesCount, boolean ignoreEmptyLines,
                                    Integer maxLength) {
        if (maxLength != null && maxLength < 0) {
            throw new IllegalArgumentException("maxLength can't be negative");
        }

        if (is == null) {
            return "<null>";
        }

        if (linesCount <= 0) {
            return "";
        }

        ReadableByteChannel readableChannel = Channels.newChannel(is);
        Scanner scanner = new Scanner(readableChannel, encoding);
        return getPreview(linesCount, ignoreEmptyLines, maxLength, scanner);
    }

    private static String getPreview(int linesCount, boolean ignoreEmptyLines, Integer maxLength, Scanner scanner) {
        StringBuilder buffer = new StringBuilder();
        for (int lineNumber = 0; scanner.hasNextLine() && lineNumber < linesCount; ) {
            if (maxLength != null && buffer.length() > 0 && buffer.length() + NEW_LINE_MARKER.length() >= maxLength) {
                break;
            }

            String line = scanner.nextLine();

            // append new line marker (prevent using new line symbol in preview)
            if (buffer.length() > 0) {
                buffer.append(NEW_LINE_MARKER);
            }
            buffer.append(line);

            // inc new line counter
            if (ignoreEmptyLines) {
                // if is not blank line then inc new line counter
                if (line != null && !line.trim().isEmpty()) {
                    lineNumber++;
                }
            } else {
                lineNumber++;
            }
        }
        scanner.close();

        String preview = buffer.toString();

        // trim to max length
        if (maxLength != null) {
            preview = preview.substring(0, Math.min(preview.length(), maxLength));
        }
        return preview;
    }


    /**
     * Utils class private constructor to permit direct class instantiation.
     */
    private StringUtils() {
        throw new IllegalAccessError("Utils class constructor not permitted to call");
    }

}
