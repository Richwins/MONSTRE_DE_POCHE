package com.esiea.monstredepoche.models.items;

import com.esiea.monstredepoche.models.Item;
import com.esiea.monstredepoche.models.Monster;
import com.esiea.monstredepoche.models.enums.StatusCondition;
import com.esiea.monstredepoche.services.StatusEffectManager;

/**
 * Médicament utilisable pour soigner les altérations d'état d'un monstre
 * ou assécher le terrain inondé.
 */
public class Medicine extends Item {
    private boolean curesStatus;
    private boolean driesTerrain;
    
    /**
     * Constructeur d'un médicament
     * @param name Nom du médicament
     * @param description Description du médicament
     * @param curesStatus true si le médicament soigne les altérations d'état
     * @param driesTerrain true si le médicament assèche le terrain
     */
    public Medicine(String name, String description, boolean curesStatus, boolean driesTerrain) {
        super(name, description);
        this.curesStatus = curesStatus;
        this.driesTerrain = driesTerrain;
    }
    
    /**
     * Utilise le médicament sur un monstre (soigne les altérations d'état)
     * @param target Le monstre cible
     * @return true si le médicament a été utilisé avec succès
     */
    @Override
    public boolean use(Monster target) {
        if (target == null || !target.isAlive()) {
            return false;
        }
        
        if (curesStatus && target.getCurrentStatus() != StatusCondition.NONE) {
            StatusEffectManager.removeStatus(target);
            return true;
        }
        
        return false;
    }
    
    /**
     * Vérifie si le médicament peut être utilisé sur le terrain
     * @return true si le médicament assèche le terrain
     */
    public boolean useOnTerrain() {
        return driesTerrain;
    }
    
    // Getters
    public boolean curesStatus() {
        return curesStatus;
    }
    
    public boolean driesTerrain() {
        return driesTerrain;
    }
}

