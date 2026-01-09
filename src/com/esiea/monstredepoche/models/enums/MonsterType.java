package com.esiea.monstredepoche.models.enums;

/**
 * Types élémentaires des monstres.
 * Détermine les avantages et désavantages dans les combats.
 * 
 * Note: "Nature" est une catégorie qui regroupe PLANT et INSECT,
 * mais ces deux types sont distincts dans l'enum pour la logique de combat.
 */
public enum MonsterType {
    ELECTRIC,
    WATER,
    GROUND,
    FIRE,
    PLANT,
    INSECT;
    
    /**
     * Vérifie si un type appartient à la catégorie Nature (Plant ou Insect).
     * @param type Le type à vérifier
     * @return true si le type est Plant ou Insect, false sinon
     */
    public static boolean isNatureType(MonsterType type) {
        return type == PLANT || type == INSECT;
    }
}

