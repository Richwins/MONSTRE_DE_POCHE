package com.esiea.monstredepoche.models;

import com.esiea.monstredepoche.models.enums.MonsterType;
import com.esiea.monstredepoche.models.enums.StatusCondition;
import com.esiea.monstredepoche.models.enums.TerrainStatus;

public class BattleField {
    private TerrainStatus terrainStatus;
    private int floodDuration;
    private Player player1;
    private Player player2;
    
    public BattleField(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.terrainStatus = TerrainStatus.NORMAL;
        this.floodDuration = 0;
    }
    
    public void applyTerrainEffects() {
        if (terrainStatus == TerrainStatus.FLOODED) {
            // Les monstres de type Plant et Insect récupèrent des PV
            if (player1.getActiveMonster() != null) {
                Monster m1 = player1.getActiveMonster();
                if (m1.getType() == MonsterType.PLANT || 
                    m1.getType() == MonsterType.INSECT) {
                    m1.heal(5);
                }
            }
            if (player2.getActiveMonster() != null) {
                Monster m2 = player2.getActiveMonster();
                if (m2.getType() == MonsterType.PLANT || 
                    m2.getType() == MonsterType.INSECT) {
                    m2.heal(5);
                }
            }
            
            // Guérit les brûlures et empoisonnements
            if (player1.getActiveMonster() != null) {
                Monster m1 = player1.getActiveMonster();
                if (m1.getCurrentStatus() == StatusCondition.BURNED ||
                    m1.getCurrentStatus() == StatusCondition.POISONED) {
                    m1.setCurrentStatus(StatusCondition.NONE);
                }
            }
            if (player2.getActiveMonster() != null) {
                Monster m2 = player2.getActiveMonster();
                if (m2.getCurrentStatus() == StatusCondition.BURNED ||
                    m2.getCurrentStatus() == StatusCondition.POISONED) {
                    m2.setCurrentStatus(StatusCondition.NONE);
                }
            }
        }
    }
    
    public void updateTerrain() {
        if (terrainStatus == TerrainStatus.FLOODED) {
            floodDuration--;
            if (floodDuration <= 0) {
                terrainStatus = TerrainStatus.NORMAL;
            }
        }
    }
    
    public void setFlooded(int duration) {
        this.terrainStatus = TerrainStatus.FLOODED;
        this.floodDuration = duration;
    }
    
    public void setNormal() {
        this.terrainStatus = TerrainStatus.NORMAL;
        this.floodDuration = 0;
    }
    
    // Getters
    public TerrainStatus getTerrainStatus() {
        return terrainStatus;
    }
    
    public Player getPlayer1() {
        return player1;
    }
    
    public Player getPlayer2() {
        return player2;
    }
    
    public boolean isFlooded() {
        return terrainStatus == TerrainStatus.FLOODED;
    }
}

