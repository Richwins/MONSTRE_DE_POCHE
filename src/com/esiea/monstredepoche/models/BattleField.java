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
    private Monster floodSource; // Le monstre qui a causé l'inondation
    private Player player1;
    private Player player2;
    
    public BattleField(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.terrainStatus = TerrainStatus.NORMAL;
        this.floodDuration = 0;
        this.floodSource = null;
    }
    
    /**
     * Applique les effets du terrain sur les monstres actifs.
     * En cas d'inondation : 
     * - Les monstres Nature qui ont utilisé une attaque spéciale récupèrent 1/20 de leurs PV max
     * - Guérit brûlures/empoisonnements
     */
    public void applyTerrainEffects() {
        if (terrainStatus == TerrainStatus.FLOODED) {
            // Les monstres de type Nature (Plante ou Insecte) récupèrent 1/20 de leurs PV max
            // SEULEMENT s'ils ont utilisé une attaque spéciale ce tour
            if (player1.getActiveMonster() != null) {
                Monster m1 = player1.getActiveMonster();
                if (MonsterType.isNatureType(m1.getType()) && m1.isAlive() && m1.hasUsedSpecialAbility()) {
                    int healAmount = m1.getMaxHp() / 20;
                    if (healAmount > 0) {
                        m1.heal(healAmount);
                        System.out.println(m1.getName() + " récupère " + healAmount + " PV grâce au terrain inondé !");
                    }
                }
            }
            if (player2.getActiveMonster() != null) {
                Monster m2 = player2.getActiveMonster();
                if (MonsterType.isNatureType(m2.getType()) && m2.isAlive() && m2.hasUsedSpecialAbility()) {
                    int healAmount = m2.getMaxHp() / 20;
                    if (healAmount > 0) {
                        m2.heal(healAmount);
                        System.out.println(m2.getName() + " récupère " + healAmount + " PV grâce au terrain inondé !");
                    }
                }
            }
            
            // Guérit les brûlures et empoisonnements
            if (player1.getActiveMonster() != null) {
                Monster m1 = player1.getActiveMonster();
                if (m1.getCurrentStatus() == StatusCondition.BURNED) {
                    m1.setCurrentStatus(StatusCondition.NONE);
                    System.out.println(m1.getName() + " est soigné de sa brûlure par l'inondation !");
                } else if (m1.getCurrentStatus() == StatusCondition.POISONED) {
                    m1.setCurrentStatus(StatusCondition.NONE);
                    System.out.println(m1.getName() + " est soigné de son empoisonnement par l'inondation !");
                }
            }
            if (player2.getActiveMonster() != null) {
                Monster m2 = player2.getActiveMonster();
                if (m2.getCurrentStatus() == StatusCondition.BURNED) {
                    m2.setCurrentStatus(StatusCondition.NONE);
                    System.out.println(m2.getName() + " est soigné de sa brûlure par l'inondation !");
                } else if (m2.getCurrentStatus() == StatusCondition.POISONED) {
                    m2.setCurrentStatus(StatusCondition.NONE);
                    System.out.println(m2.getName() + " est soigné de son empoisonnement par l'inondation !");
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
     * @param source Le monstre qui a causé l'inondation
     */
    public void setFlooded(int duration, Monster source) {
        this.terrainStatus = TerrainStatus.FLOODED;
        this.floodDuration = duration;
        this.floodSource = source;
        System.out.println("Le terrain est inondé pour " + duration + " tour(s) !");
    }
    
    /**
     * Remet le terrain à l'état normal
     */
    public void setNormal() {
        if (this.terrainStatus == TerrainStatus.FLOODED) {
            System.out.println("Le terrain n'est plus inondé.");
        }
        this.terrainStatus = TerrainStatus.NORMAL;
        this.floodDuration = 0;
        this.floodSource = null;
    }
    
    /**
     * Vérifie si le monstre source de l'inondation est toujours actif
     * Si non, retire l'inondation
     */
    public void checkFloodSource() {
        if (terrainStatus == TerrainStatus.FLOODED && floodSource != null) {
            // Vérifier si le monstre source est toujours actif sur le terrain
            boolean sourceIsActive = (player1.getActiveMonster() == floodSource) || 
                                     (player2.getActiveMonster() == floodSource);
            
            if (!sourceIsActive) {
                setNormal();
            }
        }
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
    
    public Monster getFloodSource() {
        return floodSource;
    }
}

