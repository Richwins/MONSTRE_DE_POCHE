package com.esiea.monstredepoche.services;

import com.esiea.monstredepoche.models.enums.MonsterType;
import com.esiea.monstredepoche.utils.Constants;

public class TypeAdvantageCalculator {
    
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
        
        // Ground est fort contre Electric, faible contre Plant/Insect (Nature)
        if (attacker == MonsterType.GROUND) {
            if (defender == MonsterType.ELECTRIC) {
                return Constants.TYPE_ADVANTAGE_MULTIPLIER;
            }
            if (defender == MonsterType.PLANT || defender == MonsterType.INSECT) {
                return Constants.TYPE_DISADVANTAGE_MULTIPLIER;
            }
        }
        
        // Fire est fort contre Plant/Insect (Nature), faible contre Water
        if (attacker == MonsterType.FIRE) {
            if (defender == MonsterType.PLANT || defender == MonsterType.INSECT) {
                return Constants.TYPE_ADVANTAGE_MULTIPLIER;
            }
            if (defender == MonsterType.WATER) {
                return Constants.TYPE_DISADVANTAGE_MULTIPLIER;
            }
        }
        
        // Pas d'avantage/désavantage
        return 1.0;
    }
}

