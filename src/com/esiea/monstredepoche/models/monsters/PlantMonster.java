package com.esiea.monstredepoche.models.monsters;

import com.esiea.monstredepoche.models.BattleField;
import com.esiea.monstredepoche.models.Monster;
import com.esiea.monstredepoche.models.enums.MonsterType;
import com.esiea.monstredepoche.models.enums.StatusCondition;
import com.esiea.monstredepoche.services.StatusEffectManager;
import com.esiea.monstredepoche.utils.RandomGenerator;

/**
 * Monstre de type Plante (sous-type de la catégorie Nature).
 * Hérite de NatureMonster pour bénéficier de la récupération de PV sur terrain inondé.
 * 
 * Capacité spéciale spécifique : peut se soigner des altérations d'état (~20% de chance).
 */
public class PlantMonster extends NatureMonster {
    private double healChance;
    
    /**
     * Constructeur d'un monstre de type Plante.
     * @param name      Le nom du monstre
     * @param hp        Les points de vie du monstre
     * @param speed     La vitesse du monstre
     * @param attack    L'attaque du monstre
     * @param defense   La défense du monstre
     * @param healChance Probabilité de se soigner des altérations d'état (~20%)
     */
    public PlantMonster(String name, int hp, int speed, int attack, int defense, double healChance) {
        super(name, MonsterType.PLANT, hp, speed, attack, defense);
        this.healChance = healChance;
    }
    
    /**
     * Utilise la capacité spéciale : tente de se soigner des altérations d'état
     */
    @Override
    public void useSpecialAbility(BattleField field, Monster opponent) {
        removeStatus();
    }
    
    /**
     * Tente de se soigner des altérations d'état (~20% de chance)
     * Supprime paralysie, brûlure et empoisonnement
     */
    public void removeStatus() {
        if (getCurrentStatus() != StatusCondition.NONE) {
            if (RandomGenerator.randomChance(healChance)) {
                StatusEffectManager.removeStatus(this);
                System.out.println(getName() + " se soigne de son altération d'état !");
            }
        }
    }
    
    public double getHealChance() {
        return healChance;
    }
}

