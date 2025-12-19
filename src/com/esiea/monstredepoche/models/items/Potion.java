package com.esiea.monstredepoche.models.items;

import com.esiea.monstredepoche.models.Item;
import com.esiea.monstredepoche.models.Monster;

public class Potion extends Item {
    private int hpRestore;
    private int attackBoost;
    private int defenseBoost;
    
    public Potion(String name, String description, int hpRestore, int attackBoost, int defenseBoost) {
        super(name, description);
        this.hpRestore = hpRestore;
        this.attackBoost = attackBoost;
        this.defenseBoost = defenseBoost;
    }
    
    @Override
    public boolean use(Monster target) {
        if (target == null || !target.isAlive()) {
            return false;
        }
        
        if (hpRestore > 0) {
            target.heal(hpRestore);
        }
        
        if (attackBoost > 0) {
            target.setAttack(target.getAttack() + attackBoost);
        }
        
        if (defenseBoost > 0) {
            target.setDefense(target.getDefense() + defenseBoost);
        }
        
        return true;
    }
    
    // Getters
    public int getHpRestore() {
        return hpRestore;
    }
    
    public int getAttackBoost() {
        return attackBoost;
    }
    
    public int getDefenseBoost() {
        return defenseBoost;
    }
}

