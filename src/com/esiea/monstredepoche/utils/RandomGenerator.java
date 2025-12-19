package com.esiea.monstredepoche.utils;

import java.util.Random;

public class RandomGenerator {
    private static final Random random = new Random();
    
    public static int randomInRange(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min must be <= max");
        }
        return random.nextInt(max - min + 1) + min;
    }
    
    public static double randomDouble(double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException("min must be <= max");
        }
        return min + (max - min) * random.nextDouble();
    }
    
    public static boolean randomChance(double probability) {
        return random.nextDouble() < probability;
    }
}

