package com.esiea.monstredepoche.models.monsters;

import com.esiea.monstredepoche.models.BattleField;
import com.esiea.monstredepoche.models.Monster;
import com.esiea.monstredepoche.models.enums.MonsterType;
import com.esiea.monstredepoche.utils.RandomGenerator;

public class WaterMonster extends Monster {
    private double floodChance;
    private double fallChance;
    
    public WaterMonster(String name, int hp, int speed, int attack, int defense, double floodChance, double fallChance) {
        super(name, MonsterType.EAU, hp, speed, attack, defense);
        this.floodChance = floodChance;
        this.fallChance = fallChance;
    }
    
    @Override
    public void useSpecialAbility(BattleField field, Monster opponent) {
        triggerFlood(field);
    }
    
    public void triggerFlood(BattleField field) {
        if (RandomGenerator.randomChance(floodChance)) {
            field.setFlooded(3); // Durée de 3 tours
        }
    }
    
    public double getFloodChance() {
        return floodChance;
    }
    
    public double getFallChance() {
        return fallChance;
    }
}

