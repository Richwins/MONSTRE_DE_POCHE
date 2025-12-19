package com.esiea.monstredepoche.models.monsters;

import com.esiea.monstredepoche.models.BattleField;
import com.esiea.monstredepoche.models.Monster;
import com.esiea.monstredepoche.models.enums.MonsterType;
import com.esiea.monstredepoche.services.StatusEffectManager;
import com.esiea.monstredepoche.utils.RandomGenerator;

public class FireMonster extends Monster {
    private double burnChance;
    
    public FireMonster(String name, int hp, int speed, int attack, int defense, double burnChance) {
        super(name, MonsterType.FEU, hp, speed, attack, defense);
        this.burnChance = burnChance;
    }
    
    @Override
    public void useSpecialAbility(BattleField field, Monster opponent) {
        if (opponent != null && opponent.isAlive()) {
            inflictBurn(opponent);
        }
    }
    
    public void inflictBurn(Monster target) {
        if (RandomGenerator.randomChance(burnChance)) {
            StatusEffectManager.applyBurn(target);
        }
    }
    
    public double getBurnChance() {
        return burnChance;
    }
}

