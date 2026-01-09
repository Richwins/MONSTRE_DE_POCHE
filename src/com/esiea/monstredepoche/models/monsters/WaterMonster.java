package com.esiea.monstredepoche.models.monsters;

import com.esiea.monstredepoche.models.BattleField;
import com.esiea.monstredepoche.models.Monster;
import com.esiea.monstredepoche.models.enums.MonsterType;
import com.esiea.monstredepoche.utils.RandomGenerator;

/**
 * Monstre de type Eau.
 * Capacité spéciale : peut inonder le terrain, ce qui soigne les monstres Nature
 * et guérit les brûlures/empoisonnements.
 */
public class WaterMonster extends Monster {
    private double floodChance;
    private double fallChance;
    
    /**
     * Constructeur d'un monstre d'eau
     * @param floodChance Probabilité d'inonder le terrain
     * @param fallChance Probabilité de faire glisser l'adversaire (non implémenté)
     */
    public WaterMonster(String name, int hp, int speed, int attack, int defense, double floodChance, double fallChance) {
        super(name, MonsterType.WATER, hp, speed, attack, defense);
        this.floodChance = floodChance;
        this.fallChance = fallChance;
    }
    
    /**
     * Utilise la capacité spéciale : tente d'inonder le terrain
     */
    @Override
    public void useSpecialAbility(BattleField field, Monster opponent) {
        triggerFlood(field);
    }
    
    /**
     * Tente d'inonder le terrain pour 1 à 3 tours (aléatoire)
     * @param field Le terrain de combat
     */
    public void triggerFlood(BattleField field) {
        if (RandomGenerator.randomChance(floodChance)) {
            int duration = RandomGenerator.randomInRange(1, 3); // 1 à 3 tours
            field.setFlooded(duration, this);
        }
    }
    
    public double getFloodChance() {
        return floodChance;
    }
    
    public double getFallChance() {
        return fallChance;
    }
}

