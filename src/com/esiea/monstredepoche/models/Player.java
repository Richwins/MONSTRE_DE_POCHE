package com.esiea.monstredepoche.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente un joueur dans le jeu.
 * Un joueur possède une équipe de monstres (maximum 3), des objets,
 * et un monstre actif qui combat sur le terrain.
 */
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
    
    /**
     * Change le monstre actif
     * @param index L'index du monstre à activer
     * @return true si le changement a réussi, false sinon
     */
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
    
    /**
     * Utilise un objet sur un monstre cible
     * @param itemIndex L'index de l'objet à utiliser
     * @param target Le monstre cible
     * @return true si l'objet a été utilisé avec succès
     */
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
    
    /**
     * Vérifie si le joueur a perdu (tous ses monstres sont KO)
     * @return true si tous les monstres sont KO
     */
    public boolean hasLost() {
        for (Monster monster : monsters) {
            if (monster.isAlive()) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Retourne la liste des monstres disponibles (vivants et non actifs)
     * @return Liste des monstres disponibles pour le changement
     */
    public List<Monster> getAvailableMonsters() {
        List<Monster> available = new ArrayList<>();
        for (Monster monster : monsters) {
            if (monster.isAlive() && monster != activeMonster) {
                available.add(monster);
            }
        }
        return available;
    }
    
    /**
     * Vérifie si le monstre actif est KO et le remplace automatiquement par un autre monstre vivant
     * @return true si un nouveau monstre a été activé, false sinon
     */
    public boolean checkAndReplaceKOActiveMonster() {
        // Si le monstre actif est null ou KO, chercher un remplaçant
        if (activeMonster == null || !activeMonster.isAlive()) {
            // Chercher le premier monstre vivant disponible
            for (Monster monster : monsters) {
                if (monster.isAlive()) {
                    activeMonster = monster;
                    return true;
                }
            }
            // Aucun monstre vivant trouvé
            activeMonster = null;
            return false;
        }
        return false;
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

