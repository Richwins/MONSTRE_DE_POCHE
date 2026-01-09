package com.esiea.monstredepoche.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Contrôleur pour le menu principal de l'application.
 */
public class MainMenuController {
    private MonsterGameApp app;
    
    public MainMenuController(MonsterGameApp app) {
        this.app = app;
    }
    
    public Scene createScene() {
        VBox root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.setBackground(new Background(new BackgroundFill(Color.web("#FFFFFF"), CornerRadii.EMPTY, Insets.EMPTY)));
        
        // Titre
        Label title = new Label("MONSTRE DE POCHE");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        title.setTextFill(Color.web("#3B4CCA"));
        title.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 2);");
        
        // Sous-titre
        Label subtitle = new Label("Jeu de Combat en Tour par Tour");
        subtitle.setFont(Font.font("Arial", 18));
        subtitle.setTextFill(Color.web("#2C2C2C"));
        
        // Bouton Nouvelle Partie
        Button newGameBtn = createStyledButton("Nouvelle Partie", "#3B4CCA", "#0075BE");
        newGameBtn.setPrefWidth(300);
        newGameBtn.setPrefHeight(60);
        newGameBtn.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        newGameBtn.setOnAction(e -> app.showTeamSelection());
        
        // Bouton Quitter
        Button quitBtn = createStyledButton("Quitter", "#C03028", "#C22E28");
        quitBtn.setPrefWidth(300);
        quitBtn.setPrefHeight(60);
        quitBtn.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        quitBtn.setOnAction(e -> {
            javafx.application.Platform.exit();
        });
        
        root.getChildren().addAll(title, subtitle, newGameBtn, quitBtn);
        
        return new Scene(root, 1200, 800);
    }
    
    private Button createStyledButton(String text, String color1, String color2) {
        Button button = new Button(text);
        button.setStyle(
            "-fx-background-color: " + color1 + ";" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 15;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);" +
            "-fx-cursor: hand;"
        );
        
        button.setOnMouseEntered(e -> {
            button.setStyle(
                "-fx-background-color: " + color2 + ";" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 15;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 3);" +
                "-fx-cursor: hand;"
            );
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle(
                "-fx-background-color: " + color1 + ";" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 15;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);" +
                "-fx-cursor: hand;"
            );
        });
        
        return button;
    }
}
