package com.esiea.monstredepoche.loaders;

import java.util.HashMap;
import java.util.Map;

/**
 * Utilitaire de parsing pour les fichiers de données.
 * Fournit des méthodes pour parser les champs et les plages de valeurs.
 */
public class DataParser {
    
    /**
     * Parse une ligne pour extraire une clé et une valeur
     * @param line La ligne à parser (format: "Key Value")
     * @return Map contenant "key" et "value"
     */
    public static Map<String, String> parseField(String line) {
        Map<String, String> field = new HashMap<>();
        String[] parts = line.trim().split("\\s+", 2);
        if (parts.length >= 2) {
            field.put("key", parts[0]);
            field.put("value", parts[1]);
        }
        return field;
    }
    
    /**
     * Parse une plage de valeurs (ex: "110 141" ou "110")
     * @param rangeStr La chaîne contenant la plage
     * @return Tableau de 2 entiers [min, max] ou [valeur, valeur] si une seule valeur
     */
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
    
    /**
     * Génère un nombre aléatoire dans une plage donnée
     * @param min Valeur minimale (incluse)
     * @param max Valeur maximale (incluse)
     * @return Un nombre aléatoire entre min et max
     */
    public static int randomInRange(int min, int max) {
        return min + (int)(Math.random() * (max - min + 1));
    }
}

