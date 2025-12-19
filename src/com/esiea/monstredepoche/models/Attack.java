package com.esiea.monstredepoche.models;

import com.esiea.monstredepoche.models.enums.AttackType;
import com.esiea.monstredepoche.services.DamageCalculator;
import com.esiea.monstredepoche.utils.RandomGenerator;

/**
 * Représente une attaque qu'un monstre peut utiliser.
 * Chaque attaque a un type, une puissance, un nombre d'utilisations limité
 * et une probabilité d'échec.
 */
public class Attack {
    private String name;
    private AttackType type;
    private int power;
    private int nbUse;
    private int maxUses;
    private double failProbability;
    
    /**
     * Constructeur d'une attaque
     * @param name Nom de l'attaque
     * @param type Type de l'attaque (NORMAL, ELECTRIC, WATER, etc.)
     * @param power Puissance de l'attaque
     * @param maxUses Nombre maximum d'utilisations
     * @param failProbability Probabilité d'échec (entre 0.0 et 1.0)
     */
    public Attack(String name, AttackType type, int power, int maxUses, double failProbability) {
        this.name = name;
        this.type = type;
        this.power = power;
        this.maxUses = maxUses;
        this.nbUse = maxUses;
        this.failProbability = failProbability;
    }
    
    /**
     * Vérifie si l'attaque peut encore être utilisée
     * @return true si le nombre d'utilisations restantes > 0
     */
    public boolean canUse() {
        return nbUse > 0;
    }
    
    /**
     * Utilise l'attaque (décrémente le compteur et vérifie l'échec)
     * @return true si l'attaque réussit, false si elle échoue ou n'a plus d'utilisations
     */
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
    
    /**
     * Calcule les dégâts infligés par cette attaque
     * @param attacker Le monstre attaquant
     * @param defender Le monstre défenseur
     * @return Les dégâts calculés
     */
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

