package com.esiea.monstredepoche.utils;

import java.util.Random;

/**
 * Générateur de nombres aléatoires pour le jeu.
 * Fournit des méthodes utilitaires pour générer des nombres aléatoires
 * dans des plages données et tester des probabilités.
 */
public class RandomGenerator {
    private static final Random random = new Random();
    
    /**
     * Génère un entier aléatoire dans une plage donnée
     * @param min Valeur minimale (incluse)
     * @param max Valeur maximale (incluse)
     * @return Un entier aléatoire entre min et max
     * @throws IllegalArgumentException si min > max
     */
    public static int randomInRange(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min must be <= max");
        }
        return random.nextInt(max - min + 1) + min;
    }
    
    /**
     * Génère un double aléatoire dans une plage donnée
     * @param min Valeur minimale (incluse)
     * @param max Valeur maximale (excluse)
     * @return Un double aléatoire entre min et max
     * @throws IllegalArgumentException si min > max
     */
    public static double randomDouble(double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException("min must be <= max");
        }
        return min + (max - min) * random.nextDouble();
    }
    
    /**
     * Teste une probabilité (ex: 0.25 = 25% de chance)
     * @param probability Probabilité entre 0.0 et 1.0
     * @return true si le test réussit, false sinon
     */
    public static boolean randomChance(double probability) {
        return random.nextDouble() < probability;
    }
}

