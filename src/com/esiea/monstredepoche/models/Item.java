package com.esiea.monstredepoche.models;

public abstract class Item {
    protected String name;
    protected String description;
    
    public Item(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    public abstract boolean use(Monster target);
    
    // Getters
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
}

