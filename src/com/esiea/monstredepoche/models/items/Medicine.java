package com.esiea.monstredepoche.models.items;

import com.esiea.monstredepoche.models.Item;
import com.esiea.monstredepoche.models.Monster;
import com.esiea.monstredepoche.models.enums.StatusCondition;
import com.esiea.monstredepoche.services.StatusEffectManager;

public class Medicine extends Item {
    private boolean curesStatus;
    private boolean driesTerrain;
    
    public Medicine(String name, String description, boolean curesStatus, boolean driesTerrain) {
        super(name, description);
        this.curesStatus = curesStatus;
        this.driesTerrain = driesTerrain;
    }
    
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

