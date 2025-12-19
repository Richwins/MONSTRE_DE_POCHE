package com.esiea.monstredepoche.models;

import com.esiea.monstredepoche.models.enums.MonsterType;
import com.esiea.monstredepoche.models.enums.StatusCondition;
import com.esiea.monstredepoche.services.DamageCalculator;
import com.esiea.monstredepoche.services.TypeAdvantageCalculator;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe abstraite représentant un monstre dans le jeu.
 * Chaque monstre possède des statistiques (HP, attaque, défense, vitesse),
 * un type élémentaire, des attaques et peut subir des altérations d'état.
 * Les classes concrètes implémentent la capacité spéciale unique à chaque type.
 */
public abstract class Monster {
    protected String name;
    protected MonsterType type;
    protected int hp;
    protected int maxHp;
    protected int attack;
    protected int defense;
    protected int speed;
    protected StatusCondition currentStatus;
    protected int paralysisCounter; // Nombre de tours passés en paralysie
    protected boolean hasUsedSpecialAbility; // Flag pour la récupération PV Nature
    protected List<Attack> attacks;
    
    /**
     * @param name Nom du monstre
     * @param type Type du monstre
     * @param hp Points de vie (HP) du monstre
     * @param speed Vitesse du monstre
     * @param attack Attaque du monstre
     * @param defense Défense du monstre
     */
    public Monster(String name, MonsterType type, int hp, int speed, int attack, int defense) {
        this.name = name;
        this.type = type;
        this.maxHp = hp;
        this.hp = hp;
        this.speed = speed;
        this.attack = attack;
        this.defense = defense;
        this.currentStatus = StatusCondition.NONE;
        this.paralysisCounter = 0;
        this.hasUsedSpecialAbility = false;
        this.attacks = new ArrayList<>();
    }
    
    /**
     * Inflige des dégâts au monstre (réduit les points de vie)
     * @param damage Montant des dégâts à infliger
     */
    public void takeDamage(int damage) {
        hp = Math.max(0, hp - damage);
    }
    
    /**
     * Soigne le monstre (augmente les points de vie)
     * @param amount Montant de points de vie à restaurer
     */
    public void heal(int amount) {
        hp = Math.min(maxHp, hp + amount);
    }
    
    /**
     * Ajoute une attaque au monstre (maximum 4 attaques)
     * @param attack L'attaque à ajouter
     */
    public void addAttack(Attack attack) {
        if (attacks.size() < 4) {
            attacks.add(attack);
        }
    }
    
    /**
     * Vérifie si le monstre est encore en vie
     * @return true si les points de vie sont supérieurs à 0
     */
    public boolean isAlive() {
        return hp > 0;
    }
    
    /**
     * Calcule l'avantage de type contre un adversaire
     * @param opponent Le monstre adverse
     * @return Le multiplicateur d'avantage (2.0 si fort, 0.5 si faible, 1.0 sinon)
     */
    public double getTypeAdvantage(Monster opponent) {
        return TypeAdvantageCalculator.getAdvantageMultiplier(this.type, opponent.getType());
    }
    
    /**
     * Calcule les dégâts d'une attaque à mains nues
     * @param target Le monstre cible
     * @return Les dégâts calculés
     */
    public double calculateBareDamage(Monster target) {
        return DamageCalculator.calculateBareDamage(this, target);
    }
    
    /**
     * Méthode abstraite pour utiliser la capacité spéciale du monstre.
     * Chaque type de monstre a une capacité unique (paralysie, inondation, etc.)
     * @param field Le terrain de combat
     * @param opponent Le monstre adverse
     */
    public abstract void useSpecialAbility(BattleField field, Monster opponent);
    
    // Getters et Setters
    public String getName() {
        return name;
    }
    
    public MonsterType getType() {
        return type;
    }
    
    /**
     * @return Les points de vie actuels du monstre (HP)
     */
    public int getHp() {
        return hp;
    }
    
    /**
     * @return Les points de vie maximum du monstre (HP max)
     */
    public int getMaxHp() {
        return maxHp;
    }
    
    public int getAttack() {
        return attack;
    }
    
    public void setAttack(int attack) {
        this.attack = attack;
    }
    
    public int getDefense() {
        return defense;
    }
    
    public void setDefense(int defense) {
        this.defense = defense;
    }
    
    public int getSpeed() {
        return speed;
    }
    
    public StatusCondition getCurrentStatus() {
        return currentStatus;
    }
    
    public void setCurrentStatus(StatusCondition status) {
        this.currentStatus = status;
        // Réinitialiser le compteur si le statut change
        if (status != StatusCondition.PARALYZED) {
            this.paralysisCounter = 0;
        }
    }
    
    public int getParalysisCounter() {
        return paralysisCounter;
    }
    
    public void incrementParalysisCounter() {
        this.paralysisCounter++;
    }
    
    public void resetParalysisCounter() {
        this.paralysisCounter = 0;
    }
    
    public boolean hasUsedSpecialAbility() {
        return hasUsedSpecialAbility;
    }
    
    public void setHasUsedSpecialAbility(boolean hasUsedSpecialAbility) {
        this.hasUsedSpecialAbility = hasUsedSpecialAbility;
    }
    
    public List<Attack> getAttacks() {
        return attacks;
    }
    
    @Override
    public String toString() {
        return name + " (Points de vie: " + hp + "/" + maxHp + ", Type: " + type + ")";
    }
}

