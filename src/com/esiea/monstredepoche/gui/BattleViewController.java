package com.esiea.monstredepoche.gui;

import com.esiea.monstredepoche.controllers.GameController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Contrôleur pour la vue de combat.
 * Utilise le GameController existant sans le modifier.
 */
public class BattleViewController {
    private MonsterGameApp app;
    private GameController gameController;
    
    public BattleViewController(MonsterGameApp app, GameController gameController) {
        this.app = app;
        this.gameController = gameController;
    }
    
    public Scene createScene() {
        BorderPane root = new BorderPane();
        root.setBackground(new Background(new BackgroundFill(Color.web("#FFFFFF"), CornerRadii.EMPTY, Insets.EMPTY)));
        root.setPadding(new Insets(20));
        
        // Zone de combat (centre)
        VBox battleArea = new VBox(20);
        battleArea.setAlignment(Pos.CENTER);
        battleArea.setPadding(new Insets(40));
        
        Label battleLabel = new Label("ARÈNE DE COMBAT");
        battleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        battleLabel.setTextFill(Color.web("#3B4CCA"));
        
        Label infoLabel = new Label("Le combat se déroule dans la console.\nConsultez la console pour voir les actions.");
        infoLabel.setFont(Font.font("Arial", 18));
        infoLabel.setTextFill(Color.web("#2C2C2C"));
        infoLabel.setAlignment(Pos.CENTER);
        
        battleArea.getChildren().addAll(battleLabel, infoLabel);
        
        // Zone d'actions (bas)
        HBox actionArea = new HBox(15);
        actionArea.setAlignment(Pos.CENTER);
        actionArea.setPadding(new Insets(20));
        actionArea.setStyle(
            "-fx-background-color: #F5F5F5;" +
            "-fx-background-radius: 15;" +
            "-fx-border-color: #3B4CCA;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 15;"
        );
        
        Button attackBtn = createActionButton("Attaquer", "#F8D030");
        Button itemBtn = createActionButton("Objet", "#6890F0");
        Button switchBtn = createActionButton("Changer", "#78C850");
        Button menuBtn = createActionButton("Menu", "#C03028");
        
        menuBtn.setOnAction(e -> app.showMainMenu());
        
        actionArea.getChildren().addAll(attackBtn, itemBtn, switchBtn, menuBtn);
        
        root.setCenter(battleArea);
        root.setBottom(actionArea);
        
        return new Scene(root, 1200, 800);
    }
    
    private Button createActionButton(String text, String color) {
        Button button = new Button(text);
        button.setPrefWidth(150);
        button.setPrefHeight(60);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        button.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 10;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);" +
            "-fx-cursor: hand;"
        );
        
        button.setOnMouseEntered(e -> {
            button.setStyle(
                "-fx-background-color: " + darkenColor(color) + ";" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 10;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 3);" +
                "-fx-cursor: hand;"
            );
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle(
                "-fx-background-color: " + color + ";" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 10;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);" +
                "-fx-cursor: hand;"
            );
        });
        
        return button;
    }
    
    private String darkenColor(String color) {
        // Simplification : retourner une couleur plus foncée
        // Dans une vraie implémentation, on parserait la couleur hex et on la foncerait
        return color; // Placeholder
    }
}
