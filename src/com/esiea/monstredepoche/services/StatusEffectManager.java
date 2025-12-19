package com.esiea.monstredepoche.services;

import com.esiea.monstredepoche.models.Monster;
import com.esiea.monstredepoche.models.enums.StatusCondition;
import com.esiea.monstredepoche.utils.Constants;

public class StatusEffectManager {
    
    public static void applyBurn(Monster monster) {
        if (monster.getCurrentStatus() == StatusCondition.NONE) {
            monster.setCurrentStatus(StatusCondition.BURNED);
        }
    }
    
    public static void applyPoison(Monster monster) {
        if (monster.getCurrentStatus() == StatusCondition.NONE) {
            monster.setCurrentStatus(StatusCondition.POISONED);
        }
    }
    
    public static void applyParalysis(Monster monster) {
        if (monster.getCurrentStatus() == StatusCondition.NONE) {
            monster.setCurrentStatus(StatusCondition.PARALYZED);
        }
    }
    
    public static boolean checkParalysis(Monster monster) {
        if (monster.getCurrentStatus() == StatusCondition.PARALYZED) {
            return java.util.concurrent.ThreadLocalRandom.current().nextDouble() < Constants.PARALYSIS_FAIL_CHANCE;
        }
        return false;
    }
    
    public static void applyBurnDamage(Monster monster) {
        if (monster.getCurrentStatus() == StatusCondition.BURNED) {
            int damage = (int) (monster.getAttack() * Constants.BURN_DAMAGE_RATIO);
            monster.takeDamage(damage);
        }
    }
    
    public static void applyPoisonDamage(Monster monster) {
        if (monster.getCurrentStatus() == StatusCondition.POISONED) {
            int damage = (int) (monster.getAttack() * Constants.POISON_DAMAGE_RATIO);
            monster.takeDamage(damage);
        }
    }
    
    public static void removeStatus(Monster monster) {
        monster.setCurrentStatus(StatusCondition.NONE);
    }
    
    public static void updateStatusDuration(Monster monster) {
        // La paralysie se dissipe progressivement
        if (monster.getCurrentStatus() == StatusCondition.PARALYZED) {
            if (java.util.concurrent.ThreadLocalRandom.current().nextDouble() < 0.2) {
                removeStatus(monster);
            }
        }
    }
}

