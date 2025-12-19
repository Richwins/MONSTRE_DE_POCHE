package com.esiea.monstredepoche.models.monsters;

import com.esiea.monstredepoche.models.BattleField;
import com.esiea.monstredepoche.models.Monster;
import com.esiea.monstredepoche.models.enums.MonsterType;
import com.esiea.monstredepoche.services.StatusEffectManager;

public class InsectMonster extends Monster {
    private int poisonCounter;
    
    public InsectMonster(String name, int hp, int speed, int attack, int defense) {
        super(name, MonsterType.INSECT, hp, speed, attack, defense);
        this.poisonCounter = 0;
    }
    
    @Override
    public void useSpecialAbility(BattleField field, Monster opponent) {
        if (opponent != null && opponent.isAlive()) {
            inflictPoison(opponent);
        }
    }
    
    public void inflictPoison(Monster target) {
        poisonCounter++;
        // Empoisonne une attaque sur trois
        if (poisonCounter % 3 == 0) {
            StatusEffectManager.applyPoison(target);
        }
    }
    
    public int getPoisonCounter() {
        return poisonCounter;
    }
}

