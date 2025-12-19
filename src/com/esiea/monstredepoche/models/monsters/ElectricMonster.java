package com.esiea.monstredepoche.models.monsters;

import com.esiea.monstredepoche.models.BattleField;
import com.esiea.monstredepoche.models.Monster;
import com.esiea.monstredepoche.models.enums.MonsterType;
import com.esiea.monstredepoche.services.StatusEffectManager;
import com.esiea.monstredepoche.utils.RandomGenerator;

public class ElectricMonster extends Monster {
    private double paralysisChance;
    
    public ElectricMonster(String name, int hp, int speed, int attack, int defense, double paralysisChance) {
        super(name, MonsterType.ELECTRIC, hp, speed, attack, defense);
        this.paralysisChance = paralysisChance;
    }
    
    @Override
    public void useSpecialAbility(BattleField field, Monster opponent) {
        if (opponent != null && opponent.isAlive()) {
            paralyze(opponent);
        }
    }
    
    public void paralyze(Monster target) {
        if (RandomGenerator.randomChance(paralysisChance)) {
            StatusEffectManager.applyParalysis(target);
        }
    }
    
    public double getParalysisChance() {
        return paralysisChance;
    }
}

