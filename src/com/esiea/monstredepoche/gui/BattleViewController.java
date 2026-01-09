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

import java.util.ArrayList;
import java.util.List;

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
    private ScrollPane logArea;
    private TextArea logTextArea;
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
        
        // Zone de log (messages de combat)
        logTextArea = new TextArea();
        logTextArea.setEditable(false);
        logTextArea.setWrapText(true);
        logTextArea.setFont(Font.font("Consolas", 12));
        logTextArea.setStyle(
            "-fx-background-color: #1A1F1E;" +
            "-fx-text-fill: #A8FF9E;" +
            "-fx-border-color: #39FF14;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 5;"
        );
        logTextArea.setPrefRowCount(8);
        
        logArea = new ScrollPane(logTextArea);
        logArea.setFitToWidth(true);
        logArea.setStyle("-fx-background: #1A1F1E;");
        
        // Zone d'actions (bas)
        actionArea = createActionArea();
        
        // Layout principal
        HBox playersBox = new HBox(20);
        playersBox.getChildren().addAll(player1Area, centerArea, player2Area);
        HBox.setHgrow(player1Area, Priority.ALWAYS);
        HBox.setHgrow(centerArea, Priority.ALWAYS);
        HBox.setHgrow(player2Area, Priority.ALWAYS);
        
        // Layout vertical : joueurs + log
        VBox mainContent = new VBox(15);
        mainContent.getChildren().addAll(playersBox, logArea);
        VBox.setVgrow(playersBox, Priority.ALWAYS);
        VBox.setVgrow(logArea, Priority.NEVER);
        
        root.setCenter(mainContent);
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
        
        Label actionLabel = new Label("Actions disponibles :");
        actionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        actionLabel.setTextFill(Color.web("#A8FF9E")); // Vert Clair Texte
        
        HBox buttonsBox = new HBox(15);
        buttonsBox.setAlignment(Pos.CENTER);
        
        Button attackBtn = createStyledButton("⚔️ Attaquer", "#39FF14", "#00FF41");
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
        area.getChildren().addAll(actionLabel, buttonsBox);
        
        return area;
    }
    
    private Button[] actionButtons; // Références aux boutons d'action pour les activer/désactiver
    
    private Button createStyledButton(String text, String color1, String color2) {
        Button button = new Button(text);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        String rgb1 = hexToRgb(color1);
        String rgb2 = hexToRgb(color2);
        
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
            button.setStyle(
                "-fx-background-color: " + color2 + ";" +
                "-fx-text-fill: #FFFFFF;" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: " + color2 + ";" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 10;" +
                "-fx-effect: dropshadow(gaussian, " + rgb2 + ", 25, 0, 0, 0), " +
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
        Dialog<Integer> dialog = createStyledDialog("Sélection d'attaque", "Choisissez une attaque pour " + monster.getName());
        
        List<Attack> specialAttacks = new ArrayList<>();
        AttackType monsterAttackType = convertMonsterTypeToAttackType(monster.getType());
        
        for (Attack attack : monster.getAttacks()) {
            if (attack.getType() == monsterAttackType && attack.canUse()) {
                specialAttacks.add(attack);
            }
        }
        
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        
        ListView<Attack> attackList = new ListView<>();
        attackList.setStyle(
            "-fx-background-color: #1A1F1E;" +
            "-fx-text-fill: #A8FF9E;" +
            "-fx-border-color: #39FF14;"
        );
        
        for (Attack attack : specialAttacks) {
            attackList.getItems().add(attack);
        }
        
        attackList.setCellFactory(param -> new ListCell<Attack>() {
            @Override
            protected void updateItem(Attack attack, boolean empty) {
                super.updateItem(attack, empty);
                if (empty || attack == null) {
                    setText(null);
                    setStyle("-fx-background-color: #1A1F1E;");
                } else {
                    setText(attack.getName() + " | Puissance: " + attack.getPower() + 
                           " | Utilisations: " + attack.getNbUse() + "/" + attack.getMaxUses());
                    setStyle(
                        "-fx-background-color: #1C2B21;" +
                        "-fx-text-fill: #A8FF9E;" +
                        "-fx-padding: 5;"
                    );
                }
            }
        });
        
        ButtonType normalAttackBtn = new ButtonType("Attaque normale (mains nues)", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelBtn = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        
        if (!specialAttacks.isEmpty()) {
            content.getChildren().addAll(
                new Label("Attaques spéciales disponibles :"),
                attackList
            );
            dialog.getDialogPane().getButtonTypes().addAll(normalAttackBtn, cancelBtn);
        } else {
            content.getChildren().add(new Label("Aucune attaque spéciale disponible.\nUtilisez l'attaque normale."));
            dialog.getDialogPane().getButtonTypes().addAll(normalAttackBtn, cancelBtn);
        }
        
        dialog.getDialogPane().setContent(content);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == normalAttackBtn) {
                return -1;
            } else if (dialogButton == cancelBtn) {
                return null;
            } else {
                Attack selected = attackList.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    return monster.getAttacks().indexOf(selected);
                }
            }
            return null;
        });
        
        dialog.showAndWait().ifPresent(attackIndex -> {
            if (attackIndex != null) {
                executeAttack(attackIndex);
            }
        });
    }
    
    private void showItemSelectionDialog() {
        Dialog<ItemSelectionResult> dialog = new Dialog<>();
        dialog.setTitle("Sélection d'objet");
        dialog.setHeaderText("Choisissez un objet à utiliser");
        
        // Style dark/neon pour le dialogue
        dialog.getDialogPane().setStyle(
            "-fx-background-color: #1A1F1E;" +
            "-fx-border-color: #39FF14;" +
            "-fx-border-width: 2;"
        );
        
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        
        ListView<Item> itemList = new ListView<>();
        itemList.setStyle(
            "-fx-background-color: #1A1F1E;" +
            "-fx-text-fill: #A8FF9E;" +
            "-fx-border-color: #39FF14;"
        );
        
        for (Item item : currentPlayer.getItems()) {
            itemList.getItems().add(item);
        }
        
        itemList.setCellFactory(param -> new ListCell<Item>() {
            @Override
            protected void updateItem(Item item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("-fx-background-color: #1A1F1E;");
                } else {
                    setText(item.getName() + " - " + item.getDescription());
                    setStyle(
                        "-fx-background-color: #1C2B21;" +
                        "-fx-text-fill: #A8FF9E;" +
                        "-fx-padding: 5;"
                    );
                }
            }
        });
        
        // Sélection du monstre cible
        Label targetLabel = new Label("Choisissez le monstre cible :");
        targetLabel.setTextFill(Color.web("#A8FF9E"));
        
        ListView<Monster> monsterList = new ListView<>();
        monsterList.setStyle(
            "-fx-background-color: #1A1F1E;" +
            "-fx-text-fill: #A8FF9E;" +
            "-fx-border-color: #39FF14;"
        );
        
        for (Monster monster : currentPlayer.getMonsters()) {
            if (monster.isAlive()) {
                monsterList.getItems().add(monster);
            }
        }
        
        monsterList.setCellFactory(param -> new ListCell<Monster>() {
            @Override
            protected void updateItem(Monster monster, boolean empty) {
                super.updateItem(monster, empty);
                if (empty || monster == null) {
                    setText(null);
                    setStyle("-fx-background-color: #1A1F1E;");
                } else {
                    String active = monster == currentPlayer.getActiveMonster() ? " (ACTIF)" : "";
                    setText(monster.getName() + " | PV: " + monster.getHp() + "/" + monster.getMaxHp() + active);
                    setStyle(
                        "-fx-background-color: #1C2B21;" +
                        "-fx-text-fill: #A8FF9E;" +
                        "-fx-padding: 5;"
                    );
                }
            }
        });
        
        content.getChildren().addAll(
            new Label("Objet :"),
            itemList,
            targetLabel,
            monsterList
        );
        
        ButtonType useBtn = new ButtonType("Utiliser", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelBtn = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(useBtn, cancelBtn);
        dialog.getDialogPane().setContent(content);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == useBtn) {
                Item selectedItem = itemList.getSelectionModel().getSelectedItem();
                Monster selectedMonster = monsterList.getSelectionModel().getSelectedItem();
                if (selectedItem != null && selectedMonster != null) {
                    return new ItemSelectionResult(
                        currentPlayer.getItems().indexOf(selectedItem),
                        selectedMonster
                    );
                }
            }
            return null;
        });
        
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
        Dialog<Integer> dialog = createStyledDialog("Changement de monstre", "Choisissez un monstre à envoyer au combat");
        
        ListView<Monster> monsterList = new ListView<>();
        monsterList.setStyle(
            "-fx-background-color: #1A1F1E;" +
            "-fx-text-fill: #A8FF9E;" +
            "-fx-border-color: #39FF14;"
        );
        
        for (Monster monster : available) {
            if (monster != currentPlayer.getActiveMonster() && monster.isAlive()) {
                monsterList.getItems().add(monster);
            }
        }
        
        monsterList.setCellFactory(param -> new ListCell<Monster>() {
            @Override
            protected void updateItem(Monster monster, boolean empty) {
                super.updateItem(monster, empty);
                if (empty || monster == null) {
                    setText(null);
                    setStyle("-fx-background-color: #1A1F1E;");
                } else {
                    String status = monster == currentPlayer.getActiveMonster() ? " (ACTIF)" : "";
                    setText(monster.getName() + " | PV: " + monster.getHp() + "/" + monster.getMaxHp() + 
                           " | Type: " + gameController.getMonsterTypeDisplay(monster) + status);
                    setStyle(
                        "-fx-background-color: #1C2B21;" +
                        "-fx-text-fill: #A8FF9E;" +
                        "-fx-padding: 5;"
                    );
                }
            }
        });
        
        ButtonType switchBtn = new ButtonType("Changer", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelBtn = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(switchBtn, cancelBtn);
        dialog.getDialogPane().setContent(monsterList);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == switchBtn) {
                Monster selected = monsterList.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    return currentPlayer.getMonsters().indexOf(selected);
                }
            }
            return null;
        });
        
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
        addLogMessage("⚔️ " + currentPlayer.getName() + " choisit d'attaquer !");
        
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
        Item item = currentPlayer.getItems().get(itemIndex);
        addLogMessage("💊 " + currentPlayer.getName() + " utilise " + item.getName() + " sur " + targetMonster.getName() + " !");
        
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
        Monster selected = currentPlayer.getMonsters().get(monsterIndex);
        addLogMessage("🔄 " + currentPlayer.getName() + " change de monstre : " + selected.getName() + " !");
        
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
            addLogMessage("\n" + "=".repeat(50));
            addLogMessage("=== EXÉCUTION DU TOUR ===");
            addLogMessage("=".repeat(50));
            
            // Afficher l'état avant le tour
            displayGameStateInLog();
            
            battleController.processTurn();
            
            // Afficher l'état après le tour
            addLogMessage("\n--- État après le tour ---");
            displayGameStateInLog();
            
            // Vérifier le gagnant
            Player winner = battleController.checkWinner();
            if (winner != null) {
                addLogMessage("\n" + "=".repeat(50));
                addLogMessage("🎉 " + winner.getName().toUpperCase() + " REMPORTE LA VICTOIRE !");
                addLogMessage("=".repeat(50));
                showAlert("Victoire !", winner.getName() + " remporte la victoire !");
                app.showMainMenu();
                return;
            }
            
            // Nouveau tour
            waitingForPlayer1Action = true;
            currentPlayer = player1;
            currentPlayerLabel.setText("Tour de " + player1.getName());
            statusLabel.setText("En attente de l'action de " + player1.getName());
            updateDisplay();
            updateActionButtons();
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
            addLogMessage("🤖 Tour de " + bot.getName());
            
            TurnManager.Action action = bot.makeDecision();
            
            if (action != null) {
                battleController.getTurnManager().addAction(action);
                executeTurn();
            } else {
                addLogMessage("🤖 " + bot.getName() + " ne peut pas agir.");
                executeTurn();
            }
        });
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
        
        for (int i = 0; i < player.getMonsters().size(); i++) {
            Monster monster = player.getMonsters().get(i);
            boolean isActive = monster == player.getActiveMonster();
            VBox monsterCard = createMonsterCard(monster, isActive, i + 1);
            area.getChildren().add(monsterCard);
        }
    }
    
    private VBox createMonsterCard(Monster monster, boolean isActive, int index) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(12));
        card.setStyle(
            "-fx-background-color: " + (isActive ? "#0D1F12" : "#1A1F1E") + ";" +
            "-fx-border-color: " + (isActive ? "#39FF14" : "#1C2B21") + ";" +
            "-fx-border-width: " + (isActive ? "3" : "2") + ";" +
            "-fx-border-radius: 8;"
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
        
        // Barre de vie
        double hpPercent = monster.getMaxHp() > 0 ? (double) monster.getHp() / monster.getMaxHp() : 0;
        ProgressBar hpBar = new ProgressBar(hpPercent);
        hpBar.setPrefWidth(350);
        hpBar.setPrefHeight(20);
        
        String hpColor = hpPercent > 0.5 ? "#50C878" : hpPercent > 0.25 ? "#FFE135" : "#FF3838";
        hpBar.setStyle(
            "-fx-accent: " + hpColor + ";" +
            "-fx-background-color: #1C2B21;" +
            "-fx-border-color: " + hpColor + ";"
        );
        
        Label hpLabel = new Label("PV: " + monster.getHp() + "/" + monster.getMaxHp());
        hpLabel.setFont(Font.font("Arial", 12));
        hpLabel.setTextFill(Color.web("#50C878")); // Vert Émeraude
        
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
        
        card.getChildren().addAll(header, hpBar, hpLabel, typeLabel, statsLabel);
        
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
        Platform.runLater(() -> {
            logTextArea.appendText(message + "\n");
            logTextArea.setScrollTop(Double.MAX_VALUE);
        });
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
        
        // Ajouter les messages de log seulement si le TextArea existe
        if (logTextArea != null) {
            addLogMessage("=== Début du combat ===");
            addLogMessage(player1.getName() + " vs " + player2.getName());
            addLogMessage(player1.getActiveMonster().getName() + " vs " + player2.getActiveMonster().getName());
        }
        
        // Mettre à jour l'affichage seulement si la scène est créée
        if (root != null) {
            updateDisplay();
        }
    }
}
