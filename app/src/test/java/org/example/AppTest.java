package org.example;

import org.junit.jupiter.api.Test;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppTest {

    @Test
    public void testCompressionDecompression() {
        String text = "Hello World";
        Map<String, Object> compressed = App.compress(text);
        Map<Character, String> codeMap = (Map<Character, String>) compressed.get("codes");
        String encodedText = (String) compressed.get("encodedText");

        String decompressed = App.decompress(encodedText, codeMap);
        assertEquals(text, decompressed);
    }
}
