package com.esiea.monstredepoche.models;

import com.esiea.monstredepoche.models.enums.MonsterType;
import com.esiea.monstredepoche.models.enums.StatusCondition;
import com.esiea.monstredepoche.services.DamageCalculator;
import com.esiea.monstredepoche.services.TypeAdvantageCalculator;

import java.util.ArrayList;
import java.util.List;

public abstract class Monster {
    protected String name;
    protected MonsterType type;
    protected int hp;
    protected int maxHp;
    protected int attack;
    protected int defense;
    protected int speed;
    protected StatusCondition currentStatus;
    protected List<Attack> attacks;
    
    public Monster(String name, MonsterType type, int hp, int speed, int attack, int defense) {
        this.name = name;
        this.type = type;
        this.maxHp = hp;
        this.hp = hp;
        this.speed = speed;
        this.attack = attack;
        this.defense = defense;
        this.currentStatus = StatusCondition.NONE;
        this.attacks = new ArrayList<>();
    }
    
    public void takeDamage(int damage) {
        hp = Math.max(0, hp - damage);
    }
    
    public void heal(int amount) {
        hp = Math.min(maxHp, hp + amount);
    }
    
    public void addAttack(Attack attack) {
        attacks.add(attack);
    }
    
    public boolean isAlive() {
        return hp > 0;
    }
    
    public double getTypeAdvantage(Monster opponent) {
        return TypeAdvantageCalculator.getAdvantageMultiplier(this.type, opponent.getType());
    }
    
    public double calculateBareDamage(Monster target) {
        return DamageCalculator.calculateBareDamage(this, target);
    }
    
    // Méthodes abstraites
    public abstract void useSpecialAbility(BattleField field, Monster opponent);
    
    // Getters et Setters
    public String getName() {
        return name;
    }
    
    public MonsterType getType() {
        return type;
    }
    
    public int getHp() {
        return hp;
    }
    
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
    }
    
    public List<Attack> getAttacks() {
        return attacks;
    }
    
    @Override
    public String toString() {
        return name + " (HP: " + hp + "/" + maxHp + ", Type: " + type + ")";
    }
}

