package com.esiea.monstredepoche.loaders;

import java.util.HashMap;
import java.util.Map;

public class DataParser {
    
    public static Map<String, String> parseField(String line) {
        Map<String, String> field = new HashMap<>();
        String[] parts = line.trim().split("\\s+", 2);
        if (parts.length >= 2) {
            field.put("key", parts[0]);
            field.put("value", parts[1]);
        }
        return field;
    }
    
    public static int[] parseRange(String rangeStr) {
        String[] parts = rangeStr.trim().split("\\s+");
        int[] range = new int[2];
        if (parts.length >= 1) {
            range[0] = Integer.parseInt(parts[0]);
        }
        if (parts.length >= 2) {
            range[1] = Integer.parseInt(parts[1]);
        } else {
            range[1] = range[0];
        }
        return range;
    }
    
    public static int randomInRange(int min, int max) {
        return min + (int)(Math.random() * (max - min + 1));
    }
}

