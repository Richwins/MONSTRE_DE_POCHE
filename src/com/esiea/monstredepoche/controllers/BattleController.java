package com.esiea.monstredepoche.controllers;

import com.esiea.monstredepoche.models.BattleField;
import com.esiea.monstredepoche.models.Player;

/**
 * Contrôleur de combat.
 * Gère l'initialisation du combat, l'exécution des tours
 * et la vérification des conditions de victoire.
 */
public class BattleController {
    private BattleField field;
    private TurnManager turnManager;
    
    public BattleController(Player player1, Player player2) {
        this.field = new BattleField(player1, player2);
        this.turnManager = new TurnManager(field);
    }
    
    /**
     * Initialise le combat et affiche les informations de départ
     */
    public void initializeBattle() {
        System.out.println("\n=== Début du combat ===");
        System.out.println(field.getPlayer1().getName() + " vs " + field.getPlayer2().getName());
        System.out.println("\n" + field.getPlayer1().getActiveMonster() + " vs " + 
                          field.getPlayer2().getActiveMonster());
    }
    
    /**
     * Traite un tour de combat (exécute les actions des joueurs)
     */
    public void processTurn() {
        turnManager.executeActions();
    }
    
    /**
     * Vérifie s'il y a un gagnant
     * @return Le joueur gagnant, ou null si le combat continue
     */
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

