package com.esiea.monstredepoche.models;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private List<Monster> monsters;
    private List<Item> items;
    private Monster activeMonster;
    
    public Player(String name) {
        this.name = name;
        this.monsters = new ArrayList<>();
        this.items = new ArrayList<>();
        this.activeMonster = null;
    }
    
    public boolean switchMonster(int index) {
        if (index < 0 || index >= monsters.size()) {
            return false;
        }
        
        Monster newMonster = monsters.get(index);
        if (newMonster == activeMonster || !newMonster.isAlive()) {
            return false;
        }
        
        activeMonster = newMonster;
        return true;
    }
    
    public boolean useItem(int itemIndex, Monster target) {
        if (itemIndex < 0 || itemIndex >= items.size()) {
            return false;
        }
        
        Item item = items.get(itemIndex);
        boolean used = item.use(target);
        
        if (used) {
            items.remove(itemIndex);
        }
        
        return used;
    }
    
    public boolean hasLost() {
        for (Monster monster : monsters) {
            if (monster.isAlive()) {
                return false;
            }
        }
        return true;
    }
    
    public List<Monster> getAvailableMonsters() {
        List<Monster> available = new ArrayList<>();
        for (Monster monster : monsters) {
            if (monster.isAlive() && monster != activeMonster) {
                available.add(monster);
            }
        }
        return available;
    }
    
    // Getters et Setters
    public String getName() {
        return name;
    }
    
    public List<Monster> getMonsters() {
        return monsters;
    }
    
    public void addMonster(Monster monster) {
        monsters.add(monster);
        if (activeMonster == null) {
            activeMonster = monster;
        }
    }
    
    public List<Item> getItems() {
        return items;
    }
    
    public void addItem(Item item) {
        items.add(item);
    }
    
    public Monster getActiveMonster() {
        return activeMonster;
    }
    
    public void setActiveMonster(Monster monster) {
        this.activeMonster = monster;
    }
}

