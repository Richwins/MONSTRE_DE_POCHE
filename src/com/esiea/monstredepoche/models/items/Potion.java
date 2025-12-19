package com.esiea.monstredepoche.models.items;

import com.esiea.monstredepoche.models.Item;
import com.esiea.monstredepoche.models.Monster;

/**
 * Potion utilisable pour soigner ou augmenter les statistiques d'un monstre.
 * Peut restaurer des points de vie, augmenter l'attaque ou la défense.
 */
public class Potion extends Item {
    private int hpRestore;
    private int attackBoost;
    private int defenseBoost;
    
    /**
     * Constructeur d'une potion
     * @param name Nom de la potion
     * @param description Description de la potion
     * @param hpRestore Points de vie restaurés (0 si aucun)
     * @param attackBoost Bonus d'attaque (0 si aucun)
     * @param defenseBoost Bonus de défense (0 si aucun)
     */
    public Potion(String name, String description, int hpRestore, int attackBoost, int defenseBoost) {
        super(name, description);
        this.hpRestore = hpRestore;
        this.attackBoost = attackBoost;
        this.defenseBoost = defenseBoost;
    }
    
    /**
     * Utilise la potion sur un monstre (soigne et/ou augmente les stats)
     * @param target Le monstre cible
     * @return true si la potion a été utilisée avec succès
     */
    @Override
    public boolean use(Monster target) {
        if (target == null || !target.isAlive()) {
            return false;
        }
        
        if (hpRestore > 0) {
            target.heal(hpRestore);
        }
        
        if (attackBoost > 0) {
            target.setAttack(target.getAttack() + attackBoost);
        }
        
        if (defenseBoost > 0) {
            target.setDefense(target.getDefense() + defenseBoost);
        }
        
        return true;
    }
    
    // Getters
    public int getHpRestore() {
        return hpRestore;
    }
    
    public int getAttackBoost() {
        return attackBoost;
    }
    
    public int getDefenseBoost() {
        return defenseBoost;
    }
}

