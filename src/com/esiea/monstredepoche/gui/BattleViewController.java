package com.esiea.monstredepoche.gui;

import com.esiea.monstredepoche.controllers.BattleController;
import com.esiea.monstredepoche.controllers.GameController;
import com.esiea.monstredepoche.controllers.TurnManager;
import com.esiea.monstredepoche.models.*;
import com.esiea.monstredepoche.models.enums.AttackType;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.TranslateTransition;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.ParallelTransition;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contrôleur pour la vue de combat complète.
 * Affiche toutes les informations comme la version console avec le style dark/neon.
 */
public class BattleViewController {
    private MonsterGameApp app;
    private GameController gameController;
    private BattleController battleController;
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private boolean soloMode = false;
    private boolean waitingForPlayer1Action = false;
    private boolean waitingForPlayer2Action = false;
    
    private BorderPane root;
    private VBox player1Area;
    private VBox player2Area;
    private VBox centerArea;
    private VBox actionArea;
    private Label terrainLabel;
    private Label statusLabel;
    private Label currentPlayerLabel;
    
    public BattleViewController(MonsterGameApp app, GameController gameController) {
        this.app = app;
        this.gameController = gameController;
    }
    
    public Scene createScene() {
        root = new BorderPane();
        // Background dark
        root.setBackground(new Background(new BackgroundFill(Color.web("#0A0E0D"), CornerRadii.EMPTY, Insets.EMPTY)));
        root.setPadding(new Insets(20));
        
        // Initialiser le combat seulement si les joueurs ne sont pas encore définis
        // (si setPlayers() a déjà été appelé, ne pas réinitialiser)
        if (player1 == null || player2 == null || battleController == null) {
            initializeBattle();
        }
        
        // Zone centrale (terrain et statut)
        centerArea = new VBox(15);
        centerArea.setAlignment(Pos.CENTER);
        centerArea.setPadding(new Insets(20));
        centerArea.setStyle(
            "-fx-background-color: #1A1F1E;" +
            "-fx-background-radius: 15;" +
            "-fx-border-color: #39FF14;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 15;"
        );
        
        terrainLabel = new Label();
        terrainLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        terrainLabel.setTextFill(Color.web("#39FF14")); // Vert Néon
        
        currentPlayerLabel = new Label();
        currentPlayerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        currentPlayerLabel.setTextFill(Color.web("#00FFFF")); // Cyan Néon
        
        statusLabel = new Label("En attente du début du combat...");
        statusLabel.setFont(Font.font("Arial", 16));
        statusLabel.setTextFill(Color.web("#E0E0E0")); // Gris Clair
        
        centerArea.getChildren().addAll(terrainLabel, currentPlayerLabel, statusLabel);
        
        // Zone joueur 1 (gauche)
        player1Area = createPlayerArea("Joueur 1", Color.web("#0D1F12"));
        
        // Zone joueur 2 (droite)
        player2Area = createPlayerArea("Joueur 2", Color.web("#0D1F12"));
        
        // Zone d'actions (bas)
        actionArea = createActionArea();
        
        // Layout principal
        HBox playersBox = new HBox(20);
        playersBox.getChildren().addAll(player1Area, centerArea, player2Area);
        HBox.setHgrow(player1Area, Priority.ALWAYS);
        HBox.setHgrow(centerArea, Priority.ALWAYS);
        HBox.setHgrow(player2Area, Priority.ALWAYS);
        
        // Layout principal : joueurs uniquement
        root.setCenter(playersBox);
        root.setBottom(actionArea);
        
        // Mettre à jour l'affichage
        updateDisplay();
        
        return new Scene(root, 1400, 900);
    }
    
    private void initializeBattle() {
        if (battleController == null) {
            player1 = new Player("Joueur 1");
            player2 = new Player("Joueur 2");
            battleController = new BattleController(player1, player2);
            battleController.initializeBattle();
        }
        
        currentPlayer = player1;
        waitingForPlayer1Action = true;
        waitingForPlayer2Action = false;
    }
    
    private VBox createPlayerArea(String playerName, Color bgColor) {
        VBox area = new VBox(10);
        area.setAlignment(Pos.TOP_CENTER);
        area.setPadding(new Insets(15));
        area.setBackground(new Background(new BackgroundFill(bgColor, CornerRadii.EMPTY, Insets.EMPTY)));
        area.setStyle(
            "-fx-border-color: #39FF14;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 10;"
        );
        area.setPrefWidth(400);
        
        Label nameLabel = new Label(playerName);
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        nameLabel.setTextFill(Color.web("#39FF14")); // Vert Néon
        
        area.getChildren().add(nameLabel);
        
        return area;
    }
    
    private VBox createActionArea() {
        VBox area = new VBox(10);
        area.setAlignment(Pos.CENTER);
        area.setPadding(new Insets(20));
        area.setStyle(
            "-fx-background-color: #1A1F1E;" +
            "-fx-background-radius: 15;" +
            "-fx-border-color: #39FF14;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 15;"
        );
        
        HBox buttonsBox = new HBox(15);
        buttonsBox.setAlignment(Pos.CENTER);
        
        Button attackBtn = createStyledButton("⚔️ Attaquer", "#1A5F2E", "#2D7F4F");
        attackBtn.setPrefWidth(180);
        attackBtn.setPrefHeight(60);
        attackBtn.setOnAction(e -> handleAttack());
        
        Button itemBtn = createStyledButton("💊 Objet", "#00B4FF", "#00FFFF");
        itemBtn.setPrefWidth(180);
        itemBtn.setPrefHeight(60);
        itemBtn.setOnAction(e -> handleItem());
        
        Button switchBtn = createStyledButton("🔄 Changer", "#8B00FF", "#B026FF");
        switchBtn.setPrefWidth(180);
        switchBtn.setPrefHeight(60);
        switchBtn.setOnAction(e -> handleSwitch());
        
        Button menuBtn = createStyledButton("🏠 Menu", "#FF3838", "#FF6B35");
        menuBtn.setPrefWidth(180);
        menuBtn.setPrefHeight(60);
        menuBtn.setOnAction(e -> app.showMainMenu());
        
        // Stocker les références pour pouvoir les activer/désactiver
        actionButtons = new Button[]{attackBtn, itemBtn, switchBtn};
        
        buttonsBox.getChildren().addAll(attackBtn, itemBtn, switchBtn, menuBtn);
        area.getChildren().add(buttonsBox);
        
        return area;
    }
    
    private Button[] actionButtons; // Références aux boutons d'action pour les activer/désactiver
    
    // Stockage des références aux barres de vie pour animation
    private Map<Monster, ProgressBar> monsterHpBars = new HashMap<>();
    private Map<Monster, Label> monsterHpLabels = new HashMap<>();
    
    // Stockage des références aux cartes de monstres pour animations
    private Map<Monster, VBox> monsterCards = new HashMap<>();
    
    private Button createStyledButton(String text, String color1, String color2) {
        Button button = new Button(text);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        String rgb1 = hexToRgb(color1);
        // Créer une couleur plus sombre pour le hover (70% de la couleur de base)
        String darkColor = darkenColor(color1, 0.7);
        String rgbDark = hexToRgb(darkColor);
        
        button.setStyle(
            "-fx-background-color: " + color1 + ";" +
            "-fx-text-fill: #FFFFFF;" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: " + color1 + ";" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 10;" +
            "-fx-effect: dropshadow(gaussian, " + rgb1 + ", 15, 0, 0, 0), " +
            "dropshadow(gaussian, rgba(0,0,0,0.5), 5, 0, 0, 2);" +
            "-fx-cursor: hand;"
        );
        
        button.setOnMouseEntered(e -> {
            // Utiliser la couleur assombrie au hover
            button.setStyle(
                "-fx-background-color: " + darkColor + ";" +
                "-fx-text-fill: #FFFFFF;" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: " + darkColor + ";" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 10;" +
                "-fx-effect: dropshadow(gaussian, " + rgbDark + ", 25, 0, 0, 0), " +
                "dropshadow(gaussian, rgba(0,0,0,0.7), 8, 0, 0, 3);" +
                "-fx-cursor: hand;"
            );
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle(
                "-fx-background-color: " + color1 + ";" +
                "-fx-text-fill: #FFFFFF;" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: " + color1 + ";" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 10;" +
                "-fx-effect: dropshadow(gaussian, " + rgb1 + ", 15, 0, 0, 0), " +
                "dropshadow(gaussian, rgba(0,0,0,0.5), 5, 0, 0, 2);" +
                "-fx-cursor: hand;"
            );
        });
        
        return button;
    }
    
    private String hexToRgb(String hex) {
        hex = hex.replace("#", "");
        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);
        return "rgba(" + r + "," + g + "," + b + ",0.6)";
    }
    
    /**
     * Assombrit une couleur hexadécimale
     * @param hex La couleur en hexadécimal (avec ou sans #)
     * @param factor Le facteur d'assombrissement (0.0 à 1.0, plus petit = plus sombre)
     * @return La couleur assombrie en hexadécimal
     */
    private String darkenColor(String hex, double factor) {
        // Enlever le #
        hex = hex.replace("#", "");
        
        // Convertir hex en RGB
        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);
        
        // Assombrir en multipliant par le facteur
        r = (int) (r * factor);
        g = (int) (g * factor);
        b = (int) (b * factor);
        
        // S'assurer que les valeurs restent dans la plage 0-255
        r = Math.max(0, Math.min(255, r));
        g = Math.max(0, Math.min(255, g));
        b = Math.max(0, Math.min(255, b));
        
        // Retourner en format hexadécimal
        return String.format("#%02X%02X%02X", r, g, b);
    }
    
    /**
     * Crée un bouton stylisé pour les popups d'action
     */
    private Button createActionButton(String text, String color1, String color2) {
        Button button = new Button(text);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        String rgb1 = hexToRgb(color1);
        // Créer une couleur plus sombre pour le hover (70% de la couleur de base)
        String darkColor = darkenColor(color1, 0.7);
        String rgbDark = hexToRgb(darkColor);
        
        button.setStyle(
            "-fx-background-color: " + color1 + ";" +
            "-fx-text-fill: #FFFFFF;" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: " + color1 + ";" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 10;" +
            "-fx-effect: dropshadow(gaussian, " + rgb1 + ", 15, 0, 0, 0), " +
            "dropshadow(gaussian, rgba(0,0,0,0.5), 5, 0, 0, 2);" +
            "-fx-cursor: hand;" +
            "-fx-wrap-text: true;"
        );
        
        button.setOnMouseEntered(e -> {
            // Utiliser la couleur assombrie au hover
            button.setStyle(
                "-fx-background-color: " + darkColor + ";" +
                "-fx-text-fill: #FFFFFF;" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: " + darkColor + ";" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 10;" +
                "-fx-effect: dropshadow(gaussian, " + rgbDark + ", 25, 0, 0, 0), " +
                "dropshadow(gaussian, rgba(0,0,0,0.7), 8, 0, 0, 3);" +
                "-fx-cursor: hand;" +
                "-fx-wrap-text: true;"
            );
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle(
                "-fx-background-color: " + color1 + ";" +
                "-fx-text-fill: #FFFFFF;" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: " + color1 + ";" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 10;" +
                "-fx-effect: dropshadow(gaussian, " + rgb1 + ", 15, 0, 0, 0), " +
                "dropshadow(gaussian, rgba(0,0,0,0.5), 5, 0, 0, 2);" +
                "-fx-cursor: hand;" +
                "-fx-wrap-text: true;"
            );
        });
        
        return button;
    }
    
    private void handleAttack() {
        if (battleController == null || currentPlayer == null) {
            showAlert("Erreur", "Le combat n'est pas initialisé.");
            return;
        }
        
        if (!waitingForPlayer1Action && currentPlayer == player1) {
            showAlert("Information", "Ce n'est pas votre tour.");
            return;
        }
        if (!waitingForPlayer2Action && currentPlayer == player2 && !soloMode) {
            showAlert("Information", "Ce n'est pas votre tour.");
            return;
        }
        
        Monster activeMonster = currentPlayer.getActiveMonster();
        if (activeMonster == null || !activeMonster.isAlive()) {
            showAlert("Erreur", "Aucun monstre actif disponible.");
            return;
        }
        
        showAttackSelectionDialog(activeMonster);
    }
    
    private void handleItem() {
        if (battleController == null || currentPlayer == null) {
            showAlert("Erreur", "Le combat n'est pas initialisé.");
            return;
        }
        
        if (!waitingForPlayer1Action && currentPlayer == player1) {
            showAlert("Information", "Ce n'est pas votre tour.");
            return;
        }
        if (!waitingForPlayer2Action && currentPlayer == player2 && !soloMode) {
            showAlert("Information", "Ce n'est pas votre tour.");
            return;
        }
        
        if (currentPlayer.getItems().isEmpty()) {
            showAlert("Information", "Vous n'avez plus d'objets disponibles.");
            return;
        }
        
        showItemSelectionDialog();
    }
    
    private void handleSwitch() {
        if (battleController == null || currentPlayer == null) {
            showAlert("Erreur", "Le combat n'est pas initialisé.");
            return;
        }
        
        if (!waitingForPlayer1Action && currentPlayer == player1) {
            showAlert("Information", "Ce n'est pas votre tour.");
            return;
        }
        if (!waitingForPlayer2Action && currentPlayer == player2 && !soloMode) {
            showAlert("Information", "Ce n'est pas votre tour.");
            return;
        }
        
        List<Monster> available = currentPlayer.getAvailableMonsters();
        if (available.size() <= 1) {
            showAlert("Information", "Vous n'avez qu'un seul monstre disponible.");
            return;
        }
        
        showMonsterSelectionDialog(available);
    }
    
    private void showAttackSelectionDialog(Monster monster) {
        Dialog<Integer> dialog = createStyledDialog("⚔️ Sélection d'attaque", "Choisissez une attaque pour " + monster.getName());
        
        List<Attack> specialAttacks = new ArrayList<>();
        AttackType monsterAttackType = convertMonsterTypeToAttackType(monster.getType());
        
        for (Attack attack : monster.getAttacks()) {
            if (attack.getType() == monsterAttackType && attack.canUse()) {
                specialAttacks.add(attack);
            }
        }
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label("Attaques disponibles :");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        titleLabel.setTextFill(Color.web("#1A5F2E"));
        content.getChildren().add(titleLabel);
        
        // Grille pour les boutons d'attaques spéciales
        GridPane attacksGrid = new GridPane();
        attacksGrid.setHgap(15);
        attacksGrid.setVgap(15);
        attacksGrid.setAlignment(Pos.CENTER);
        attacksGrid.setPadding(new Insets(10));
        
        int col = 0;
        int row = 0;
        for (int i = 0; i < specialAttacks.size(); i++) {
            Attack attack = specialAttacks.get(i);
            int attackIndex = monster.getAttacks().indexOf(attack);
            
            Button attackBtn = createActionButton(
                attack.getName() + "\n⚡ Puissance: " + attack.getPower() + 
                "\n📊 " + attack.getNbUse() + "/" + attack.getMaxUses() + " utilisations",
                "#1A5F2E",
                "#2D7F4F"
            );
            attackBtn.setPrefWidth(200);
            attackBtn.setPrefHeight(100);
            attackBtn.setOnAction(e -> {
                dialog.setResult(attackIndex);
                dialog.close();
            });
            
            attacksGrid.add(attackBtn, col, row);
            col++;
            if (col >= 2) {
                col = 0;
                row++;
            }
        }
        
        if (!specialAttacks.isEmpty()) {
            content.getChildren().add(attacksGrid);
        }
        
        // Bouton attaque normale
        Button normalAttackBtn = createActionButton(
            "👊 Attaque Normale\n(mains nues)",
            "#FF6B35",
            "#FF8C42"
        );
        normalAttackBtn.setPrefWidth(250);
        normalAttackBtn.setPrefHeight(80);
        normalAttackBtn.setOnAction(e -> {
            dialog.setResult(-1);
            dialog.close();
        });
        
        // Bouton annuler
        Button cancelBtn = createActionButton("❌ Annuler", "#8A8A8A", "#A0A0A0");
        cancelBtn.setPrefWidth(150);
        cancelBtn.setPrefHeight(50);
        cancelBtn.setOnAction(e -> {
            dialog.setResult(null);
            dialog.close();
        });
        
        HBox bottomButtons = new HBox(15);
        bottomButtons.setAlignment(Pos.CENTER);
        bottomButtons.getChildren().addAll(normalAttackBtn, cancelBtn);
        
        content.getChildren().addAll(bottomButtons);
        
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().clear(); // Retirer les boutons par défaut
        
        dialog.showAndWait().ifPresent(attackIndex -> {
            if (attackIndex != null) {
                executeAttack(attackIndex);
            }
        });
    }
    
    private void showItemSelectionDialog() {
        Dialog<ItemSelectionResult> dialog = new Dialog<>();
        dialog.setTitle("💊 Sélection d'objet");
        
        Monster activeMonster = currentPlayer.getActiveMonster();
        if (activeMonster == null) {
            showAlert("Erreur", "Aucun monstre actif disponible.");
            return;
        }
        
        dialog.setHeaderText("Choisissez un objet à utiliser sur " + activeMonster.getName() + " (monstre actif)");
        
        // Style dark/neon pour le dialogue
        dialog.getDialogPane().setStyle(
            "-fx-background-color: #1A1F1E;" +
            "-fx-border-color: #00B4FF;" +
            "-fx-border-width: 2;"
        );
        
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.CENTER);
        
        // Afficher le monstre actif
        Label monsterInfoLabel = new Label("⚡ Monstre actif : " + activeMonster.getName() + 
            " | ❤️ PV: " + activeMonster.getHp() + "/" + activeMonster.getMaxHp());
        monsterInfoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        monsterInfoLabel.setTextFill(Color.web("#00FFFF"));
        
        // Section : Sélection de l'objet
        Label itemTitleLabel = new Label("Choisissez un objet :");
        itemTitleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        itemTitleLabel.setTextFill(Color.web("#00B4FF"));
        
        GridPane itemsGrid = new GridPane();
        itemsGrid.setHgap(15);
        itemsGrid.setVgap(15);
        itemsGrid.setAlignment(Pos.CENTER);
        itemsGrid.setPadding(new Insets(10));
        
        int col = 0;
        int row = 0;
        for (int i = 0; i < currentPlayer.getItems().size(); i++) {
            Item item = currentPlayer.getItems().get(i);
            final int itemIndex = i;
            
            Button itemBtn = createActionButton(
                item.getName() + "\n" + item.getDescription(),
                "#00B4FF",
                "#00FFFF"
            );
            itemBtn.setPrefWidth(200);
            itemBtn.setPrefHeight(100);
            
            // Appliquer directement l'objet au monstre actif
            itemBtn.setOnAction(e -> {
                dialog.setResult(new ItemSelectionResult(
                    itemIndex,
                    activeMonster
                ));
                dialog.close();
            });
            
            itemsGrid.add(itemBtn, col, row);
            col++;
            if (col >= 2) {
                col = 0;
                row++;
            }
        }
        
        // Bouton annuler
        Button cancelBtn = createActionButton("❌ Annuler", "#8A8A8A", "#A0A0A0");
        cancelBtn.setPrefWidth(150);
        cancelBtn.setPrefHeight(50);
        cancelBtn.setOnAction(e -> {
            dialog.setResult(null);
            dialog.close();
        });
        
        content.getChildren().addAll(
            monsterInfoLabel,
            itemTitleLabel,
            itemsGrid,
            cancelBtn
        );
        
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().clear(); // Retirer les boutons par défaut
        
        dialog.showAndWait().ifPresent(result -> {
            if (result != null) {
                executeItem(result.itemIndex, result.targetMonster);
            }
        });
    }
    
    /**
     * Classe pour stocker le résultat de la sélection d'objet
     */
    private static class ItemSelectionResult {
        int itemIndex;
        Monster targetMonster;
        
        ItemSelectionResult(int itemIndex, Monster targetMonster) {
            this.itemIndex = itemIndex;
            this.targetMonster = targetMonster;
        }
    }
    
    private void showMonsterSelectionDialog(List<Monster> available) {
        Dialog<Integer> dialog = createStyledDialog("🔄 Changement de monstre", "Choisissez un monstre à envoyer au combat");
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label("Monstres disponibles :");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        titleLabel.setTextFill(Color.web("#8B00FF"));
        content.getChildren().add(titleLabel);
        
        GridPane monstersGrid = new GridPane();
        monstersGrid.setHgap(15);
        monstersGrid.setVgap(15);
        monstersGrid.setAlignment(Pos.CENTER);
        monstersGrid.setPadding(new Insets(10));
        
        int col = 0;
        int row = 0;
        for (Monster monster : available) {
            if (monster != currentPlayer.getActiveMonster() && monster.isAlive()) {
                int monsterIndex = currentPlayer.getMonsters().indexOf(monster);
                
                String typeDisplay = gameController.getMonsterTypeDisplay(monster);
                double hpPercent = monster.getMaxHp() > 0 ? (double) monster.getHp() / monster.getMaxHp() : 0;
                String hpStatus = hpPercent > 0.5 ? "❤️" : hpPercent > 0.25 ? "💛" : "💔";
                
                Button monsterBtn = createActionButton(
                    monster.getName() + "\n" + typeDisplay + 
                    "\n" + hpStatus + " PV: " + monster.getHp() + "/" + monster.getMaxHp() +
                    "\n⚔️ ATK:" + monster.getAttack() + " 🛡️ DEF:" + monster.getDefense() + " ⚡ SPD:" + monster.getSpeed(),
                    "#8B00FF",
                    "#B026FF"
                );
                monsterBtn.setPrefWidth(220);
                monsterBtn.setPrefHeight(120);
                monsterBtn.setOnAction(e -> {
                    dialog.setResult(monsterIndex);
                    dialog.close();
                });
                
                monstersGrid.add(monsterBtn, col, row);
                col++;
                if (col >= 2) {
                    col = 0;
                    row++;
                }
            }
        }
        
        content.getChildren().add(monstersGrid);
        
        // Bouton annuler
        Button cancelBtn = createActionButton("❌ Annuler", "#8A8A8A", "#A0A0A0");
        cancelBtn.setPrefWidth(150);
        cancelBtn.setPrefHeight(50);
        cancelBtn.setOnAction(e -> {
            dialog.setResult(null);
            dialog.close();
        });
        
        content.getChildren().add(cancelBtn);
        
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().clear(); // Retirer les boutons par défaut
        
        dialog.showAndWait().ifPresent(monsterIndex -> {
            if (monsterIndex != null) {
                executeSwitch(monsterIndex);
            }
        });
    }
    
    private Dialog<Integer> createStyledDialog(String title, String header) {
        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        
        // Style dark/neon pour le dialogue
        dialog.getDialogPane().setStyle(
            "-fx-background-color: #1A1F1E;" +
            "-fx-border-color: #39FF14;" +
            "-fx-border-width: 2;"
        );
        
        return dialog;
    }
    
    private void executeAttack(int attackIndex) {
        if (battleController == null || currentPlayer == null) {
            return;
        }
        
        TurnManager.Action action = new TurnManager.Action(
            TurnManager.Action.ActionType.ATTACK, currentPlayer
        );
        action.attackIndex = attackIndex;
        
        battleController.getTurnManager().addAction(action);
        
        nextPlayerOrProcessTurn();
    }
    
    private void executeItem(int itemIndex, Monster targetMonster) {
        if (battleController == null || currentPlayer == null) {
            return;
        }
        
        TurnManager.Action action = new TurnManager.Action(
            TurnManager.Action.ActionType.USE_ITEM, currentPlayer
        );
        action.targetIndex = itemIndex;
        action.targetMonster = targetMonster; // Définir le monstre cible
        
        battleController.getTurnManager().addAction(action);
        
        nextPlayerOrProcessTurn();
    }
    
    private void executeSwitch(int monsterIndex) {
        if (battleController == null || currentPlayer == null) {
            return;
        }
        
        TurnManager.Action action = new TurnManager.Action(
            TurnManager.Action.ActionType.SWITCH_MONSTER, currentPlayer
        );
        action.targetIndex = monsterIndex;
        
        battleController.getTurnManager().addAction(action);
        
        nextPlayerOrProcessTurn();
    }
    
    private void nextPlayerOrProcessTurn() {
        if (waitingForPlayer1Action && currentPlayer == player1) {
            waitingForPlayer1Action = false;
            if (soloMode) {
                // En mode solo, le bot joue automatiquement
                waitingForPlayer2Action = false;
                statusLabel.setText("Exécution du tour...");
                updateDisplay();
                
                // Le bot joue
                processBotTurn();
            } else {
                waitingForPlayer2Action = true;
                currentPlayer = player2;
                currentPlayerLabel.setText("Tour de " + player2.getName());
                statusLabel.setText("En attente de l'action de " + player2.getName());
                updateDisplay();
                updateActionButtons();
            }
        } else if (waitingForPlayer2Action && currentPlayer == player2 && !soloMode) {
            // Les deux joueurs ont fait leur action, exécuter le tour
            waitingForPlayer2Action = false;
            statusLabel.setText("Exécution du tour...");
            updateDisplay();
            
            executeTurn();
        }
    }
    
    private void executeTurn() {
        Platform.runLater(() -> {
            // Stocker les PV avant le tour pour l'animation
            Map<Monster, Integer> hpBefore = new HashMap<>();
            for (Monster m : player1.getMonsters()) {
                hpBefore.put(m, m.getHp());
            }
            for (Monster m : player2.getMonsters()) {
                hpBefore.put(m, m.getHp());
            }
            
            // Récupérer les actions d'attaque avant l'exécution
            TurnManager turnManager = battleController.getTurnManager();
            List<TurnManager.Action> allActions = turnManager.getActions();
            List<TurnManager.Action> attackActions = new ArrayList<>();
            
            for (TurnManager.Action action : allActions) {
                if (action.type == TurnManager.Action.ActionType.ATTACK) {
                    attackActions.add(action);
                }
            }
            
            // Trier par vitesse (comme dans TurnManager)
            attackActions.sort((a1, a2) -> {
                Monster m1 = a1.player.getActiveMonster();
                Monster m2 = a2.player.getActiveMonster();
                if (m1 == null || m2 == null) return 0;
                return Integer.compare(m2.getSpeed(), m1.getSpeed());
            });
            
            // Animer les attaques une par une, puis exécuter le tour
            animateAttacksSequence(attackActions, hpBefore);
            
            // Vérifier le gagnant
            Player winner = battleController.checkWinner();
            if (winner != null) {
                // Afficher l'interface de victoire améliorée
                showVictoryScreen(winner);
                return;
            }
            
            // Nouveau tour
            waitingForPlayer1Action = true;
            currentPlayer = player1;
            currentPlayerLabel.setText("Tour de " + player1.getName());
            statusLabel.setText("En attente de l'action de " + player1.getName());
            statusLabel.setTextFill(Color.web("#E0E0E0")); // Remettre la couleur normale
            updateDisplay();
            updateActionButtons();
            setActionButtonsEnabled(true); // Réactiver les boutons
        });
    }
    
    private void displayGameStateInLog() {
        if (battleController == null) return;
        
        BattleField field = battleController.getField();
        Player p1 = field.getPlayer1();
        Player p2 = field.getPlayer2();
        
        // Terrain
        if (field.isFlooded()) {
            addLogMessage("💧 TERRAIN: INONDÉ (durée: " + field.getFloodDuration() + " tours)");
        } else {
            addLogMessage("🌍 TERRAIN: NORMAL");
        }
        
        // Joueur 1
        addLogMessage("\n👤 " + p1.getName().toUpperCase());
        for (Monster m : p1.getMonsters()) {
            boolean isActive = m == p1.getActiveMonster();
            String status = isActive ? "⚡ ACTIF" : "   ";
            String hpBar = createHpBar(m);
            String statusCond = getStatusText(m);
            if (m.isAlive()) {
                addLogMessage(String.format("%s %s | %s [%d/%d] %s | ATK:%d DEF:%d SPD:%d",
                    status, m.getName(), hpBar, m.getHp(), m.getMaxHp(), statusCond,
                    m.getAttack(), m.getDefense(), m.getSpeed()));
            } else {
                addLogMessage(status + " " + m.getName() + " | ❌ KO");
            }
        }
        
        // Joueur 2
        addLogMessage("\n👤 " + p2.getName().toUpperCase());
        for (Monster m : p2.getMonsters()) {
            boolean isActive = m == p2.getActiveMonster();
            String status = isActive ? "⚡ ACTIF" : "   ";
            String hpBar = createHpBar(m);
            String statusCond = getStatusText(m);
            if (m.isAlive()) {
                addLogMessage(String.format("%s %s | %s [%d/%d] %s | ATK:%d DEF:%d SPD:%d",
                    status, m.getName(), hpBar, m.getHp(), m.getMaxHp(), statusCond,
                    m.getAttack(), m.getDefense(), m.getSpeed()));
            } else {
                addLogMessage(status + " " + m.getName() + " | ❌ KO");
            }
        }
    }
    
    private String createHpBar(Monster monster) {
        int barLength = 20;
        int hpPercent = 0;
        if (monster.getMaxHp() > 0) {
            hpPercent = (int) ((double) monster.getHp() / monster.getMaxHp() * barLength);
        }
        return "█".repeat(Math.max(0, hpPercent)) + 
               "░".repeat(Math.max(0, barLength - hpPercent));
    }
    
    private void processBotTurn() {
        if (!soloMode || gameController.getBot() == null) {
            return;
        }
        
        Platform.runLater(() -> {
            com.esiea.monstredepoche.controllers.Bot bot = gameController.getBot();
            
            // Désactiver les boutons pendant que le bot réfléchit
            setActionButtonsEnabled(false);
            statusLabel.setText("🤖 " + bot.getName() + " réfléchit...");
            updateDisplay();
            
            // Afficher que le bot réfléchit
            addLogMessage("\n🤖 === Tour de " + bot.getName() + " ===");
            
            // Créer un délai court pour montrer que le bot réfléchit
            PauseTransition pause = new PauseTransition(Duration.millis(800));
            pause.setOnFinished(e -> {
                Platform.runLater(() -> {
                    TurnManager.Action action = bot.makeDecision();
                    
                    if (action != null) {
                        // Afficher l'action du bot de manière visible
                        String actionDescription = getBotActionDescription(action, bot);
                        addLogMessage("🤖 " + bot.getName() + " : " + actionDescription);
                        statusLabel.setText("🤖 " + bot.getName() + " : " + actionDescription);
                        statusLabel.setTextFill(Color.web("#FF6B35")); // Orange pour le bot
                        updateDisplay();
                        
                        battleController.getTurnManager().addAction(action);
                        
                        // Attendre un peu pour que l'utilisateur voie l'action du bot
                        PauseTransition showAction = new PauseTransition(Duration.millis(1200));
                        showAction.setOnFinished(e2 -> {
                            Platform.runLater(() -> {
                                executeTurn();
                            });
                        });
                        showAction.play();
                    } else {
                        statusLabel.setText("🤖 " + bot.getName() + " ne peut pas agir.");
                        updateDisplay();
                        
                        // Attendre un peu avant d'exécuter le tour
                        PauseTransition showAction = new PauseTransition(Duration.millis(800));
                        showAction.setOnFinished(e2 -> {
                            Platform.runLater(() -> {
                                executeTurn();
                            });
                        });
                        showAction.play();
                    }
                });
            });
            pause.play();
        });
    }
    
    /**
     * Retourne une description textuelle de l'action du bot
     */
    private String getBotActionDescription(TurnManager.Action action, com.esiea.monstredepoche.controllers.Bot bot) {
        if (action == null || bot == null || bot.getPlayer() == null) {
            return "Aucune action";
        }
        
        Player botPlayer = bot.getPlayer();
        Monster activeMonster = botPlayer.getActiveMonster();
        
        switch (action.type) {
            case ATTACK:
                if (action.attackIndex == -1) {
                    return "Attaque normale avec " + (activeMonster != null ? activeMonster.getName() : "son monstre");
                } else {
                    Attack attack = activeMonster != null && action.attackIndex < activeMonster.getAttacks().size() 
                        ? activeMonster.getAttacks().get(action.attackIndex) 
                        : null;
                    if (attack != null) {
                        return "Utilise " + attack.getName() + " avec " + activeMonster.getName();
                    }
                    return "Attaque avec " + (activeMonster != null ? activeMonster.getName() : "son monstre");
                }
            case USE_ITEM:
                if (action.targetIndex >= 0 && action.targetIndex < botPlayer.getItems().size()) {
                    Item item = botPlayer.getItems().get(action.targetIndex);
                    Monster target = action.targetMonster != null ? action.targetMonster : activeMonster;
                    return "Utilise " + item.getName() + " sur " + (target != null ? target.getName() : "son monstre");
                }
                return "Utilise un objet";
            case SWITCH_MONSTER:
                if (action.targetIndex >= 0 && action.targetIndex < botPlayer.getMonsters().size()) {
                    Monster selected = botPlayer.getMonsters().get(action.targetIndex);
                    return "Change de monstre : " + selected.getName();
                }
                return "Change de monstre";
            default:
                return "Action inconnue";
        }
    }
    
    /**
     * Active ou désactive les boutons d'action
     */
    private void setActionButtonsEnabled(boolean enabled) {
        if (actionButtons == null) return;
        for (Button btn : actionButtons) {
            btn.setDisable(!enabled);
        }
    }
    
    private void updateDisplay() {
        if (battleController == null) {
            return;
        }
        
        BattleField field = battleController.getField();
        
        // Mettre à jour le terrain
        if (field.isFlooded()) {
            terrainLabel.setText("💧 TERRAIN INONDÉ (durée: " + field.getFloodDuration() + " tours)");
            terrainLabel.setTextFill(Color.web("#00FFFF")); // Cyan Néon
        } else {
            terrainLabel.setText("🌍 TERRAIN NORMAL");
            terrainLabel.setTextFill(Color.web("#39FF14")); // Vert Néon
        }
        
        // Mettre à jour l'affichage des joueurs avec leurs vrais noms
        updatePlayerDisplay(player1Area, player1, player1 != null ? player1.getName() : "Joueur 1");
        updatePlayerDisplay(player2Area, player2, player2 != null ? player2.getName() : (soloMode ? "Bot" : "Joueur 2"));
        
        // Activer/désactiver les boutons selon le tour
        updateActionButtons();
    }
    
    private void updateActionButtons() {
        if (actionButtons == null) return;
        
        boolean canAct = false;
        if (waitingForPlayer1Action && currentPlayer == player1) {
            canAct = true;
        } else if (waitingForPlayer2Action && currentPlayer == player2 && !soloMode) {
            canAct = true;
        }
        
        for (Button btn : actionButtons) {
            btn.setDisable(!canAct);
            // Style pour bouton désactivé
            if (!canAct) {
                btn.setStyle(
                    "-fx-background-color: #1C2B21;" +
                    "-fx-text-fill: #8A8A8A;" +
                    "-fx-background-radius: 10;" +
                    "-fx-border-color: #1C2B21;" +
                    "-fx-border-width: 2;" +
                    "-fx-border-radius: 10;" +
                    "-fx-opacity: 0.5;"
                );
            }
        }
    }
    
    private void updatePlayerDisplay(VBox area, Player player, String playerName) {
        area.getChildren().clear();
        
        Label nameLabel = new Label(playerName);
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        nameLabel.setTextFill(Color.web("#39FF14")); // Vert Néon
        area.getChildren().add(nameLabel);
        
        if (player == null || player.getMonsters().isEmpty()) {
            Label noMonsterLabel = new Label("Aucun monstre");
            noMonsterLabel.setTextFill(Color.web("#8A8A8A"));
            area.getChildren().add(noMonsterLabel);
            return;
        }
        
        // Nettoyer les références des monstres qui ne sont plus dans l'équipe
        List<Monster> currentMonsters = new ArrayList<>(player.getMonsters());
        monsterHpBars.entrySet().removeIf(entry -> !currentMonsters.contains(entry.getKey()));
        monsterHpLabels.entrySet().removeIf(entry -> !currentMonsters.contains(entry.getKey()));
        monsterCards.entrySet().removeIf(entry -> !currentMonsters.contains(entry.getKey()));
        
        for (int i = 0; i < player.getMonsters().size(); i++) {
            Monster monster = player.getMonsters().get(i);
            boolean isActive = monster == player.getActiveMonster();
            VBox monsterCard = createMonsterCard(monster, isActive, i + 1);
            area.getChildren().add(monsterCard);
        }
    }
    
    private VBox createMonsterCard(Monster monster, boolean isActive, int index) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setStyle(
            "-fx-background-color: " + (isActive ? "#0D1F12" : "#1A1F1E") + ";" +
            "-fx-border-color: " + (isActive ? "#39FF14" : "#1C2B21") + ";" +
            "-fx-border-width: " + (isActive ? "3" : "2") + ";" +
            "-fx-border-radius: 10;"
        );
        
        // En-tête avec index et nom
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label indexLabel = new Label("[" + index + "]");
        indexLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        indexLabel.setTextFill(Color.web("#8A8A8A"));
        
        Label nameLabel = new Label(monster.getName() + (isActive ? " ⚡ ACTIF" : ""));
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        nameLabel.setTextFill(Color.web(isActive ? "#39FF14" : "#E0E0E0"));
        
        header.getChildren().addAll(indexLabel, nameLabel);
        
        if (!monster.isAlive()) {
            Label koLabel = new Label("❌ KO");
            koLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            koLabel.setTextFill(Color.web("#FF3838")); // Rouge Danger
            card.getChildren().addAll(header, koLabel);
            return card;
        }
        
        // Conteneur pour la barre de vie avec label
        VBox hpContainer = new VBox(5);
        hpContainer.setAlignment(Pos.CENTER_LEFT);
        
        // Label PV au-dessus de la barre
        Label hpLabel = new Label("PV: " + monster.getHp() + "/" + monster.getMaxHp());
        hpLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        hpLabel.setTextFill(Color.web("#50C878")); // Vert Émeraude
        
        // Stocker la référence pour la mise à jour
        monsterHpLabels.put(monster, hpLabel);
        
        // Barre de vie améliorée
        double hpPercent = monster.getMaxHp() > 0 ? (double) monster.getHp() / monster.getMaxHp() : 0;
        ProgressBar hpBar = new ProgressBar(hpPercent);
        hpBar.setPrefWidth(360);
        hpBar.setPrefHeight(25);
        hpBar.setMinHeight(25);
        hpBar.setMaxHeight(25);
        
        // Stocker la référence pour l'animation (mise à jour à chaque recréation)
        monsterHpBars.put(monster, hpBar);
        
        updateHpBarStyle(hpBar, hpPercent);
        
        hpContainer.getChildren().addAll(hpLabel, hpBar);
        
        // Statistiques
        Label typeLabel = new Label("Type: " + gameController.getMonsterTypeDisplay(monster));
        typeLabel.setFont(Font.font("Arial", 11));
        typeLabel.setTextFill(Color.web("#E0E0E0"));
        
        Label statsLabel = new Label("ATK: " + monster.getAttack() + " | DEF: " + monster.getDefense() + " | SPD: " + monster.getSpeed());
        statsLabel.setFont(Font.font("Arial", 11));
        statsLabel.setTextFill(Color.web("#E0E0E0"));
        
        // État d'altération
        String statusText = getStatusText(monster);
        if (!statusText.isEmpty()) {
            Label statusLabel = new Label(statusText);
            statusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            statusLabel.setTextFill(Color.web("#FF3838")); // Rouge Danger
            card.getChildren().add(statusLabel);
        }
        
        card.getChildren().addAll(header, hpContainer, typeLabel, statsLabel);
        
        // Stocker la référence à la carte pour les animations
        monsterCards.put(monster, card);
        
        return card;
    }
    
    private String getStatusText(Monster monster) {
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
    
    /**
     * Met à jour le style de la barre de vie selon le pourcentage
     * La partie remplie (PV restants) est toujours verte
     */
    private void updateHpBarStyle(ProgressBar hpBar, double hpPercent) {
        // Toujours vert pour les PV restants
        String hpColor = "#50C878"; // Vert pour les PV restants
        hpBar.setStyle(
            "-fx-accent: " + hpColor + ";" +
            "-fx-background-color: #1C2B21;" + // Gris foncé pour la partie vide (PV perdus)
            "-fx-border-color: " + hpColor + ";"
        );
    }
    
    /**
     * Anime la progression des barres de vie après un changement de PV
     */
    private void animateHpBars(Map<Monster, Integer> hpBefore) {
        // Animer toutes les barres de vie qui ont changé
        for (Monster monster : monsterHpBars.keySet()) {
            ProgressBar hpBar = monsterHpBars.get(monster);
            Label hpLabel = monsterHpLabels.get(monster);
            
            if (hpBar == null || hpLabel == null) continue;
            
            Integer oldHp = hpBefore.get(monster);
            int newHp = monster.getHp();
            int maxHp = monster.getMaxHp();
            
            if (oldHp == null || oldHp == newHp) {
                // Pas de changement, juste mettre à jour
                double hpPercent = maxHp > 0 ? (double) newHp / maxHp : 0;
                hpBar.setProgress(Math.max(0, Math.min(1, hpPercent)));
                updateHpBarStyle(hpBar, hpPercent);
                hpLabel.setText("PV: " + newHp + "/" + maxHp);
                continue;
            }
            
            // Animer la transition
            final int startHp = oldHp;
            final int endHp = newHp;
            final int steps = Math.max(15, Math.abs(endHp - startHp)); // Au moins 15 étapes pour une animation fluide
            final int totalDuration = 500; // 500ms pour l'animation complète
            final int stepDuration = totalDuration / steps;
            
            Timeline timeline = new Timeline();
            
            for (int i = 0; i <= steps; i++) {
                final int step = i;
                double progress = (double) step / steps;
                // Utiliser une fonction d'easing pour une animation plus naturelle
                double easedProgress = 1 - Math.pow(1 - progress, 3); // Ease-out cubic
                int currentHp = (int) (startHp + (endHp - startHp) * easedProgress);
                
                KeyFrame keyFrame = new KeyFrame(
                    Duration.millis(step * stepDuration),
                    e -> {
                        double hpPercent = maxHp > 0 ? (double) currentHp / maxHp : 0;
                        hpBar.setProgress(Math.max(0, Math.min(1, hpPercent)));
                        updateHpBarStyle(hpBar, hpPercent);
                        hpLabel.setText("PV: " + currentHp + "/" + maxHp);
                    }
                );
                timeline.getKeyFrames().add(keyFrame);
            }
            
            timeline.play();
        }
    }
    
    private AttackType convertMonsterTypeToAttackType(com.esiea.monstredepoche.models.enums.MonsterType monsterType) {
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
    
    private void addLogMessage(String message) {
        // Zone de log supprimée - les messages ne sont plus affichés
    }
    
    /**
     * Anime les attaques une par une avec des effets visuels, puis exécute le tour
     */
    private void animateAttacksSequence(List<TurnManager.Action> attackActions, Map<Monster, Integer> hpBefore) {
        if (attackActions.isEmpty()) {
            // Pas d'attaques, exécuter directement le tour
            executeTurnAfterAnimation(hpBefore);
            return;
        }
        
        // Animer chaque attaque séquentiellement
        animateAttackAtIndex(attackActions, 0, hpBefore);
    }
    
    /**
     * Anime une attaque à l'index donné, puis passe à la suivante
     */
    private void animateAttackAtIndex(List<TurnManager.Action> attackActions, int index, Map<Monster, Integer> hpBefore) {
        if (index >= attackActions.size()) {
            // Toutes les attaques sont animées, exécuter le tour
            executeTurnAfterAnimation(hpBefore);
            return;
        }
        
        TurnManager.Action action = attackActions.get(index);
        Monster attacker = action.player.getActiveMonster();
        Player targetPlayer = (action.player == player1) ? player2 : player1;
        Monster target = targetPlayer.getActiveMonster();
        
        if (attacker == null || target == null || !attacker.isAlive() || !target.isAlive()) {
            // Passer à l'attaque suivante
            animateAttackAtIndex(attackActions, index + 1, hpBefore);
            return;
        }
        
        // Récupérer l'attaque utilisée
        Attack attack = null;
        if (action.attackIndex >= 0 && action.attackIndex < attacker.getAttacks().size()) {
            attack = attacker.getAttacks().get(action.attackIndex);
        }
        
        // Créer l'animation d'attaque
        animateSingleAttack(attacker, target, attack, () -> {
            // Après l'animation, passer à l'attaque suivante
            animateAttackAtIndex(attackActions, index + 1, hpBefore);
        });
    }
    
    /**
     * Anime une attaque unique avec effets visuels
     */
    private void animateSingleAttack(Monster attacker, Monster target, Attack attack, Runnable onComplete) {
        VBox attackerCard = monsterCards.get(attacker);
        VBox targetCard = monsterCards.get(target);
        
        if (attackerCard == null || targetCard == null) {
            // Les cartes ne sont pas disponibles, passer directement
            onComplete.run();
            return;
        }
        
        // Déterminer la couleur de l'effet selon le type d'attaque
        String effectColor = getAttackEffectColor(attack, attacker);
        
        // Animation 1: Flash sur l'attaquant (préparation)
        FadeTransition attackerFlash = new FadeTransition(Duration.millis(200), attackerCard);
        attackerFlash.setFromValue(1.0);
        attackerFlash.setToValue(0.7);
        attackerFlash.setAutoReverse(true);
        attackerFlash.setCycleCount(2);
        
        // Animation 2: Tremblement de l'attaquant
        TranslateTransition attackerShake = new TranslateTransition(Duration.millis(100), attackerCard);
        attackerShake.setFromX(0);
        attackerShake.setByX(10);
        attackerShake.setCycleCount(4);
        attackerShake.setAutoReverse(true);
        
        // Animation 3: Flash de couleur sur la cible (impact)
        ParallelTransition targetImpact = createImpactAnimation(targetCard, effectColor);
        
        // Animation 4: Tremblement de la cible (reçoit les dégâts)
        TranslateTransition targetShake = new TranslateTransition(Duration.millis(150), targetCard);
        targetShake.setFromX(0);
        targetShake.setByX(15);
        targetShake.setCycleCount(6);
        targetShake.setAutoReverse(true);
        
        // Séquence complète
        SequentialTransition attackSequence = new SequentialTransition(
            attackerFlash,
            attackerShake,
            new PauseTransition(Duration.millis(100)), // Pause entre préparation et impact
            targetImpact,
            targetShake,
            new PauseTransition(Duration.millis(200)) // Pause après l'impact
        );
        
        attackSequence.setOnFinished(e -> onComplete.run());
        attackSequence.play();
    }
    
    /**
     * Crée une animation d'impact avec flash de couleur
     */
    private ParallelTransition createImpactAnimation(VBox targetCard, String effectColor) {
        // Flash de couleur (fade)
        FadeTransition flash = new FadeTransition(Duration.millis(300), targetCard);
        flash.setFromValue(1.0);
        flash.setToValue(0.4);
        flash.setAutoReverse(true);
        flash.setCycleCount(2);
        
        // Changement de couleur de bordure temporaire avec effet glow
        String originalStyle = targetCard.getStyle();
        Timeline colorFlash = new Timeline(
            new KeyFrame(Duration.ZERO, e -> {
                // Ajouter un effet de glow coloré
                String glowColor = effectColor.replace("#", "");
                int r = Integer.parseInt(glowColor.substring(0, 2), 16);
                int g = Integer.parseInt(glowColor.substring(2, 4), 16);
                int b = Integer.parseInt(glowColor.substring(4, 6), 16);
                String rgbGlow = "rgba(" + r + "," + g + "," + b + ",0.8)";
                
                targetCard.setStyle(
                    originalStyle + 
                    " -fx-effect: dropshadow(gaussian, " + rgbGlow + ", 30, 0, 0, 0), " +
                    "dropshadow(gaussian, rgba(0,0,0,0.5), 5, 0, 0, 2);"
                );
            }),
            new KeyFrame(Duration.millis(300), e -> {
                // Retour au style original
                targetCard.setStyle(originalStyle);
            })
        );
        
        ParallelTransition impact = new ParallelTransition(flash, colorFlash);
        return impact;
    }
    
    /**
     * Détermine la couleur d'effet selon le type d'attaque
     */
    private String getAttackEffectColor(Attack attack, Monster attacker) {
        if (attack == null) {
            // Attaque normale (mains nues)
            return "#FF6B35"; // Orange
        }
        
        switch (attack.getType()) {
            case ELECTRIC:
                return "#FFD700"; // Or/jaune pour électricité
            case WATER:
                return "#00B4FF"; // Bleu pour eau
            case GROUND:
                return "#8B4513"; // Marron pour terre
            case FIRE:
                return "#FF3838"; // Rouge pour feu
            case NATURE:
                return "#50C878"; // Vert pour nature
            default:
                return "#FFFFFF"; // Blanc pour normal
        }
    }
    
    /**
     * Exécute le tour après toutes les animations
     */
    private void executeTurnAfterAnimation(Map<Monster, Integer> hpBefore) {
        Platform.runLater(() -> {
            battleController.processTurn();
            
            // Mettre à jour l'affichage pour avoir les nouvelles références des barres
            updateDisplay();
            
            // Attendre un court instant pour que l'affichage soit mis à jour
            PauseTransition updatePause = new PauseTransition(Duration.millis(50));
            updatePause.setOnFinished(e -> {
                // Animer la progression des barres de vie
                animateHpBars(hpBefore);
                
                // Vérifier le gagnant après un court délai
                PauseTransition checkWinnerPause = new PauseTransition(Duration.millis(600));
                checkWinnerPause.setOnFinished(e2 -> {
                    Platform.runLater(() -> {
                        Player winner = battleController.checkWinner();
                        if (winner != null) {
                            // Afficher l'interface de victoire améliorée
                            showVictoryScreen(winner);
                            return;
                        }
                        
                        // Nouveau tour
                        waitingForPlayer1Action = true;
                        currentPlayer = player1;
                        currentPlayerLabel.setText("Tour de " + player1.getName());
                        statusLabel.setText("En attente de l'action de " + player1.getName());
                        statusLabel.setTextFill(Color.web("#E0E0E0")); // Remettre la couleur normale
                        updateDisplay();
                        updateActionButtons();
                        setActionButtonsEnabled(true); // Réactiver les boutons
                    });
                });
                checkWinnerPause.play();
            });
            updatePause.play();
        });
    }
    
    /**
     * Affiche une interface de victoire améliorée avec un design moderne
     */
    private void showVictoryScreen(Player winner) {
        Dialog<String> victoryDialog = new Dialog<>();
        victoryDialog.setTitle("🎉 VICTOIRE !");
        victoryDialog.setResizable(false);
        
        // Style dark/neon pour le dialogue
        victoryDialog.getDialogPane().setStyle(
            "-fx-background-color: #0A0E0D;" +
            "-fx-border-color: #39FF14;" +
            "-fx-border-width: 3;" +
            "-fx-border-radius: 15;"
        );
        
        VBox content = new VBox(25);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40, 50, 40, 50));
        content.setStyle("-fx-background-color: #0A0E0D;");
        
        // Titre de victoire
        Label victoryTitle = new Label("🎉 VICTOIRE ! 🎉");
        victoryTitle.setFont(Font.font("Arial", FontWeight.BOLD, 42));
        victoryTitle.setTextFill(Color.web("#39FF14"));
        victoryTitle.setStyle(
            "-fx-effect: dropshadow(gaussian, rgba(57,255,20,0.8), 20, 0, 0, 0);"
        );
        
        // Nom du gagnant
        Label winnerLabel = new Label(winner.getName().toUpperCase());
        winnerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        winnerLabel.setTextFill(Color.web("#00FFFF"));
        winnerLabel.setStyle(
            "-fx-effect: dropshadow(gaussian, rgba(0,255,255,0.6), 15, 0, 0, 0);"
        );
        
        // Message de victoire
        Label victoryMessage = new Label("remporte la victoire !");
        victoryMessage.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
        victoryMessage.setTextFill(Color.web("#E0E0E0"));
        
        // Statistiques du gagnant
        VBox statsBox = new VBox(10);
        statsBox.setAlignment(Pos.CENTER);
        statsBox.setPadding(new Insets(20, 0, 20, 0));
        
        int aliveMonsters = 0;
        for (Monster m : winner.getMonsters()) {
            if (m.isAlive()) {
                aliveMonsters++;
            }
        }
        
        Label statsLabel = new Label("Monstres restants : " + aliveMonsters + "/" + winner.getMonsters().size());
        statsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        statsLabel.setTextFill(Color.web("#50C878"));
        
        statsBox.getChildren().add(statsLabel);
        
        // Bouton retour au menu
        Button menuButton = createActionButton("🏠 Retour au Menu", "#39FF14", "#00FF41");
        menuButton.setPrefWidth(250);
        menuButton.setPrefHeight(60);
        menuButton.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        menuButton.setOnAction(e -> {
            victoryDialog.setResult("MENU");
            victoryDialog.close();
        });
        
        // Bouton jouer une nouvelle partie
        Button newGameButton = createActionButton("🎮 Jouer une nouvelle partie", "#00B4FF", "#00D4FF");
        newGameButton.setPrefWidth(300);
        newGameButton.setPrefHeight(60);
        newGameButton.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        newGameButton.setOnAction(e -> {
            victoryDialog.setResult("NEW_GAME");
            victoryDialog.close();
        });
        
        HBox buttonsBox = new HBox(15);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.getChildren().addAll(menuButton, newGameButton);
        
        content.getChildren().addAll(victoryTitle, winnerLabel, victoryMessage, statsBox, buttonsBox);
        
        victoryDialog.getDialogPane().setContent(content);
        victoryDialog.getDialogPane().getButtonTypes().clear(); // Retirer les boutons par défaut
        
        // Afficher le dialogue et traiter le résultat
        victoryDialog.showAndWait().ifPresent(result -> {
            if ("MENU".equals(result)) {
                Platform.runLater(() -> {
                    app.showMainMenu();
                });
            } else if ("NEW_GAME".equals(result)) {
                Platform.runLater(() -> {
                    restartGame();
                });
            }
        });
    }
    
    /**
     * Réinitialise le jeu et retourne à la sélection d'équipe
     */
    private void restartGame() {
        // Retourner à la sélection d'équipe avec le même mode (solo ou 1v1)
        app.showTeamSelection(soloMode);
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        // Style dark/neon
        alert.getDialogPane().setStyle(
            "-fx-background-color: #1A1F1E;" +
            "-fx-border-color: #39FF14;" +
            "-fx-border-width: 2;"
        );
        
        alert.showAndWait();
    }
    
    public void setPlayers(Player p1, Player p2, boolean soloMode) {
        this.soloMode = soloMode;
        this.player1 = p1;
        this.player2 = p2;
        
        // S'assurer que chaque joueur a un monstre actif
        if (player1.getActiveMonster() == null && !player1.getMonsters().isEmpty()) {
            for (Monster m : player1.getMonsters()) {
                if (m.isAlive()) {
                    player1.setActiveMonster(m);
                    break;
                }
            }
        }
        
        if (player2.getActiveMonster() == null && !player2.getMonsters().isEmpty()) {
            for (Monster m : player2.getMonsters()) {
                if (m.isAlive()) {
                    player2.setActiveMonster(m);
                    break;
                }
            }
        }
        
        this.battleController = new BattleController(player1, player2);
        this.battleController.initializeBattle();
        
        // Si mode solo, créer et configurer le bot
        if (soloMode) {
            com.esiea.monstredepoche.controllers.Bot bot = new com.esiea.monstredepoche.controllers.Bot("Bot");
            bot.setPlayer(player2);
            gameController.setBot(bot);
        }
        
        this.currentPlayer = player1;
        this.waitingForPlayer1Action = true;
        this.waitingForPlayer2Action = false;
        
        // Mettre à jour les labels seulement s'ils existent déjà (scène créée)
        if (currentPlayerLabel != null) {
            currentPlayerLabel.setText("Tour de " + player1.getName());
        }
        if (statusLabel != null) {
            statusLabel.setText("En attente de l'action de " + player1.getName());
        }
        
        // Les messages de début de combat sont supprimés pour simplifier l'interface
        
        // Mettre à jour l'affichage seulement si la scène est créée
        if (root != null) {
            updateDisplay();
        }
    }
}
