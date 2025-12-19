package com.esiea.monstredepoche.models;

import com.esiea.monstredepoche.models.enums.MonsterType;
import com.esiea.monstredepoche.models.enums.StatusCondition;
import com.esiea.monstredepoche.models.enums.TerrainStatus;

/**
 * Représente le terrain de combat.
 * Gère l'état du terrain (normal ou inondé) et applique les effets
 * du terrain sur les monstres (soins pour Nature, guérison des brûlures/empoisonnements).
 */
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
    
    /**
     * Applique les effets du terrain sur les monstres actifs.
     * En cas d'inondation : soigne les monstres Nature et guérit brûlures/empoisonnements.
     */
    public void applyTerrainEffects() {
        if (terrainStatus == TerrainStatus.FLOODED) {
            // Les monstres de type Nature récupèrent des PV
            if (player1.getActiveMonster() != null) {
                Monster m1 = player1.getActiveMonster();
                if (m1.getType() == MonsterType.NATURE) {
                    m1.heal(5);
                }
            }
            if (player2.getActiveMonster() != null) {
                Monster m2 = player2.getActiveMonster();
                if (m2.getType() == MonsterType.NATURE) {
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
    
    /**
     * Met à jour l'état du terrain (réduit la durée d'inondation si applicable)
     */
    public void updateTerrain() {
        if (terrainStatus == TerrainStatus.FLOODED) {
            floodDuration--;
            if (floodDuration <= 0) {
                terrainStatus = TerrainStatus.NORMAL;
            }
        }
    }
    
    /**
     * Inonde le terrain pour une durée donnée
     * @param duration Nombre de tours d'inondation
     */
    public void setFlooded(int duration) {
        this.terrainStatus = TerrainStatus.FLOODED;
        this.floodDuration = duration;
    }
    
    /**
     * Remet le terrain à l'état normal
     */
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
    
    public int getFloodDuration() {
        return floodDuration;
    }
}

