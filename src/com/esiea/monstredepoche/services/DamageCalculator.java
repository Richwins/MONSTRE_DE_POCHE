package com.esiea.monstredepoche.services;

import com.esiea.monstredepoche.models.Attack;
import com.esiea.monstredepoche.models.Monster;
import com.esiea.monstredepoche.models.enums.AttackType;
import com.esiea.monstredepoche.models.enums.MonsterType;
import com.esiea.monstredepoche.utils.Constants;
import com.esiea.monstredepoche.utils.RandomGenerator;

/**
 * Service de calcul des dégâts.
 * Contient les formules de calcul pour les attaques à mains nues
 * et les attaques spéciales, en tenant compte des avantages de type.
 */
public class DamageCalculator {
    
    /**
     * Calcule les dégâts d'une attaque à mains nues
     * Formule : dégâts = (20 × attaque × coef) / défense adverse
     * Puis multiplié par l'avantage de type
     */
    public static double calculateBareDamage(Monster attacker, Monster defender) {
        double coefficient = RandomGenerator.randomDouble(
            Constants.MIN_DAMAGE_COEFFICIENT,
            Constants.MAX_DAMAGE_COEFFICIENT
        );
        double advantage = TypeAdvantageCalculator.getAdvantageMultiplier(
            attacker.getType(),
            defender.getType()
        );
        
        return (Constants.BARE_DAMAGE_MULTIPLIER * attacker.getAttack() * coefficient) / 
               defender.getDefense() * 
               advantage;
    }
    
    /**
     * Calcule les dégâts d'une attaque spéciale
     * Formule : Dégâts = ((11 × attaque × puissance) / (25 × défense adverse) + 2) × avantage × coef
     */
    public static double calculateAttackDamage(Monster attacker, Monster defender, Attack attack) {
        double coefficient = RandomGenerator.randomDouble(
            Constants.MIN_DAMAGE_COEFFICIENT,
            Constants.MAX_DAMAGE_COEFFICIENT
        );
        
        // Calcul de l'avantage de type basé sur le type de l'attaque
        double advantage = 1.0;
        if (attack.getType() == AttackType.ELECTRIC && defender.getType() == MonsterType.WATER) {
            advantage = Constants.TYPE_ADVANTAGE_MULTIPLIER;
        } else if (attack.getType() == AttackType.ELECTRIC && defender.getType() == MonsterType.GROUND) {
            advantage = Constants.TYPE_DISADVANTAGE_MULTIPLIER;
        } else if (attack.getType() == AttackType.WATER && defender.getType() == MonsterType.FIRE) {
            advantage = Constants.TYPE_ADVANTAGE_MULTIPLIER;
        } else if (attack.getType() == AttackType.WATER && defender.getType() == MonsterType.ELECTRIC) {
            advantage = Constants.TYPE_DISADVANTAGE_MULTIPLIER;
        } else if (attack.getType() == AttackType.GROUND && defender.getType() == MonsterType.ELECTRIC) {
            advantage = Constants.TYPE_ADVANTAGE_MULTIPLIER;
        } else if (attack.getType() == AttackType.FIRE && MonsterType.isNatureType(defender.getType())) {
            advantage = Constants.TYPE_ADVANTAGE_MULTIPLIER;
        } else if (attack.getType() == AttackType.FIRE && defender.getType() == MonsterType.WATER) {
            advantage = Constants.TYPE_DISADVANTAGE_MULTIPLIER;
        }
        
        return ((Constants.ATTACK_DAMAGE_BASE * attacker.getAttack() * attack.getPower()) / 
                (25.0 * defender.getDefense()) + 2) * 
               advantage * 
               coefficient;
    }
}

