package com.icthh.xm.lep.script;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * The {@link StringUtilsTest} class.
 */
public class StringUtilsTest {

    private static String getResourceText(String name) throws IOException {
        InputStream input = StringUtilsTest.class.getResourceAsStream(name);
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        }
    }

    @Test
    public void test() throws IOException {
        String text = getResourceText("Script.groovy");
        String preview = StringUtils.getPreview(text, 2, true, Integer.MAX_VALUE);
        assertEquals("package com.icthh.xm.lep.script\\n\\ndef description = \"'Logic Extension Point 2017'\"", preview);

        preview = StringUtils.getPreview(text, 2, false, Integer.MAX_VALUE);
        assertEquals("package com.icthh.xm.lep.script\\n", preview);
    }

}
