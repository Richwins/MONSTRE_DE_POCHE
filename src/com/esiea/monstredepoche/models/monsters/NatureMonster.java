package com.esiea.monstredepoche.models.monsters;

import com.esiea.monstredepoche.models.BattleField;
import com.esiea.monstredepoche.models.Monster;
import com.esiea.monstredepoche.models.enums.MonsterType;
import com.esiea.monstredepoche.models.enums.StatusCondition;
import com.esiea.monstredepoche.services.StatusEffectManager;
import com.esiea.monstredepoche.utils.RandomGenerator;

public class NatureMonster extends Monster {
    private double healChance;
    private int poisonCounter;
    
    public NatureMonster(String name, int hp, int speed, int attack, int defense, double healChance) {
        super(name, MonsterType.NATURE, hp, speed, attack, defense);
        this.healChance = healChance;
        this.poisonCounter = 0;
    }
    
    @Override
    public void useSpecialAbility(BattleField field, Monster opponent) {
        // Capacité de soin des altérations d'état
        removeStatus();
        
        // Capacité d'empoisonnement
        if (opponent != null && opponent.isAlive()) {
            inflictPoison(opponent);
        }
    }
    
    public void removeStatus() {
        if (getCurrentStatus() != StatusCondition.NONE) {
            if (RandomGenerator.randomChance(healChance)) {
                StatusEffectManager.removeStatus(this);
            }
        }
    }
    
    public void inflictPoison(Monster target) {
        poisonCounter++;
        // Empoisonne une attaque sur trois
        if (poisonCounter % 3 == 0) {
            StatusEffectManager.applyPoison(target);
        }
    }
    
    public double getHealChance() {
        return healChance;
    }
    
    public int getPoisonCounter() {
        return poisonCounter;
    }
}

