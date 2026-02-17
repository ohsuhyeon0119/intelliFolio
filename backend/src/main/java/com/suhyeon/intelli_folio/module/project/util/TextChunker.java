package com.suhyeon.intelli_folio.module.project.util;

import java.util.ArrayList;
import java.util.List;

public final class TextChunker {

    private TextChunker() {}

    /**
     * Simple character-based chunking with overlap.
     * - chunkSizeChars: size of each chunk (e.g., 1200~2000)
     * - overlapChars: overlap between chunks (e.g., 150~300)
     */
    public static List<String> chunkByChars(String text, int chunkSizeChars, int overlapChars) {
        if (text == null) return List.of();
        String t = text.trim();
        if (t.isEmpty()) return List.of();

        int size = Math.max(1, chunkSizeChars);
        int overlap = Math.max(0, overlapChars);
        if (overlap >= size) overlap = Math.max(0, size / 4);

        List<String> out = new ArrayList<>();
        int start = 0;

        while (start < t.length()) {
            int end = Math.min(t.length(), start + size);
            String chunk = t.substring(start, end).trim();
            if (!chunk.isEmpty()) out.add(chunk);

            if (end == t.length()) break;
            start = end - overlap;
            if (start < 0) start = 0;
        }
        return out;
    }

    public static String stripExtension(String filename) {
        if (filename == null) return "document";
        int dot = filename.lastIndexOf('.');
        if (dot <= 0) return filename;
        return filename.substring(0, dot);
    }
}