package com.esiea.monstredepoche.gui;

import com.esiea.monstredepoche.controllers.GameController;
import com.esiea.monstredepoche.models.Monster;
import com.esiea.monstredepoche.models.Player;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    private List<Monster> availableMonsters;
    private List<Monster> selectedMonsters;
    private int currentPlayer = 1;
    private Player player1;
    private Player player2;
    
    private VBox root;
    private Label titleLabel;
    private HBox monstersContainer;
    private HBox selectedContainer;
    private Button confirmButton;
    private Button backButton;
    
    public TeamSelectionController(MonsterGameApp app, GameController gameController) {
        this.app = app;
        this.gameController = gameController;
        this.selectedMonsters = new ArrayList<>();
    }
    
    public Scene createScene() {
        root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setBackground(new Background(new BackgroundFill(Color.web("#FFFFFF"), CornerRadii.EMPTY, Insets.EMPTY)));
        
        try {
            loadMonsters();
            setupUI();
        } catch (IOException e) {
            showError("Erreur lors du chargement des monstres : " + e.getMessage());
        }
        
        return new Scene(root, 1200, 800);
    }
    
    private void loadMonsters() throws IOException {
        // Utiliser la méthode privée via réflexion ou créer une méthode publique
        // Pour l'instant, on va charger directement
        availableMonsters = new ArrayList<>();
        // Note: On devrait accéder aux monstres via le GameController
        // Pour simplifier, on va créer une méthode dans GameController pour exposer les monstres
    }
    
    private void setupUI() {
        // Titre
        titleLabel = new Label("SÉLECTION D'ÉQUIPE - JOUEUR " + currentPlayer);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.web("#3B4CCA"));
        
        // Container pour les monstres disponibles
        monstersContainer = new HBox(15);
        monstersContainer.setAlignment(Pos.CENTER);
        monstersContainer.setPadding(new Insets(20));
        
        // Container pour les monstres sélectionnés
        Label selectedLabel = new Label("Équipe actuelle (" + selectedMonsters.size() + "/3)");
        selectedLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        selectedLabel.setTextFill(Color.web("#2C2C2C"));
        
        selectedContainer = new HBox(15);
        selectedContainer.setAlignment(Pos.CENTER);
        selectedContainer.setPadding(new Insets(20));
        selectedContainer.setMinHeight(200);
        selectedContainer.setStyle(
            "-fx-background-color: #F5F5F5;" +
            "-fx-background-radius: 15;" +
            "-fx-border-color: #3B4CCA;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 15;"
        );
        
        // Boutons
        HBox buttonContainer = new HBox(20);
        buttonContainer.setAlignment(Pos.CENTER);
        
        confirmButton = createStyledButton("Confirmer l'équipe", "#78C850", "#7AC74C");
        confirmButton.setPrefWidth(200);
        confirmButton.setPrefHeight(50);
        confirmButton.setDisable(true);
        confirmButton.setOnAction(e -> confirmTeam());
        
        backButton = createStyledButton("Retour", "#C03028", "#C22E28");
        backButton.setPrefWidth(200);
        backButton.setPrefHeight(50);
        backButton.setOnAction(e -> app.showMainMenu());
        
        buttonContainer.getChildren().addAll(backButton, confirmButton);
        
        root.getChildren().addAll(
            titleLabel,
            new Label("Monstres disponibles :"),
            monstersContainer,
            selectedLabel,
            selectedContainer,
            buttonContainer
        );
        
        // Pour l'instant, on affiche un message
        // Dans une vraie implémentation, on chargerait les monstres depuis GameController
        Label infoLabel = new Label("Chargement des monstres...");
        infoLabel.setFont(Font.font("Arial", 16));
        monstersContainer.getChildren().add(infoLabel);
    }
    
    private void confirmTeam() {
        if (selectedMonsters.size() == 3) {
            if (currentPlayer == 1) {
                // Créer le joueur 1
                player1 = new Player("Joueur 1");
                for (Monster monster : selectedMonsters) {
                    player1.addMonster(monster);
                }
                // Passer au joueur 2
                currentPlayer = 2;
                selectedMonsters.clear();
                titleLabel.setText("SÉLECTION D'ÉQUIPE - JOUEUR 2");
                updateUI();
            } else {
                // Créer le joueur 2 et lancer le combat
                player2 = new Player("Joueur 2");
                for (Monster monster : selectedMonsters) {
                    player2.addMonster(monster);
                }
                // Lancer le combat
                app.showBattle();
            }
        }
    }
    
    private void updateUI() {
        // Mettre à jour l'affichage
        selectedContainer.getChildren().clear();
        confirmButton.setDisable(selectedMonsters.size() != 3);
    }
    
    private Button createStyledButton(String text, String color1, String color2) {
        Button button = new Button(text);
        button.setStyle(
            "-fx-background-color: " + color1 + ";" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 10;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);" +
            "-fx-cursor: hand;"
        );
        
        button.setOnMouseEntered(e -> {
            if (!button.isDisabled()) {
                button.setStyle(
                    "-fx-background-color: " + color2 + ";" +
                    "-fx-text-fill: white;" +
                    "-fx-background-radius: 10;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 3);" +
                    "-fx-cursor: hand;"
                );
            }
        });
        
        button.setOnMouseExited(e -> {
            if (!button.isDisabled()) {
                button.setStyle(
                    "-fx-background-color: " + color1 + ";" +
                    "-fx-text-fill: white;" +
                    "-fx-background-radius: 10;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);" +
                    "-fx-cursor: hand;"
                );
            }
        });
        
        return button;
    }
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
