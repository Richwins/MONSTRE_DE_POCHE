package com.esiea.monstredepoche.models.monsters;

import com.esiea.monstredepoche.models.BattleField;
import com.esiea.monstredepoche.models.Monster;
import com.esiea.monstredepoche.models.enums.MonsterType;
import com.esiea.monstredepoche.utils.RandomGenerator;

public class GroundMonster extends Monster {
    private double digChance;
    private boolean isUnderground;
    
    public GroundMonster(String name, int hp, int speed, int attack, int defense, double digChance) {
        super(name, MonsterType.GROUND, hp, speed, attack, defense);
        this.digChance = digChance;
        this.isUnderground = false;
    }
    
    @Override
    public void useSpecialAbility(BattleField field, Monster opponent) {
        burrowUnderground();
    }
    
    public void burrowUnderground() {
        if (RandomGenerator.randomChance(digChance)) {
            isUnderground = true;
            // Double la défense
            setDefense(getDefense() * 2);
        }
    }
    
    public void emerge() {
        if (isUnderground) {
            isUnderground = false;
            // Restaure la défense normale
            setDefense(getDefense() / 2);
        }
    }
    
    public boolean isUnderground() {
        return isUnderground;
    }
    
    public double getDigChance() {
        return digChance;
    }
}

