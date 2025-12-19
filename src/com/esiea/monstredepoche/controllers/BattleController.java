package com.esiea.monstredepoche.controllers;

import com.esiea.monstredepoche.models.BattleField;
import com.esiea.monstredepoche.models.Player;

public class BattleController {
    private BattleField field;
    private TurnManager turnManager;
    
    public BattleController(Player player1, Player player2) {
        this.field = new BattleField(player1, player2);
        this.turnManager = new TurnManager(field);
    }
    
    public void initializeBattle() {
        System.out.println("\n=== Début du combat ===");
        System.out.println(field.getPlayer1().getName() + " vs " + field.getPlayer2().getName());
        System.out.println("\n" + field.getPlayer1().getActiveMonster() + " vs " + 
                          field.getPlayer2().getActiveMonster());
    }
    
    public void processTurn() {
        turnManager.executeActions();
    }
    
    public Player checkWinner() {
        if (field.getPlayer1().hasLost()) {
            return field.getPlayer2();
        }
        if (field.getPlayer2().hasLost()) {
            return field.getPlayer1();
        }
        return null;
    }
    
    public BattleField getField() {
        return field;
    }
    
    public TurnManager getTurnManager() {
        return turnManager;
    }
}

