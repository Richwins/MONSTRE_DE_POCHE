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
        Scanner scanner = null;
        if (args.length == 0) {
            scanner = new Scanner(System.in);
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
            // Fermer le scanner avant de lancer JavaFX pour éviter les conflits
            if (scanner != null) {
                scanner.close();
            }
            
            // Lancer l'interface graphique JavaFX
            try {
                System.out.println("Lancement de l'interface graphique JavaFX...");
                System.out.println("Note: Les actions de combat s'affichent également dans la console.");
                System.out.println("Note: Le SDK JavaFX est inclus dans le projet (javafx-sdk-17.0.17/).");
                MonsterGameApp.launch(MonsterGameApp.class, args);
            } catch (NoClassDefFoundError e) {
                // Erreur JavaFX non trouvé - l'utilisateur doit relancer avec les bons arguments
                System.err.println("\n❌ ERREUR : JavaFX n'est pas disponible dans le classpath/module-path actuel.");
                System.err.println("\nPour lancer le mode GUI, vous devez utiliser la commande complète avec JavaFX :\n");
                System.err.println("Windows (PowerShell) :");
                System.err.println("  $javafxPath = \"javafx-sdk-17.0.17\\lib\"");
                System.err.println("  $binPath = \"javafx-sdk-17.0.17\\bin\"");
                System.err.println("  $env:PATH = \"$binPath;$env:PATH\"");
                System.err.println("  java --module-path \"$javafxPath\" --add-modules javafx.controls,javafx.fxml,javafx.graphics -cp \"out\\production\\MONSTRE_DE_POCHE;resources\" com.esiea.monstredepoche.Main gui\n");
                System.err.println("OU relancez simplement avec l'argument 'gui' :");
                System.err.println("  java --module-path \"javafx-sdk-17.0.17\\lib\" --add-modules javafx.controls,javafx.fxml,javafx.graphics -cp \"out\\production\\MONSTRE_DE_POCHE;resources\" com.esiea.monstredepoche.Main gui\n");
                System.err.println("Consultez README.md pour plus d'informations.");
            } catch (Exception e) {
                System.err.println("Erreur lors du lancement de l'interface graphique : " + e.getMessage());
                e.printStackTrace();
                System.err.println("\nVérifiez que :");
                System.err.println("1. Le SDK JavaFX est présent dans javafx-sdk-17.0.17/");
                System.err.println("2. Les JARs JavaFX sont dans javafx-sdk-17.0.17/lib/");
                System.err.println("3. Les DLL natives sont dans javafx-sdk-17.0.17/bin/");
                System.err.println("4. Le projet est compilé (out/production/MONSTRE_DE_POCHE existe)");
                System.err.println("\nConsultez README.md pour plus d'informations.");
            }
        } else {
            // Lancer l'interface console
            if (scanner == null) {
                scanner = new Scanner(System.in);
            }
            System.out.println("\n=== Monstre de Poche ===");
            System.out.println("Choisissez le mode de jeu :");
            System.out.println("1. Mode Deux Joueurs (1v1)");
            System.out.println("2. Mode Solo (vs Bot)");
            System.out.print("Votre choix (1 ou 2) : ");
            
            String gameMode = scanner.nextLine().trim();
            GameController game = new GameController();
            
            if (gameMode.equals("2")) {
                // Mode solo contre le bot
                System.out.print("Entrez votre nom : ");
                String playerName = scanner.nextLine().trim();
                if (playerName.isEmpty()) {
                    playerName = "Joueur";
                }
                game.startSoloGame(playerName);
            } else {
                // Mode deux joueurs (par défaut)
                game.startGame();
            }
        }
    }
}

