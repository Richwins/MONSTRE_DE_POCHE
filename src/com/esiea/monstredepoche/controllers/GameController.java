package com.esiea.monstredepoche.controllers;

import com.esiea.monstredepoche.loaders.AttackLoader;
import com.esiea.monstredepoche.loaders.MonsterLoader;
import com.esiea.monstredepoche.models.*;
import com.esiea.monstredepoche.models.enums.AttackType;
import com.esiea.monstredepoche.models.enums.MonsterType;
import com.esiea.monstredepoche.models.monsters.PlantMonster;
import com.esiea.monstredepoche.models.monsters.InsectMonster;
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
    private Bot bot; // Bot pour le mode solo
    
    public GameController() {
        this.scanner = new Scanner(System.in);
        this.availableMonsters = new ArrayList<>();
        this.availableAttacks = new ArrayList<>();
        
        // Charger les données au démarrage pour que GUI et console utilisent les mêmes données
        try {
            loadGameData();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement des données : " + e.getMessage());
        }
    }
    
    /**
     * Démarre le jeu en mode console.
     * Charge les données, configure les joueurs et lance le combat.
     * Les données sont déjà chargées dans le constructeur pour que GUI et console utilisent les mêmes.
     */
    /**
     * Démarre le jeu en mode console.
     * Charge les données, configure les joueurs et lance le combat.
     * Les données sont déjà chargées dans le constructeur pour que GUI et console utilisent les mêmes.
     */
    public void startGame() {
        System.out.println("=== Monstre de Poche ===");
        System.out.println("Bienvenue dans le jeu de combat !\n");
        
        // Les données sont déjà chargées dans le constructeur
        setupPlayers();
        battleController = new BattleController(setupPlayer1(), setupPlayer2());
        battleController.initializeBattle();
        runBattle();
    }
    
    /**
     * Charge les données du jeu (monstres et attaques).
     * Cette méthode est appelée automatiquement dans le constructeur
     * pour que GUI et console utilisent les mêmes données.
     */
    private void loadGameData() throws IOException {
        availableMonsters = MonsterLoader.parseMonsterFile("resources/monsters.txt");
        availableAttacks = AttackLoader.parseAttackFile("resources/attacks.txt");
        
        // Assigner des attaques aux monstres selon leur type
        assignAttacksToMonsters();
    }
    
    /**
     * Retourne la liste des monstres disponibles.
     * Utilisé par le GUI pour afficher les monstres sélectionnables.
     * @return Liste des monstres disponibles
     */
    public List<Monster> getAvailableMonsters() {
        return new ArrayList<>(availableMonsters); // Retourne une copie pour éviter les modifications
    }
    
    /**
     * Retourne la liste des attaques disponibles.
     * @return Liste des attaques disponibles
     */
    public List<Attack> getAvailableAttacks() {
        return new ArrayList<>(availableAttacks); // Retourne une copie
    }
    
    /**
     * Assigner des attaques aux monstres selon leur type élémentaire.
     * Chaque monstre reçoit des attaques de son type + une attaque normale.
     */
    private void assignAttacksToMonsters() {
        for (Monster monster : availableMonsters) {
            AttackType monsterAttackType = convertMonsterTypeToAttackType(monster.getType());
            
            // Trouver les attaques correspondant au type du monstre
            List<Attack> matchingAttacks = new ArrayList<>();
            for (Attack attack : availableAttacks) {
                if (attack.getType() == monsterAttackType) {
                    matchingAttacks.add(attack);
                }
            }
            
            // Ajouter une attaque normale si disponible
            Attack normalAttack = null;
            for (Attack attack : availableAttacks) {
                if (attack.getType() == AttackType.NORMAL) {
                    normalAttack = attack;
                    break;
                }
            }
            
            // Assigner jusqu'à 3 attaques spéciales + 1 normale (max 4)
            int specialCount = 0;
            for (Attack attack : matchingAttacks) {
                if (specialCount >= 3) break;
                monster.addAttack(attack);
                specialCount++;
            }
            
            // Ajouter l'attaque normale si disponible et qu'on n'a pas atteint la limite
            if (normalAttack != null && monster.getAttacks().size() < 4) {
                monster.addAttack(normalAttack);
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
    
    /**
     * Menu de sélection d'équipe amélioré.
     * Permet de choisir 3 monstres avec affichage détaillé et possibilité de voir les détails.
     */
    private void selectMonsters(Player player, String playerName) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("  SÉLECTION D'ÉQUIPE - " + playerName.toUpperCase());
        System.out.println("=".repeat(50));
        
        List<Monster> selected = new ArrayList<>();
        
        while (selected.size() < 3) {
            // Afficher l'équipe actuelle
            displayCurrentTeam(selected, playerName);
            
            // Afficher les monstres disponibles
            displayAvailableMonsters(selected);
            
            System.out.println("\n" + "-".repeat(50));
            System.out.println("Commandes disponibles :");
            System.out.println("  - Entrez un numéro (1-" + availableMonsters.size() + ") pour sélectionner un monstre");
            System.out.println("  - Entrez 'd' + numéro (ex: d1) pour voir les détails d'un monstre");
            if (!selected.isEmpty()) {
                System.out.println("  - Entrez 'r' + numéro (ex: r1) pour retirer un monstre de l'équipe");
            }
            System.out.print("\nVotre choix : ");
            
            String input = scanner.nextLine().trim().toLowerCase();
            
            if (input.startsWith("d")) {
                // Afficher les détails d'un monstre
                try {
                    int index = Integer.parseInt(input.substring(1)) - 1;
                    if (index >= 0 && index < availableMonsters.size()) {
                        displayMonsterDetails(availableMonsters.get(index));
                    } else {
                        System.out.println("❌ Numéro invalide !");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("❌ Format invalide ! Utilisez 'd' suivi d'un numéro (ex: d1)");
                }
            } else if (input.startsWith("r") && !selected.isEmpty()) {
                // Retirer un monstre de l'équipe
                try {
                    int index = Integer.parseInt(input.substring(1)) - 1;
                    if (index >= 0 && index < selected.size()) {
                        Monster removed = selected.remove(index);
                        player.getMonsters().remove(removed);
                        System.out.println("✅ " + removed.getName() + " retiré de l'équipe.");
                    } else {
                        System.out.println("❌ Numéro invalide !");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("❌ Format invalide ! Utilisez 'r' suivi d'un numéro (ex: r1)");
                }
            } else {
                // Sélectionner un monstre
                try {
                    int choice = Integer.parseInt(input) - 1;
                    if (choice >= 0 && choice < availableMonsters.size()) {
                        Monster monster = availableMonsters.get(choice);
                        
                        // Vérifier si le monstre n'est pas déjà sélectionné
                        if (selected.contains(monster)) {
                            System.out.println("❌ Ce monstre est déjà dans votre équipe !");
                        } else {
                            // Créer une copie pour chaque joueur
                            Monster copy = createMonsterCopy(monster);
                            selected.add(copy);
                            player.addMonster(copy);
                            System.out.println("✅ " + copy.getName() + " ajouté à l'équipe !");
                        }
                    } else {
                        System.out.println("❌ Choix invalide !");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("❌ Veuillez entrer un nombre valide !");
                }
            }
        }
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("✅ Équipe complète pour " + playerName + " !");
        displayCurrentTeam(selected, playerName);
        System.out.println("=".repeat(50) + "\n");
    }
    
    /**
     * Affiche l'équipe actuellement sélectionnée.
     */
    private void displayCurrentTeam(List<Monster> selected, String playerName) {
        if (selected.isEmpty()) {
            System.out.println("\n📋 Équipe actuelle : Aucun monstre sélectionné (0/3)");
        } else {
            System.out.println("\n📋 Équipe actuelle (" + selected.size() + "/3) :");
            for (int i = 0; i < selected.size(); i++) {
                Monster m = selected.get(i);
                System.out.println("  " + (i + 1) + ". " + m.getName() + 
                                 " (" + getMonsterTypeDisplay(m) + 
                                 " | PV: " + m.getHp() + "/" + m.getMaxHp() + 
                                 " | ATK: " + m.getAttack() + 
                                 " | DEF: " + m.getDefense() + 
                                 " | SPD: " + m.getSpeed() + ")");
            }
        }
    }
    
    /**
     * Affiche la liste des monstres disponibles avec leurs statistiques principales.
     */
    private void displayAvailableMonsters(List<Monster> selected) {
        System.out.println("\n📦 Monstres disponibles :");
        for (int i = 0; i < availableMonsters.size(); i++) {
            Monster monster = availableMonsters.get(i);
            String status = selected.contains(monster) ? " [DÉJÀ SÉLECTIONNÉ]" : "";
            System.out.println(String.format("  %2d. %-15s | Type: %-8s | PV: %3d | ATK: %3d | DEF: %3d | SPD: %3d%s",
                i + 1,
                monster.getName(),
                getMonsterTypeDisplay(monster),
                monster.getMaxHp(),
                monster.getAttack(),
                monster.getDefense(),
                monster.getSpeed(),
                status));
        }
    }
    
    /**
     * Affiche les détails complets d'un monstre.
     */
    private void displayMonsterDetails(Monster monster) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("  DÉTAILS : " + monster.getName().toUpperCase());
        System.out.println("=".repeat(50));
        System.out.println("Type          : " + getMonsterTypeDisplay(monster));
        System.out.println("Points de vie : " + monster.getHp() + "/" + monster.getMaxHp());
        System.out.println("Attaque       : " + monster.getAttack());
        System.out.println("Défense       : " + monster.getDefense());
        System.out.println("Vitesse       : " + monster.getSpeed());
        
        // Afficher les attaques
        System.out.println("\nAttaques disponibles (" + monster.getAttacks().size() + ") :");
        if (monster.getAttacks().isEmpty()) {
            System.out.println("  Aucune attaque assignée");
        } else {
            for (int i = 0; i < monster.getAttacks().size(); i++) {
                Attack attack = monster.getAttacks().get(i);
                System.out.println(String.format("  %d. %-20s | Puissance: %3d | Utilisations: %2d/%2d",
                    i + 1,
                    attack.getName(),
                    attack.getPower(),
                    attack.getNbUse(),
                    attack.getMaxUses()));
            }
        }
        System.out.println("=".repeat(50) + "\n");
    }
    
    /**
     * Crée une copie complète d'un monstre avec toutes ses caractéristiques.
     * Nécessaire pour que chaque joueur ait sa propre instance.
     * Recrée le monstre selon son type avec les mêmes paramètres.
     * 
     * @param original Le monstre à copier
     * @return Une copie complète du monstre
     */
    public Monster createMonsterCopy(Monster original) {
        // Créer une copie selon le type de monstre
        if (original instanceof com.esiea.monstredepoche.models.monsters.ElectricMonster) {
            com.esiea.monstredepoche.models.monsters.ElectricMonster electric = 
                (com.esiea.monstredepoche.models.monsters.ElectricMonster) original;
            Monster copy = new com.esiea.monstredepoche.models.monsters.ElectricMonster(
                original.getName(),
                original.getMaxHp(),
                original.getSpeed(),
                original.getAttack(),
                original.getDefense(),
                electric.getParalysisChance()
            );
            copyAttacks(original, copy);
            return copy;
        } else if (original instanceof com.esiea.monstredepoche.models.monsters.WaterMonster) {
            com.esiea.monstredepoche.models.monsters.WaterMonster water = 
                (com.esiea.monstredepoche.models.monsters.WaterMonster) original;
            Monster copy = new com.esiea.monstredepoche.models.monsters.WaterMonster(
                original.getName(),
                original.getMaxHp(),
                original.getSpeed(),
                original.getAttack(),
                original.getDefense(),
                water.getFloodChance(),
                water.getFallChance()
            );
            copyAttacks(original, copy);
            return copy;
        } else if (original instanceof com.esiea.monstredepoche.models.monsters.GroundMonster) {
            com.esiea.monstredepoche.models.monsters.GroundMonster ground = 
                (com.esiea.monstredepoche.models.monsters.GroundMonster) original;
            Monster copy = new com.esiea.monstredepoche.models.monsters.GroundMonster(
                original.getName(),
                original.getMaxHp(),
                original.getSpeed(),
                original.getAttack(),
                original.getDefense(),
                ground.getDigChance()
            );
            copyAttacks(original, copy);
            return copy;
        } else if (original instanceof com.esiea.monstredepoche.models.monsters.FireMonster) {
            com.esiea.monstredepoche.models.monsters.FireMonster fire = 
                (com.esiea.monstredepoche.models.monsters.FireMonster) original;
            Monster copy = new com.esiea.monstredepoche.models.monsters.FireMonster(
                original.getName(),
                original.getMaxHp(),
                original.getSpeed(),
                original.getAttack(),
                original.getDefense(),
                fire.getBurnChance()
            );
            copyAttacks(original, copy);
            return copy;
        } else if (original instanceof PlantMonster) {
            PlantMonster plant = (PlantMonster) original;
            Monster copy = new PlantMonster(
                original.getName(),
                original.getMaxHp(),
                original.getSpeed(),
                original.getAttack(),
                original.getDefense(),
                plant.getHealChance()
            );
            copyAttacks(original, copy);
            return copy;
        } else if (original instanceof InsectMonster) {
            Monster copy = new InsectMonster(
                original.getName(),
                original.getMaxHp(),
                original.getSpeed(),
                original.getAttack(),
                original.getDefense()
            );
            copyAttacks(original, copy);
            return copy;
        }
        
        // Fallback (ne devrait jamais arriver)
        return original;
    }
    
    /**
     * Copie les attaques d'un monstre vers un autre.
     */
    private void copyAttacks(Monster source, Monster target) {
        for (Attack attack : source.getAttacks()) {
            // Créer une copie de l'attaque pour éviter les références partagées
            Attack attackCopy = new Attack(
                attack.getName(),
                attack.getType(),
                attack.getPower(),
                attack.getMaxUses(),
                attack.getFailProbability()
            );
            target.addAttack(attackCopy);
        }
    }
    
    private void giveItems(Player player) {
        // Donner 5 objets au joueur
        player.addItem(new Potion("Potion", "Restaure 20 PV", 20, 0, 0));
        player.addItem(new Potion("Super Potion", "Restaure 50 PV", 50, 0, 0));
        player.addItem(new Potion("Potion d'Attaque", "Augmente l'attaque de 10", 0, 10, 0));
        player.addItem(new Medicine("Antidote", "Soigne les altérations d'état", true, false));
        player.addItem(new Medicine("Séchoir", "Assèche le terrain", false, true));
    }
    
    /**
     * Donne des objets à un joueur.
     * Méthode publique pour être utilisée par le GUI.
     * @param player Le joueur à qui donner les objets
     */
    public void giveItemsToPlayer(Player player) {
        giveItems(player);
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
            
            // Tour du joueur 2 (humain ou bot)
            if (bot != null && battleController.getField().getPlayer2() == bot.getPlayer()) {
                // Le joueur 2 est le bot, prendre une décision automatique
                processBotTurn();
            } else {
                // Le joueur 2 est humain
                processPlayerTurn(battleController.getField().getPlayer2());
            }
            
            // Exécuter le tour
            battleController.processTurn();
        }
        
        endGame();
    }
    
    /**
     * Traite le tour du bot (décision automatique)
     */
    private void processBotTurn() {
        if (bot == null) {
            return;
        }
        
        System.out.println("\n=== Tour de " + bot.getName() + " ===");
        System.out.println("Monstre actif : " + bot.getPlayer().getActiveMonster());
        
        TurnManager.Action action = bot.makeDecision();
        if (action != null) {
            battleController.getTurnManager().addAction(action);
        } else {
            System.out.println("🤖 " + bot.getName() + " ne peut pas agir.");
        }
    }
    
    /**
     * Démarre un jeu en mode solo (joueur vs bot)
     * @param playerName Le nom du joueur humain
     */
    public void startSoloGame(String playerName) {
        System.out.println("=== Monstre de Poche - Mode Solo ===");
        System.out.println("Bienvenue " + playerName + " ! Vous allez affronter un bot.\n");
        
        // Créer le bot
        bot = new Bot("Bot");
        
        // Configurer le joueur humain
        Player humanPlayer = new Player(playerName);
        selectMonsters(humanPlayer, playerName);
        giveItems(humanPlayer);
        
        // Configurer le bot (sélection aléatoire de monstres)
        Player botPlayer = new Player("Bot");
        selectRandomMonstersForBot(botPlayer);
        giveItems(botPlayer);
        bot.setPlayer(botPlayer);
        
        // Initialiser le combat
        battleController = new BattleController(humanPlayer, botPlayer);
        battleController.initializeBattle();
        
        // Lancer le combat
        runBattle();
    }
    
    /**
     * Sélectionne aléatoirement 3 monstres pour le bot
     * @param botPlayer Le joueur bot
     */
    private void selectRandomMonstersForBot(Player botPlayer) {
        System.out.println("\n=== Configuration du Bot ===");
        System.out.println("Le bot sélectionne son équipe aléatoirement...");
        
        List<Monster> available = new ArrayList<>(availableMonsters);
        List<Monster> selected = new ArrayList<>();
        
        // Sélectionner 3 monstres aléatoirement
        for (int i = 0; i < 3 && !available.isEmpty(); i++) {
            int randomIndex = com.esiea.monstredepoche.utils.RandomGenerator.randomInRange(0, available.size() - 1);
            Monster selectedMonster = available.remove(randomIndex);
            Monster copy = createMonsterCopy(selectedMonster);
            selected.add(copy);
            botPlayer.addMonster(copy);
            System.out.println("🤖 Bot sélectionne : " + copy.getName());
        }
        
        System.out.println("✅ Équipe du Bot complète !\n");
    }
    
    /**
     * Retourne le bot actuel (pour le GUI)
     * @return Le bot ou null si pas de mode solo
     */
    public Bot getBot() {
        return bot;
    }
    
    /**
     * Définit le bot (pour le GUI)
     * @param bot Le bot
     */
    public void setBot(Bot bot) {
        this.bot = bot;
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
                                getMonsterTypeDisplay(monster), monster.getAttack(), monster.getDefense(), monster.getSpeed());
            } else {
                System.out.println("     ❌ KO (0/" + monster.getMaxHp() + " Points de vie)");
            }
        }
    }
    
    /**
     * Retourne le nom d'affichage du type de monstre en français.
     * Convertit les types anglais de l'enum en français pour l'affichage.
     * Utilisé par le GUI et la console pour un affichage uniforme.
     * 
     * @param monster Le monstre dont on veut afficher le type
     * @return Le nom du type en français (ex: "Foudre", "Eau", "Plante", "Insecte")
     */
    public String getMonsterTypeDisplay(Monster monster) {
        if (monster instanceof PlantMonster) {
            return "Plante";
        } else if (monster instanceof InsectMonster) {
            return "Insecte";
        } else {
            // Convertit les types anglais en français pour l'affichage
            switch (monster.getType()) {
                case ELECTRIC:
                    return "Foudre";
                case WATER:
                    return "Eau";
                case GROUND:
                    return "Terre";
                case FIRE:
                    return "Feu";
                case PLANT:
                    return "Plante";
                case INSECT:
                    return "Insecte";
                default:
                    return monster.getType().toString();
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
            System.out.println("2. Attaque spéciale (" + getMonsterTypeDisplay(monster) + ")");
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
            case ELECTRIC:
                return AttackType.ELECTRIC;
            case WATER:
                return AttackType.WATER;
            case GROUND:
                return AttackType.GROUND;
            case FIRE:
                return AttackType.FIRE;
            case PLANT:
            case INSECT:
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

