package com.esiea.monstredepoche.controllers;

import com.esiea.monstredepoche.loaders.AttackLoader;
import com.esiea.monstredepoche.loaders.MonsterLoader;
import com.esiea.monstredepoche.models.*;
import com.esiea.monstredepoche.models.enums.AttackType;
import com.esiea.monstredepoche.models.enums.MonsterType;
import com.esiea.monstredepoche.models.items.Medicine;
import com.esiea.monstredepoche.models.items.Potion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Contrôleur principal du jeu.
 * Gère le flux global : initialisation, configuration des joueurs,
 * chargement des données, et orchestration des tours de combat.
 */
public class GameController {
    private Scanner scanner;
    private List<Monster> availableMonsters;
    private List<Attack> availableAttacks;
    private BattleController battleController;
    
    public GameController() {
        this.scanner = new Scanner(System.in);
        this.availableMonsters = new ArrayList<>();
        this.availableAttacks = new ArrayList<>();
    }
    
    public void startGame() {
        System.out.println("=== Monstre de Poche ===");
        System.out.println("Bienvenue dans le jeu de combat !\n");
        
        try {
            loadGameData();
            setupPlayers();
            battleController = new BattleController(setupPlayer1(), setupPlayer2());
            battleController.initializeBattle();
            runBattle();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement des données : " + e.getMessage());
        }
    }
    
    private void loadGameData() throws IOException {
        availableMonsters = MonsterLoader.parseMonsterFile("resources/monsters.txt");
        availableAttacks = AttackLoader.parseAttackFile("resources/attacks.txt");
        
        // Assigner des attaques aux monstres (simplifié - assigne les 4 premières attaques)
        for (Monster monster : availableMonsters) {
            int count = 0;
            for (Attack attack : availableAttacks) {
                if (count >= 4) break;
                monster.addAttack(attack);
                count++;
            }
        }
    }
    
    private void setupPlayers() {
        System.out.println("Chargement des monstres et attaques...");
        System.out.println("Monstres disponibles : " + availableMonsters.size());
        System.out.println("Attaques disponibles : " + availableAttacks.size() + "\n");
    }
    
    private Player setupPlayer1() {
        System.out.println("=== Configuration du Joueur 1 ===");
        System.out.print("Entrez votre nom : ");
        String name = scanner.nextLine();
        Player player = new Player(name);
        
        selectMonsters(player, "Joueur 1");
        giveItems(player);
        
        return player;
    }
    
    private Player setupPlayer2() {
        System.out.println("\n=== Configuration du Joueur 2 ===");
        System.out.print("Entrez votre nom : ");
        String name = scanner.nextLine();
        Player player = new Player(name);
        
        selectMonsters(player, "Joueur 2");
        giveItems(player);
        
        return player;
    }
    
    private void selectMonsters(Player player, String playerName) {
        System.out.println("\n" + playerName + ", sélectionnez 3 monstres :");
        for (int i = 0; i < availableMonsters.size(); i++) {
            System.out.println((i + 1) + ". " + availableMonsters.get(i));
        }
        
        List<Monster> selected = new ArrayList<>();
        while (selected.size() < 3) {
            System.out.print("Choisissez un monstre (1-" + availableMonsters.size() + ") : ");
            try {
                int choice = Integer.parseInt(scanner.nextLine()) - 1;
                if (choice >= 0 && choice < availableMonsters.size()) {
                    Monster monster = availableMonsters.get(choice);
                    // Créer une copie pour chaque joueur
                    Monster copy = createMonsterCopy(monster);
                    selected.add(copy);
                    player.addMonster(copy);
                    System.out.println("Monstre ajouté : " + copy.getName());
                } else {
                    System.out.println("Choix invalide !");
                }
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre valide !");
            }
        }
    }
    
    private Monster createMonsterCopy(Monster original) {
        // Créer une copie du monstre avec les mêmes caractéristiques
        // (simplifié - dans une vraie implémentation, il faudrait cloner)
        return original; // Pour l'instant, on réutilise (à améliorer)
    }
    
    private void giveItems(Player player) {
        // Donner 5 objets au joueur
        player.addItem(new Potion("Potion", "Restaure 20 PV", 20, 0, 0));
        player.addItem(new Potion("Super Potion", "Restaure 50 PV", 50, 0, 0));
        player.addItem(new Potion("Potion d'Attaque", "Augmente l'attaque de 10", 0, 10, 0));
        player.addItem(new Medicine("Antidote", "Soigne les altérations d'état", true, false));
        player.addItem(new Medicine("Séchoir", "Assèche le terrain", false, true));
    }
    
    private void runBattle() {
        while (true) {
            Player winner = battleController.checkWinner();
            if (winner != null) {
                System.out.println("\n=== " + winner.getName() + " remporte la victoire ! ===");
                break;
            }
            
            // Afficher l'état du jeu avant le tour
            displayGameState();
            
            // Tour du joueur 1
            processPlayerTurn(battleController.getField().getPlayer1());
            
            // Vérifier si le joueur 2 a perdu
            winner = battleController.checkWinner();
            if (winner != null) {
                System.out.println("\n=== " + winner.getName() + " remporte la victoire ! ===");
                break;
            }
            
            // Tour du joueur 2
            processPlayerTurn(battleController.getField().getPlayer2());
            
            // Exécuter le tour
            battleController.processTurn();
        }
        
        endGame();
    }
    
    private void displayGameState() {
        BattleField field = battleController.getField();
        Player player1 = field.getPlayer1();
        Player player2 = field.getPlayer2();
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("                    ÉTAT DU COMBAT");
        System.out.println("=".repeat(60));
        
        // État du terrain
        System.out.println("\n🌍 TERRAIN : " + getTerrainStatusString(field));
        
        // État du joueur 1
        System.out.println("\n" + "-".repeat(60));
        System.out.println("👤 " + player1.getName().toUpperCase());
        System.out.println("-".repeat(60));
        displayPlayerMonsters(player1);
        
        // État du joueur 2
        System.out.println("\n" + "-".repeat(60));
        System.out.println("👤 " + player2.getName().toUpperCase());
        System.out.println("-".repeat(60));
        displayPlayerMonsters(player2);
        
        System.out.println("\n" + "=".repeat(60) + "\n");
    }
    
    private void displayPlayerMonsters(Player player) {
        for (int i = 0; i < player.getMonsters().size(); i++) {
            Monster monster = player.getMonsters().get(i);
            boolean isActive = monster == player.getActiveMonster();
            String status = isActive ? "⚡ ACTIF" : "   ";
            
            // Barre de vie
            int barLength = 20;
            int hpPercent = 0;
            if (monster.getMaxHp() > 0) {
                hpPercent = (int) ((double) monster.getHp() / monster.getMaxHp() * barLength);
            }
            String hpBar = "█".repeat(Math.max(0, hpPercent)) + 
                          "░".repeat(Math.max(0, barLength - hpPercent));
            
            // État d'altération
            String statusCondition = getStatusConditionString(monster);
            
            System.out.printf("%s [%d] %s\n", status, i + 1, monster.getName());
            if (monster.isAlive()) {
                System.out.printf("     Points de vie: %s [%d/%d] %s\n", hpBar, monster.getHp(), monster.getMaxHp(), statusCondition);
                System.out.printf("     Type: %s | Attaque: %d | Défense: %d | Vitesse: %d\n", 
                                monster.getType(), monster.getAttack(), monster.getDefense(), monster.getSpeed());
            } else {
                System.out.println("     ❌ KO (0/" + monster.getMaxHp() + " Points de vie)");
            }
        }
    }
    
    private String getTerrainStatusString(BattleField field) {
        if (field.isFlooded()) {
            return "💧 INONDÉ (durée restante: " + getFloodDuration(field) + " tours)";
        } else {
            return "🌍 NORMAL";
        }
    }
    
    private int getFloodDuration(BattleField field) {
        return field.getFloodDuration();
    }
    
    private String getStatusConditionString(Monster monster) {
        switch (monster.getCurrentStatus()) {
            case PARALYZED:
                return "⚡ PARALYSÉ";
            case BURNED:
                return "🔥 BRÛLÉ";
            case POISONED:
                return "☠️ EMPOISONNÉ";
            default:
                return "";
        }
    }
    
    private void processPlayerTurn(Player player) {
        System.out.println("\n=== Tour de " + player.getName() + " ===");
        System.out.println("Monstre actif : " + player.getActiveMonster());
        
        System.out.println("\nActions disponibles :");
        System.out.println("1. Attaquer");
        System.out.println("2. Utiliser un objet");
        System.out.println("3. Changer de monstre");
        
        System.out.print("Votre choix : ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            
            TurnManager.Action action = new TurnManager.Action(
                TurnManager.Action.ActionType.ATTACK, player
            );
            
            switch (choice) {
                case 1:
                    action.type = TurnManager.Action.ActionType.ATTACK;
                    selectAttack(player, action);
                    break;
                case 2:
                    action.type = TurnManager.Action.ActionType.USE_ITEM;
                    selectItem(player, action);
                    break;
                case 3:
                    action.type = TurnManager.Action.ActionType.SWITCH_MONSTER;
                    selectMonster(player, action);
                    break;
                default:
                    System.out.println("Choix invalide !");
                    return;
            }
            
            battleController.getTurnManager().addAction(action);
        } catch (NumberFormatException e) {
            System.out.println("Veuillez entrer un nombre valide !");
        }
    }
    
    private void selectAttack(Player player, TurnManager.Action action) {
        Monster monster = player.getActiveMonster();
        
        // Récupérer les attaques spéciales (du type du monstre)
        List<Attack> specialAttacks = new ArrayList<>();
        AttackType monsterAttackType = convertMonsterTypeToAttackType(monster.getType());
        
        for (Attack attack : monster.getAttacks()) {
            if (attack.getType() == monsterAttackType) {
                specialAttacks.add(attack);
            }
        }
        
        // Vérifier s'il y a des attaques spéciales disponibles
        boolean hasSpecialAttack = specialAttacks.stream().anyMatch(Attack::canUse);
        
        // Menu de sélection du type d'attaque
        System.out.println("\nType d'attaque :");
        System.out.println("1. Attaque normale (mains nues)");
        if (hasSpecialAttack) {
            System.out.println("2. Attaque spéciale (" + monster.getType() + ")");
        }
        
        System.out.print("Votre choix : ");
        try {
            int typeChoice = Integer.parseInt(scanner.nextLine());
            
            if (typeChoice == 1) {
                // Attaque normale (mains nues) - dégâts de base
                action.attackIndex = -1;
            } else if (typeChoice == 2 && hasSpecialAttack) {
                // Afficher les attaques spéciales disponibles
                action.attackIndex = selectSpecificAttack(specialAttacks, monster.getAttacks());
            } else {
                // Par défaut, attaque normale (mains nues)
                action.attackIndex = -1;
            }
        } catch (NumberFormatException e) {
            // Par défaut, attaque normale (mains nues)
            action.attackIndex = -1;
        }
    }
    
    private int selectSpecificAttack(List<Attack> availableAttacks, List<Attack> allAttacks) {
        System.out.println("\nAttaques spéciales disponibles :");
        for (int i = 0; i < availableAttacks.size(); i++) {
            Attack attack = availableAttacks.get(i);
            String status = attack.canUse() ? "" : " (Épuisée)";
            System.out.println((i + 1) + ". " + attack.getName() + 
                             " (Puissance: " + attack.getPower() + 
                             ", Utilisations: " + attack.getNbUse() + "/" + attack.getMaxUses() + ")" + status);
        }
        System.out.print("Choisissez une attaque : ");
        try {
            int choice = Integer.parseInt(scanner.nextLine()) - 1;
            if (choice >= 0 && choice < availableAttacks.size()) {
                Attack selected = availableAttacks.get(choice);
                // Retourner l'index dans la liste complète des attaques
                return allAttacks.indexOf(selected);
            }
        } catch (NumberFormatException e) {
            // Ignorer
        }
        return 0;
    }
    
    private AttackType convertMonsterTypeToAttackType(MonsterType monsterType) {
        switch (monsterType) {
            case FOUDRE:
                return AttackType.ELECTRIC;
            case EAU:
                return AttackType.WATER;
            case TERRE:
                return AttackType.GROUND;
            case FEU:
                return AttackType.FIRE;
            case NATURE:
                return AttackType.NATURE;
            default:
                return AttackType.NORMAL;
        }
    }
    
    private void selectItem(Player player, TurnManager.Action action) {
        System.out.println("\nObjets disponibles :");
        for (int i = 0; i < player.getItems().size(); i++) {
            Item item = player.getItems().get(i);
            System.out.println((i + 1) + ". " + item.getName() + " - " + item.getDescription());
        }
        System.out.print("Choisissez un objet : ");
        try {
            action.targetIndex = Integer.parseInt(scanner.nextLine()) - 1;
        } catch (NumberFormatException e) {
            action.targetIndex = 0;
        }
    }
    
    private void selectMonster(Player player, TurnManager.Action action) {
        List<Monster> available = player.getAvailableMonsters();
        System.out.println("\nMonstres disponibles :");
        for (int i = 0; i < available.size(); i++) {
            System.out.println((i + 1) + ". " + available.get(i));
        }
        System.out.print("Choisissez un monstre : ");
        try {
            int choice = Integer.parseInt(scanner.nextLine()) - 1;
            if (choice >= 0 && choice < available.size()) {
                Monster selected = available.get(choice);
                action.targetIndex = player.getMonsters().indexOf(selected);
            }
        } catch (NumberFormatException e) {
            // Ignorer
        }
    }
    
    public void endGame() {
        System.out.println("\nMerci d'avoir joué !");
        scanner.close();
    }
}

