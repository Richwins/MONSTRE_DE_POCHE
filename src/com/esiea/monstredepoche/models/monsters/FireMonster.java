package com.esiea.monstredepoche.models.monsters;

import com.esiea.monstredepoche.models.BattleField;
import com.esiea.monstredepoche.models.Monster;
import com.esiea.monstredepoche.models.enums.MonsterType;
import com.esiea.monstredepoche.services.StatusEffectManager;
import com.esiea.monstredepoche.utils.RandomGenerator;

/**
 * Monstre de type Feu.
 * Capacité spéciale : peut brûler l'adversaire, lui infligeant des dégâts chaque tour.
 */
public class FireMonster extends Monster {
    private double burnChance;
    
    /**
     * Constructeur d'un monstre de feu
     * @param burnChance Probabilité de brûler l'adversaire
     */
    public FireMonster(String name, int hp, int speed, int attack, int defense, double burnChance) {
        super(name, MonsterType.FIRE, hp, speed, attack, defense);
        this.burnChance = burnChance;
    }
    
    /**
     * Utilise la capacité spéciale : tente de brûler l'adversaire
     */
    @Override
    public void useSpecialAbility(BattleField field, Monster opponent) {
        if (opponent != null && opponent.isAlive()) {
            inflictBurn(opponent);
        }
    }
    
    /**
     * Tente d'infliger une brûlure à un monstre cible
     * @param target Le monstre à brûler
     */
    public void inflictBurn(Monster target) {
        if (RandomGenerator.randomChance(burnChance)) {
            StatusEffectManager.applyBurn(target);
        }
    }
    
    public double getBurnChance() {
        return burnChance;
    }
}

