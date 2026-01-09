package com.esiea.monstredepoche;

import com.esiea.monstredepoche.controllers.GameController;
import com.esiea.monstredepoche.gui.MonsterGameApp;

import java.util.Scanner;

/**
 * Point d'entrée principal du jeu Monstre de Poche.
 * Permet de choisir entre l'interface console et l'interface graphique JavaFX.
 */
public class Main {
    /**
     * Méthode principale qui démarre le jeu
     * @param args Arguments de la ligne de commande
     *             - "gui" ou "--gui" pour lancer l'interface graphique
     *             - Aucun argument pour lancer l'interface console
     */
    public static void main(String[] args) {
        // Vérifier si l'utilisateur veut l'interface graphique
        boolean useGUI = false;
        if (args.length > 0) {
            String arg = args[0].toLowerCase();
            if (arg.equals("gui") || arg.equals("--gui") || arg.equals("-g")) {
                useGUI = true;
            }
        }
        
        // Si pas d'argument, demander à l'utilisateur
        if (args.length == 0) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("=== Monstre de Poche ===");
            System.out.println("Choisissez le mode d'affichage :");
            System.out.println("1. Interface console (par défaut)");
            System.out.println("2. Interface graphique (JavaFX)");
            System.out.print("Votre choix (1 ou 2) : ");
            
            String choice = scanner.nextLine().trim();
            if (choice.equals("2")) {
                useGUI = true;
            }
        }
        
        if (useGUI) {
            // Lancer l'interface graphique JavaFX
            try {
                System.out.println("Lancement de l'interface graphique...");
                System.out.println("Note: Les actions de combat s'affichent également dans la console.");
                System.out.println("Note: Assurez-vous que JavaFX est dans le classpath.");
                MonsterGameApp.launch(MonsterGameApp.class, args);
            } catch (Exception e) {
                System.err.println("Erreur lors du lancement de l'interface graphique : " + e.getMessage());
                System.err.println("JavaFX n'est peut-être pas dans le classpath.");
                System.err.println("Consultez README_JAVAFX.md pour plus d'informations.");
                System.err.println("\nLancement de l'interface console à la place...");
                GameController game = new GameController();
                game.startGame();
            }
        } else {
            // Lancer l'interface console (comportement original)
            GameController game = new GameController();
            game.startGame();
        }
    }
}

