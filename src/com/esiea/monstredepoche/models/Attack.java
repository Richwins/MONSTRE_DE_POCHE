package com.esiea.monstredepoche.models;

import com.esiea.monstredepoche.models.enums.AttackType;
import com.esiea.monstredepoche.services.DamageCalculator;
import com.esiea.monstredepoche.utils.RandomGenerator;

public class Attack {
    private String name;
    private AttackType type;
    private int power;
    private int nbUse;
    private int maxUses;
    private double failProbability;
    
    public Attack(String name, AttackType type, int power, int maxUses, double failProbability) {
        this.name = name;
        this.type = type;
        this.power = power;
        this.maxUses = maxUses;
        this.nbUse = maxUses;
        this.failProbability = failProbability;
    }
    
    public boolean canUse() {
        return nbUse > 0;
    }
    
    public boolean use() {
        if (!canUse()) {
            return false;
        }
        
        // Vérifier si l'attaque échoue
        if (RandomGenerator.randomChance(failProbability)) {
            return false;
        }
        
        nbUse--;
        return true;
    }
    
    public double calculateDamage(Monster attacker, Monster defender) {
        return DamageCalculator.calculateAttackDamage(attacker, defender, this);
    }
    
    // Getters
    public String getName() {
        return name;
    }
    
    public AttackType getType() {
        return type;
    }
    
    public int getPower() {
        return power;
    }
    
    public int getNbUse() {
        return nbUse;
    }
    
    public int getMaxUses() {
        return maxUses;
    }
    
    public double getFailProbability() {
        return failProbability;
    }
    
    public void setNbUse(int nbUse) {
        this.nbUse = nbUse;
    }
}

