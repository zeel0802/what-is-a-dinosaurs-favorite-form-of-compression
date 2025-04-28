package org.example;

import java.util.*;

public class App {

    // Node class inside App
    static class Node implements Comparable<Node> {
        char ch;
        int freq;
        Node left, right;

        Node(char ch, int freq) {
            this.ch = ch;
            this.freq = freq;
        }

        Node(Node left, Node right) {
            this.ch = '\0';
            this.freq = left.freq + right.freq;
            this.left = left;
            this.right = right;
        }

        public boolean isLeaf() {
            return (left == null) && (right == null);
        }

        @Override
        public int compareTo(Node other) {
            return this.freq - other.freq;
        }
    }

    // Build frequency map
    public static Map<Character, Integer> buildFrequencyMap(String text) {
        Map<Character, Integer> freqMap = new HashMap<>();
        for (char c : text.toCharArray()) {
            freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
        }
        return freqMap;
    }

    // Build Huffman tree
    public static Node buildHuffmanTree(Map<Character, Integer> freqMap) {
        PriorityQueue<Node> pq = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
            pq.add(new Node(entry.getKey(), entry.getValue()));
        }

        while (pq.size() > 1) {
            Node left = pq.poll();
            Node right = pq.poll();
            pq.add(new Node(left, right));
        }
        return pq.poll();
    }

    // Build code table
    public static void buildCodeTable(Node root, String code, Map<Character, String> codeMap) {
        if (root.isLeaf()) {
            codeMap.put(root.ch, code);
            return;
        }
        buildCodeTable(root.left, code + '0', codeMap);
        buildCodeTable(root.right, code + '1', codeMap);
    }

    // Compress function
    public static Map<String, Object> compress(String text) {
        Map<Character, Integer> freqMap = buildFrequencyMap(text);
        Node root = buildHuffmanTree(freqMap);
        Map<Character, String> codeMap = new HashMap<>();
        buildCodeTable(root, "", codeMap);

        StringBuilder encoded = new StringBuilder();
        for (char c : text.toCharArray()) {
            encoded.append(codeMap.get(c));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("codes", codeMap);
        result.put("encodedText", encoded.toString());
        return result;
    }

    // Decompress function
    public static String decompress(String encodedText, Map<Character, String> codeMap) {
        Map<String, Character> reverseCodeMap = new HashMap<>();
        for (Map.Entry<Character, String> entry : codeMap.entrySet()) {
            reverseCodeMap.put(entry.getValue(), entry.getKey());
        }

        StringBuilder decoded = new StringBuilder();
        StringBuilder currentCode = new StringBuilder();

        for (char bit : encodedText.toCharArray()) {
            currentCode.append(bit);
            if (reverseCodeMap.containsKey(currentCode.toString())) {
                decoded.append(reverseCodeMap.get(currentCode.toString()));
                currentCode.setLength(0);
            }
        }

        return decoded.toString();
    }

    // Main method to test
    public static void main(String[] args) {
        String text = "Hello World";

        Map<String, Object> compressed = compress(text);
        Map<Character, String> codeMap = (Map<Character, String>) compressed.get("codes");
        String encodedText = (String) compressed.get("encodedText");

        System.out.println("Original Text: " + text);
        System.out.println("Compressed Text: " + encodedText);
        System.out.println("Codes: " + codeMap);

        String decompressed = decompress(encodedText, codeMap);
        System.out.println("Decompressed Text: " + decompressed);
    }
}
