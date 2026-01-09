package com.esiea.monstredepoche.gui;

import com.esiea.monstredepoche.controllers.GameController;
import com.esiea.monstredepoche.models.Monster;
import com.esiea.monstredepoche.models.Player;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Contrôleur pour la sélection d'équipe.
 * Utilise le GameController existant sans le modifier.
 */
public class TeamSelectionController {
    private MonsterGameApp app;
    private GameController gameController;
    private boolean soloMode; // Mode solo (vs bot) ou 1v1
    private List<Monster> availableMonsters;
    private List<Monster> selectedMonsters;
    private int currentPlayer = 1;
    private Player player1;
    private Player player2;
    private String player1Name;
    private String player2Name;
    
    private VBox root;
    private Label titleLabel;
    private HBox monstersContainer;
    private Label selectedLabel;
    private HBox selectedContainer;
    private Button confirmButton;
    private Button backButton;
    
    public TeamSelectionController(MonsterGameApp app, GameController gameController, boolean soloMode) {
        this.app = app;
        this.gameController = gameController;
        this.soloMode = soloMode;
        this.selectedMonsters = new ArrayList<>();
    }
    
    public TeamSelectionController(MonsterGameApp app, GameController gameController) {
        this(app, gameController, false); // Par défaut, mode 1v1
    }
    
    public Scene createScene() {
        root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        // Background principal - Noir Profond
        root.setBackground(new Background(new BackgroundFill(Color.web("#0A0E0D"), CornerRadii.EMPTY, Insets.EMPTY)));
        
        try {
            loadMonsters();
            // Demander le nom du joueur avant de continuer (seulement pour le joueur 1 au début)
            if (currentPlayer == 1) {
                askPlayerName(1);
            }
            setupUI();
        } catch (IOException e) {
            System.err.println("Erreur IOException lors du chargement des monstres : " + e.getMessage());
            e.printStackTrace();
            showError("Erreur lors du chargement des monstres : " + e.getMessage());
            // Initialiser une liste vide pour éviter NullPointerException
            if (availableMonsters == null) {
                availableMonsters = new ArrayList<>();
            }
            setupUI(); // Appeler quand même pour afficher l'interface avec le message d'erreur
        } catch (Exception e) {
            System.err.println("Erreur inattendue lors du chargement des monstres : " + e.getMessage());
            e.printStackTrace();
            showError("Erreur inattendue : " + e.getMessage());
            // Initialiser une liste vide pour éviter NullPointerException
            if (availableMonsters == null) {
                availableMonsters = new ArrayList<>();
            }
            setupUI(); // Appeler quand même pour afficher l'interface
        }
        
        return new Scene(root, 1200, 800);
    }
    
    private void loadMonsters() throws IOException {
        // Utiliser les monstres chargés par le GameController (même source de données)
        availableMonsters = gameController.getAvailableMonsters();
        System.out.println("Chargement des monstres pour le GUI depuis GameController...");
        if (availableMonsters == null) {
            System.err.println("ERREUR: availableMonsters est null !");
            availableMonsters = new ArrayList<>();
        } else {
            System.out.println("Monstres disponibles : " + availableMonsters.size());
            if (availableMonsters.isEmpty()) {
                System.err.println("ATTENTION: La liste des monstres est vide !");
                System.err.println("Vérifiez que le fichier resources/monsters.txt existe et est valide.");
            }
        }
    }
    
    private void setupUI() {
        // Titre - Vert Néon avec effet glow
        if (soloMode) {
            titleLabel = new Label("SÉLECTION D'ÉQUIPE - VOTRE ÉQUIPE");
        } else {
            titleLabel = new Label("SÉLECTION D'ÉQUIPE - JOUEUR " + currentPlayer);
        }
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.web("#39FF14")); // Vert Néon
        titleLabel.setStyle(
            "-fx-effect: dropshadow(gaussian, rgba(57, 255, 20, 0.8), 20, 0, 0, 0), " +
            "dropshadow(gaussian, rgba(57, 255, 20, 0.5), 40, 0, 0, 0);"
        );
        
        // Container pour les monstres disponibles
        monstersContainer = new HBox(15);
        monstersContainer.setAlignment(Pos.CENTER);
        monstersContainer.setPadding(new Insets(20));
        
        // Label "Monstres disponibles"
        Label availableLabel = new Label("Monstres disponibles :");
        availableLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        availableLabel.setTextFill(Color.web("#A8FF9E")); // Vert Clair Texte
        
        // Container pour les monstres sélectionnés
        selectedLabel = new Label("Équipe actuelle (" + selectedMonsters.size() + "/3)");
        selectedLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        selectedLabel.setTextFill(Color.web("#A8FF9E")); // Vert Clair Texte
        
        selectedContainer = new HBox(15);
        selectedContainer.setAlignment(Pos.CENTER);
        selectedContainer.setPadding(new Insets(20));
        selectedContainer.setMinHeight(200);
        selectedContainer.setStyle(
            "-fx-background-color: #1A1F1E;" + // Gris Anthracite
            "-fx-background-radius: 15;" +
            "-fx-border-color: #39FF14;" + // Vert Néon
            "-fx-border-width: 2;" +
            "-fx-border-radius: 15;"
        );
        
        // Boutons
        HBox buttonContainer = new HBox(20);
        buttonContainer.setAlignment(Pos.CENTER);
        
        confirmButton = createStyledButton("Confirmer l'équipe", "#39FF14", "#00FF41");
        confirmButton.setPrefWidth(200);
        confirmButton.setPrefHeight(50);
        confirmButton.setDisable(true);
        confirmButton.setOnAction(e -> confirmTeam());
        
        backButton = createStyledButton("Retour", "#FF3838", "#FF6B35");
        backButton.setPrefWidth(200);
        backButton.setPrefHeight(50);
        backButton.setOnAction(e -> app.showMainMenu());
        
        buttonContainer.getChildren().addAll(backButton, confirmButton);
        
        root.getChildren().addAll(
            titleLabel,
            availableLabel,
            monstersContainer,
            selectedLabel,
            selectedContainer,
            buttonContainer
        );
        
        // Afficher les monstres disponibles (chargés depuis GameController)
        updateMonsterDisplay();
    }
    
    private void confirmTeam() {
        if (selectedMonsters.size() == 3) {
            if (currentPlayer == 1) {
                // Créer le joueur 1 avec le nom saisi
                String name = player1Name != null && !player1Name.trim().isEmpty() ? player1Name.trim() : "Joueur 1";
                player1 = new Player(name);
                for (Monster monster : selectedMonsters) {
                    player1.addMonster(monster);
                }
                
                // Donner des objets au joueur (comme dans la console)
                gameController.giveItemsToPlayer(player1);
                
                // Afficher dans la console (même comportement que la console)
                System.out.println("\n" + "=".repeat(50));
                System.out.println("✅ Équipe complète pour " + name + " !");
                System.out.println("=".repeat(50) + "\n");
                
                if (soloMode) {
                    // En mode solo, créer le bot automatiquement
                    createBotPlayer();
                    app.startBattle(player1, player2, true);
                } else {
                    // Passer au joueur 2 - demander son nom
                    currentPlayer = 2;
                    selectedMonsters.clear();
                    askPlayerName(2);
                    titleLabel.setText("SÉLECTION D'ÉQUIPE - " + (player2Name != null ? player2Name.toUpperCase() : "JOUEUR 2"));
                    updateUI();
                }
            } else {
                // Créer le joueur 2 avec le nom saisi
                String name = player2Name != null && !player2Name.trim().isEmpty() ? player2Name.trim() : "Joueur 2";
                player2 = new Player(name);
                for (Monster monster : selectedMonsters) {
                    player2.addMonster(monster);
                }
                // Donner des objets au joueur (comme dans la console)
                gameController.giveItemsToPlayer(player2);
                
                // Afficher dans la console (même comportement que la console)
                System.out.println("\n" + "=".repeat(50));
                System.out.println("✅ Équipe complète pour " + name + " !");
                System.out.println("=".repeat(50) + "\n");
                
                // Lancer le combat avec les joueurs créés
                app.startBattle(player1, player2, false);
            }
        }
    }
    
    /**
     * Demande le nom du joueur via un dialogue
     */
    private void askPlayerName(int playerNumber) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nom du joueur");
        
        String defaultName = playerNumber == 1 ? "Joueur 1" : "Joueur 2";
        if (soloMode && playerNumber == 1) {
            dialog.setHeaderText("Entrez votre nom");
            dialog.setContentText("Nom :");
        } else {
            dialog.setHeaderText("Entrez le nom du Joueur " + playerNumber);
            dialog.setContentText("Nom :");
        }
        
        dialog.getEditor().setText(defaultName);
        
        // Style dark/neon
        dialog.getDialogPane().setStyle(
            "-fx-background-color: #1A1F1E;" +
            "-fx-border-color: #39FF14;" +
            "-fx-border-width: 2;"
        );
        
        dialog.showAndWait().ifPresent(name -> {
            if (playerNumber == 1) {
                player1Name = name.trim().isEmpty() ? defaultName : name.trim();
            } else {
                player2Name = name.trim().isEmpty() ? defaultName : name.trim();
            }
        });
        
        // Si l'utilisateur a annulé, utiliser le nom par défaut
        if (playerNumber == 1 && player1Name == null) {
            player1Name = defaultName;
        } else if (playerNumber == 2 && player2Name == null) {
            player2Name = defaultName;
        }
    }
    
    private void updateUI() {
        // Mettre à jour l'affichage
        updateMonsterDisplay();
        confirmButton.setDisable(selectedMonsters.size() != 3);
    }
    
    private void updateMonsterDisplay() {
        // Afficher les monstres disponibles
        monstersContainer.getChildren().clear();
        
        if (availableMonsters == null || availableMonsters.isEmpty()) {
            // Afficher un message si aucun monstre n'est disponible
            Label errorLabel = new Label("Aucun monstre disponible.\nVérifiez que le fichier monsters.txt est bien chargé.");
            errorLabel.setFont(Font.font("Arial", 16));
            errorLabel.setTextFill(Color.web("#FF3838")); // Rouge Danger
            errorLabel.setAlignment(Pos.CENTER);
            monstersContainer.getChildren().add(errorLabel);
            System.err.println("ERREUR: Aucun monstre disponible dans TeamSelectionController !");
            System.err.println("Nombre de monstres dans GameController: " + 
                (gameController.getAvailableMonsters() != null ? gameController.getAvailableMonsters().size() : "null"));
        } else {
            for (int i = 0; i < availableMonsters.size(); i++) {
                Monster monster = availableMonsters.get(i);
                VBox monsterCard = createMonsterCard(monster, i);
                monstersContainer.getChildren().add(monsterCard);
            }
        }
        
        // Afficher les monstres sélectionnés
        selectedContainer.getChildren().clear();
        for (int i = 0; i < selectedMonsters.size(); i++) {
            Monster monster = selectedMonsters.get(i);
            VBox selectedCard = createSelectedMonsterCard(monster, i);
            selectedContainer.getChildren().add(selectedCard);
        }
        
        // Mettre à jour le label "Équipe actuelle"
        if (selectedLabel != null) {
            selectedLabel.setText("Équipe actuelle (" + selectedMonsters.size() + "/3)");
        }
    }
    
    private VBox createMonsterCard(Monster monster, int index) {
        VBox card = new VBox(5);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(10));
        card.setStyle(
            "-fx-background-color: #1A1F1E;" + // Gris Anthracite
            "-fx-border-color: #39FF14;" + // Vert Néon
            "-fx-border-width: 2;" +
            "-fx-border-radius: 5;"
        );
        card.setPrefSize(150, 180);
        
        Label nameLabel = new Label(monster.getName());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        nameLabel.setTextFill(Color.web("#39FF14")); // Vert Néon
        
        // Utiliser getMonsterTypeDisplay du GameController (même méthode que la console)
        Label typeLabel = new Label("Type: " + gameController.getMonsterTypeDisplay(monster));
        typeLabel.setTextFill(Color.web("#E0E0E0")); // Gris Clair
        Label hpLabel = new Label("PV: " + monster.getMaxHp());
        hpLabel.setTextFill(Color.web("#50C878")); // Vert Émeraude (HP)
        Label atkLabel = new Label("ATK: " + monster.getAttack());
        atkLabel.setTextFill(Color.web("#E0E0E0")); // Gris Clair
        Label defLabel = new Label("DEF: " + monster.getDefense());
        defLabel.setTextFill(Color.web("#E0E0E0")); // Gris Clair
        Label spdLabel = new Label("SPD: " + monster.getSpeed());
        spdLabel.setTextFill(Color.web("#E0E0E0")); // Gris Clair
        
        Button selectButton = new Button("Sélectionner");
        selectButton.setStyle(
            "-fx-background-color: #39FF14;" + // Vert Néon
            "-fx-text-fill: #0A0E0D;" + // Noir Profond pour le texte
            "-fx-background-radius: 5;" +
            "-fx-border-color: #39FF14;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 5;" +
            "-fx-cursor: hand;"
        );
        selectButton.setOnMouseEntered(e -> {
            if (!selectButton.isDisabled()) {
                selectButton.setStyle(
                    "-fx-background-color: #00FF41;" + // Vert Acide
                    "-fx-text-fill: #0A0E0D;" +
                    "-fx-background-radius: 5;" +
                    "-fx-border-color: #00FF41;" +
                    "-fx-border-width: 1;" +
                    "-fx-border-radius: 5;" +
                    "-fx-cursor: hand;"
                );
            }
        });
        selectButton.setOnMouseExited(e -> {
            if (!selectButton.isDisabled()) {
                selectButton.setStyle(
                    "-fx-background-color: #39FF14;" +
                    "-fx-text-fill: #0A0E0D;" +
                    "-fx-background-radius: 5;" +
                    "-fx-border-color: #39FF14;" +
                    "-fx-border-width: 1;" +
                    "-fx-border-radius: 5;" +
                    "-fx-cursor: hand;"
                );
            }
        });
        selectButton.setOnAction(e -> selectMonster(monster));
        selectButton.setDisable(selectedMonsters.contains(monster) || selectedMonsters.size() >= 3);
        
        // Style pour le bouton désactivé
        selectButton.disabledProperty().addListener((obs, wasDisabled, isNowDisabled) -> {
            if (isNowDisabled) {
                selectButton.setStyle(
                    "-fx-background-color: #1C2B21;" + // Vert Charbon
                    "-fx-text-fill: #8A8A8A;" + // Gris Moyen
                    "-fx-background-radius: 5;" +
                    "-fx-border-color: #1C2B21;" +
                    "-fx-border-width: 1;" +
                    "-fx-border-radius: 5;"
                );
            }
        });
        
        card.getChildren().addAll(nameLabel, typeLabel, hpLabel, atkLabel, defLabel, spdLabel, selectButton);
        return card;
    }
    
    private VBox createSelectedMonsterCard(Monster monster, int index) {
        VBox card = new VBox(5);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(10));
        card.setStyle(
            "-fx-background-color: #0D1F12;" + // Vert Sombre
            "-fx-border-color: #00FF41;" + // Vert Acide
            "-fx-border-width: 2;" +
            "-fx-border-radius: 5;"
        );
        card.setPrefSize(120, 150);
        
        Label nameLabel = new Label(monster.getName());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        nameLabel.setTextFill(Color.web("#39FF14")); // Vert Néon
        
        // Utiliser getMonsterTypeDisplay du GameController (même méthode que la console)
        Label typeLabel = new Label("Type: " + gameController.getMonsterTypeDisplay(monster));
        typeLabel.setTextFill(Color.web("#E0E0E0")); // Gris Clair
        
        Button removeButton = new Button("Retirer");
        removeButton.setStyle(
            "-fx-background-color: #FF3838;" + // Rouge Danger
            "-fx-text-fill: #FFFFFF;" + // Blanc Pur
            "-fx-background-radius: 5;" +
            "-fx-border-color: #FF3838;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 5;" +
            "-fx-cursor: hand;"
        );
        removeButton.setOnMouseEntered(e -> {
            removeButton.setStyle(
                "-fx-background-color: #FF6B35;" + // Orange Toxique
                "-fx-text-fill: #FFFFFF;" +
                "-fx-background-radius: 5;" +
                "-fx-border-color: #FF6B35;" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 5;" +
                "-fx-cursor: hand;"
            );
        });
        removeButton.setOnMouseExited(e -> {
            removeButton.setStyle(
                "-fx-background-color: #FF3838;" +
                "-fx-text-fill: #FFFFFF;" +
                "-fx-background-radius: 5;" +
                "-fx-border-color: #FF3838;" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 5;" +
                "-fx-cursor: hand;"
            );
        });
        removeButton.setOnAction(e -> removeSelectedMonster(index));
        
        card.getChildren().addAll(nameLabel, typeLabel, removeButton);
        return card;
    }
    
    private void selectMonster(Monster monster) {
        if (selectedMonsters.size() < 3 && !selectedMonsters.contains(monster)) {
            // Utiliser createMonsterCopy du GameController (même méthode que la console)
            Monster copy = gameController.createMonsterCopy(monster);
            selectedMonsters.add(copy);
            
            // Afficher dans la console (même comportement que la console)
            System.out.println("✅ " + copy.getName() + " ajouté à l'équipe !");
            
            updateMonsterDisplay();
            confirmButton.setDisable(selectedMonsters.size() < 3);
        }
    }
    
    private void removeSelectedMonster(int index) {
        if (index >= 0 && index < selectedMonsters.size()) {
            Monster removed = selectedMonsters.remove(index);
            
            // Afficher dans la console (même comportement que la console)
            System.out.println("✅ " + removed.getName() + " retiré de l'équipe.");
            
            updateMonsterDisplay();
            confirmButton.setDisable(selectedMonsters.size() < 3);
        }
    }
    
    private Button createStyledButton(String text, String color1, String color2) {
        Button button = new Button(text);
        
        // Convertir hex en RGB pour l'effet glow
        String rgb1 = hexToRgb(color1);
        String rgb2 = hexToRgb(color2);
        
        button.setStyle(
            "-fx-background-color: " + color1 + ";" +
            "-fx-text-fill: #FFFFFF;" + // Blanc Pur
            "-fx-background-radius: 10;" +
            "-fx-border-color: " + color1 + ";" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 10;" +
            "-fx-effect: dropshadow(gaussian, " + rgb1 + ", 15, 0, 0, 0), " +
            "dropshadow(gaussian, rgba(0,0,0,0.5), 5, 0, 0, 2);" +
            "-fx-cursor: hand;"
        );
        
        button.setOnMouseEntered(e -> {
            if (!button.isDisabled()) {
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
            }
        });
        
        button.setOnMouseExited(e -> {
            if (!button.isDisabled()) {
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
            }
        });
        
        // Style pour le bouton désactivé
        button.disabledProperty().addListener((obs, wasDisabled, isNowDisabled) -> {
            if (isNowDisabled) {
                button.setStyle(
                    "-fx-background-color: #1C2B21;" + // Vert Charbon
                    "-fx-text-fill: #8A8A8A;" + // Gris Moyen
                    "-fx-background-radius: 10;" +
                    "-fx-border-color: #1C2B21;" +
                    "-fx-border-width: 2;" +
                    "-fx-border-radius: 10;"
                );
            }
        });
        
        return button;
    }
    
    /**
     * Convertit une couleur hexadécimale en format rgba pour les effets glow
     */
    private String hexToRgb(String hex) {
        // Enlever le #
        hex = hex.replace("#", "");
        
        // Convertir hex en RGB
        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);
        
        // Retourner en format rgba avec opacité pour l'effet glow
        return "rgba(" + r + "," + g + "," + b + ",0.6)";
    }
    
    private void createBotPlayer() {
        // Créer le bot et sélectionner aléatoirement son équipe
        System.out.println("\n=== Configuration du Bot ===");
        System.out.println("Le bot sélectionne son équipe aléatoirement...");
        
        player2 = new Player("Bot");
        List<Monster> available = new ArrayList<>(availableMonsters);
        
        // Sélectionner 3 monstres aléatoirement
        for (int i = 0; i < 3 && !available.isEmpty(); i++) {
            int randomIndex = com.esiea.monstredepoche.utils.RandomGenerator.randomInRange(0, available.size() - 1);
            Monster selectedMonster = available.remove(randomIndex);
            Monster copy = gameController.createMonsterCopy(selectedMonster);
            player2.addMonster(copy);
            System.out.println("🤖 Bot sélectionne : " + copy.getName());
        }
        
        // Donner des objets au bot
        gameController.giveItemsToPlayer(player2);
        
        // Créer et configurer le bot dans le GameController
        com.esiea.monstredepoche.controllers.Bot bot = new com.esiea.monstredepoche.controllers.Bot("Bot");
        bot.setPlayer(player2);
        gameController.setBot(bot);
        
        System.out.println("✅ Équipe du Bot complète !\n");
    }
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
