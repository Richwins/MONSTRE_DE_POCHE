package com.esiea.monstredepoche.models.monsters;

import com.esiea.monstredepoche.models.BattleField;
import com.esiea.monstredepoche.models.Monster;
import com.esiea.monstredepoche.models.enums.MonsterType;
import com.esiea.monstredepoche.models.enums.StatusCondition;
import com.esiea.monstredepoche.services.StatusEffectManager;
import com.esiea.monstredepoche.utils.RandomGenerator;

public class PlantMonster extends Monster {
    private double healChance;
    
    public PlantMonster(String name, int hp, int speed, int attack, int defense, double healChance) {
        super(name, MonsterType.PLANT, hp, speed, attack, defense);
        this.healChance = healChance;
    }
    
    @Override
    public void useSpecialAbility(BattleField field, Monster opponent) {
        removeStatus();
    }
    
    public void removeStatus() {
        if (getCurrentStatus() != StatusCondition.NONE) {
            if (RandomGenerator.randomChance(healChance)) {
                StatusEffectManager.removeStatus(this);
            }
        }
    }
    
    public double getHealChance() {
        return healChance;
    }
}

