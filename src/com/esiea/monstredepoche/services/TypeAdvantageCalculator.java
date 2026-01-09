package com.esiea.monstredepoche.services;

import com.esiea.monstredepoche.models.enums.MonsterType;
import com.esiea.monstredepoche.utils.Constants;

/**
 * Service de calcul des avantages de type entre monstres.
 * Détermine les multiplicateurs de dégâts selon les types :
 * - Avantage : x2.0 (ex: Foudre > Eau)
 * - Désavantage : x0.5 (ex: Foudre < Terre)
 * - Neutre : x1.0
 */
public class TypeAdvantageCalculator {
    
    /**
     * Calcule le multiplicateur d'avantage de type
     * @param attacker Type du monstre attaquant
     * @param defender Type du monstre défenseur
     * @return Multiplicateur de dégâts (2.0, 0.5 ou 1.0)
     */
    public static double getAdvantageMultiplier(MonsterType attacker, MonsterType defender) {
        // Electric est fort contre Water, faible contre Ground
        if (attacker == MonsterType.ELECTRIC) {
            if (defender == MonsterType.WATER) {
                return Constants.TYPE_ADVANTAGE_MULTIPLIER;
            }
            if (defender == MonsterType.GROUND) {
                return Constants.TYPE_DISADVANTAGE_MULTIPLIER;
            }
        }
        
        // Water est fort contre Fire, faible contre Electric
        if (attacker == MonsterType.WATER) {
            if (defender == MonsterType.FIRE) {
                return Constants.TYPE_ADVANTAGE_MULTIPLIER;
            }
            if (defender == MonsterType.ELECTRIC) {
                return Constants.TYPE_DISADVANTAGE_MULTIPLIER;
            }
        }
        
        // Ground est fort contre Electric, faible contre Nature (Plant ou Insect)
        if (attacker == MonsterType.GROUND) {
            if (defender == MonsterType.ELECTRIC) {
                return Constants.TYPE_ADVANTAGE_MULTIPLIER;
            }
            if (MonsterType.isNatureType(defender)) {
                return Constants.TYPE_DISADVANTAGE_MULTIPLIER;
            }
        }
        
        // Fire est fort contre Nature (Plant ou Insect), faible contre Water
        if (attacker == MonsterType.FIRE) {
            if (MonsterType.isNatureType(defender)) {
                return Constants.TYPE_ADVANTAGE_MULTIPLIER;
            }
            if (defender == MonsterType.WATER) {
                return Constants.TYPE_DISADVANTAGE_MULTIPLIER;
            }
        }
        
        // Nature (Plant ou Insect) est fort contre Ground, faible contre Fire
        if (MonsterType.isNatureType(attacker)) {
            if (defender == MonsterType.GROUND) {
                return Constants.TYPE_ADVANTAGE_MULTIPLIER;
            }
            if (defender == MonsterType.FIRE) {
                return Constants.TYPE_DISADVANTAGE_MULTIPLIER;
            }
        }
        
        // Pas d'avantage/désavantage
        return 1.0;
    }
}

