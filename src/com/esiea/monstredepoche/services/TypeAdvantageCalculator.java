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
        // Foudre est fort contre Eau, faible contre Terre
        if (attacker == MonsterType.FOUDRE) {
            if (defender == MonsterType.EAU) {
                return Constants.TYPE_ADVANTAGE_MULTIPLIER;
            }
            if (defender == MonsterType.TERRE) {
                return Constants.TYPE_DISADVANTAGE_MULTIPLIER;
            }
        }
        
        // Eau est fort contre Feu, faible contre Foudre
        if (attacker == MonsterType.EAU) {
            if (defender == MonsterType.FEU) {
                return Constants.TYPE_ADVANTAGE_MULTIPLIER;
            }
            if (defender == MonsterType.FOUDRE) {
                return Constants.TYPE_DISADVANTAGE_MULTIPLIER;
            }
        }
        
        // Terre est fort contre Foudre, faible contre Nature
        if (attacker == MonsterType.TERRE) {
            if (defender == MonsterType.FOUDRE) {
                return Constants.TYPE_ADVANTAGE_MULTIPLIER;
            }
            if (defender == MonsterType.NATURE) {
                return Constants.TYPE_DISADVANTAGE_MULTIPLIER;
            }
        }
        
        // Feu est fort contre Nature, faible contre Eau
        if (attacker == MonsterType.FEU) {
            if (defender == MonsterType.NATURE) {
                return Constants.TYPE_ADVANTAGE_MULTIPLIER;
            }
            if (defender == MonsterType.EAU) {
                return Constants.TYPE_DISADVANTAGE_MULTIPLIER;
            }
        }
        
        // Nature est fort contre Terre, faible contre Feu
        if (attacker == MonsterType.NATURE) {
            if (defender == MonsterType.TERRE) {
                return Constants.TYPE_ADVANTAGE_MULTIPLIER;
            }
            if (defender == MonsterType.FEU) {
                return Constants.TYPE_DISADVANTAGE_MULTIPLIER;
            }
        }
        
        // Pas d'avantage/désavantage
        return 1.0;
    }
}

