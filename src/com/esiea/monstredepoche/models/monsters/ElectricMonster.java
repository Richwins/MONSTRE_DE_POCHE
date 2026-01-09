package com.esiea.monstredepoche.models.monsters;

import com.esiea.monstredepoche.models.BattleField;
import com.esiea.monstredepoche.models.Monster;
import com.esiea.monstredepoche.models.enums.MonsterType;
import com.esiea.monstredepoche.services.StatusEffectManager;
import com.esiea.monstredepoche.utils.RandomGenerator;

/**
 * Monstre de type Foudre.
 * Capacité spéciale : peut paralyser l'adversaire avec une certaine probabilité.
 */
public class ElectricMonster extends Monster {
    private double paralysisChance;
    
    /**
     * Constructeur d'un monstre électrique
     * @param paralysisChance Probabilité de paralyser l'adversaire (entre 0.0 et 1.0)
     */
    public ElectricMonster(String name, int hp, int speed, int attack, int defense, double paralysisChance) {
        super(name, MonsterType.ELECTRIC, hp, speed, attack, defense);
        this.paralysisChance = paralysisChance;
    }
    
    /**
     * Utilise la capacité spéciale : tente de paralyser l'adversaire
     */
    @Override
    public void useSpecialAbility(BattleField field, Monster opponent) {
        if (opponent != null && opponent.isAlive()) {
            paralyze(opponent);
        }
    }
    
    /**
     * Tente de paralyser un monstre cible
     * @param target Le monstre à paralyser
     */
    public void paralyze(Monster target) {
        if (RandomGenerator.randomChance(paralysisChance)) {
            StatusEffectManager.applyParalysis(target);
        }
    }
    
    public double getParalysisChance() {
        paralysisChance = paralysisChance * 2;
        return paralysisChance;
    }
}

