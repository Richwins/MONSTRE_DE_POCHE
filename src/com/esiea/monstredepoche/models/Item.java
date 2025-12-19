package com.esiea.monstredepoche.models;

/**
 * Classe abstraite représentant un objet utilisable dans le jeu.
 * Les objets peuvent être des potions (soin, boost) ou des médicaments (soin d'état).
 */
public abstract class Item {
    protected String name;
    protected String description;
    
    /**
     * Constructeur d'un objet
     * @param name Nom de l'objet
     * @param description Description de l'objet
     */
    public Item(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    /**
     * Utilise l'objet sur un monstre cible
     * @param target Le monstre sur lequel utiliser l'objet
     * @return true si l'objet a été utilisé avec succès
     */
    public abstract boolean use(Monster target);
    
    // Getters
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
}

