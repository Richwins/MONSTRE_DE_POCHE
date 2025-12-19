package com.esiea.monstredepoche;

import com.esiea.monstredepoche.controllers.GameController;

/**
 * Point d'entrée principal du jeu Monstre de Poche.
 * Lance le contrôleur de jeu qui gère toute l'application.
 */
public class Main {
    /**
     * Méthode principale qui démarre le jeu
     * @param args Arguments de la ligne de commande (non utilisés)
     */
    public static void main(String[] args) {
        GameController game = new GameController();
        game.startGame();
    }
}

