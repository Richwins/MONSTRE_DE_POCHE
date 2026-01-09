package com.esiea.monstredepoche.models.monsters;

import com.esiea.monstredepoche.models.BattleField;
import com.esiea.monstredepoche.models.Monster;
import com.esiea.monstredepoche.models.enums.MonsterType;
import com.esiea.monstredepoche.utils.RandomGenerator;

/**
 * Monstre de type Terre.
 * Capacité spéciale : peut s'enfouir sous terre, doublant sa défense.
 */
public class GroundMonster extends Monster {
    private double digChance;
    private boolean isUnderground;
    private int undergroundDuration; // Nombre de tours restants sous terre
    private int originalDefense; // Défense originale avant enfouissement
    
    /**
     * Constructeur d'un monstre de terre
     * @param digChance Probabilité de s'enfouir sous terre
     */
    public GroundMonster(String name, int hp, int speed, int attack, int defense, double digChance) {
        super(name, MonsterType.GROUND, hp, speed, attack, defense);
        this.digChance = digChance;
        this.isUnderground = false;
        this.undergroundDuration = 0;
        this.originalDefense = defense;
    }
    
    /**
     * Utilise la capacité spéciale : tente de s'enfouir sous terre
     */
    @Override
    public void useSpecialAbility(BattleField field, Monster opponent) {
        burrowUnderground();
    }
    
    /**
     * S'enfouit sous terre pour 1 à 3 tours, doublant la défense
     */
    public void burrowUnderground() {
        if (!isUnderground && RandomGenerator.randomChance(digChance)) {
            // Sauvegarder la défense actuelle
            originalDefense = getDefense();
            
            // S'enfouir pour 1 à 3 tours
            undergroundDuration = RandomGenerator.randomInRange(1, 3);
            isUnderground = true;
            
            // Double la défense
            setDefense(originalDefense * 2);
            
            System.out.println(getName() + " s'enfouit sous terre pour " + undergroundDuration + " tour(s) !");
            System.out.println("Sa défense est doublée !");
        }
    }
    
    /**
     * Émerge du sol, restaurant la défense normale
     */
    public void emerge() {
        if (isUnderground) {
            isUnderground = false;
            undergroundDuration = 0;
            
            // Restaure la défense originale
            setDefense(originalDefense);
            
            System.out.println(getName() + " émerge du sol !");
        }
    }
    
    /**
     * Met à jour la durée d'enfouissement.
     * À appeler à chaque tour pour décrémenter le compteur.
     */
    public void updateUndergroundDuration() {
        if (isUnderground) {
            undergroundDuration--;
            
            if (undergroundDuration <= 0) {
                emerge();
            }
        }
    }
    
    public boolean isUnderground() {
        return isUnderground;
    }
    
    public double getDigChance() {
        return digChance;
    }
    
    public int getUndergroundDuration() {
        return undergroundDuration;
    }
}

