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
        // Background principal - Noir Profond
        root.setBackground(new Background(new BackgroundFill(Color.web("#0A0E0D"), CornerRadii.EMPTY, Insets.EMPTY)));
        
        // Titre - Vert Néon avec effet glow
        Label title = new Label("MONSTRE DE POCHE");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        title.setTextFill(Color.web("#39FF14")); // Vert Néon
        title.setStyle(
            "-fx-effect: dropshadow(gaussian, rgba(57, 255, 20, 0.8), 20, 0, 0, 0), " +
            "dropshadow(gaussian, rgba(57, 255, 20, 0.5), 40, 0, 0, 0);"
        );
        
        // Sous-titre - Gris Clair
        Label subtitle = new Label("Jeu de Combat en Tour par Tour");
        subtitle.setFont(Font.font("Arial", 18));
        subtitle.setTextFill(Color.web("#E0E0E0")); // Gris Clair
        
        // Label pour sélectionner le mode de jeu - Vert Clair Texte
        Label modeLabel = new Label("Choisissez votre mode de jeu :");
        modeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        modeLabel.setTextFill(Color.web("#A8FF9E")); // Vert Clair Texte
        modeLabel.setPadding(new Insets(20, 0, 10, 0));
        
        // Bouton Deux Joueurs (1v1) - Bleu Info -> Cyan Néon au hover
        Button newGameBtn = createStyledButton("🎮 Deux Joueurs (1v1)", "#00B4FF", "#00FFFF");
        newGameBtn.setPrefWidth(350);
        newGameBtn.setPrefHeight(70);
        newGameBtn.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        newGameBtn.setOnAction(e -> app.showTeamSelection(false));
        
        // Label descriptif pour deux joueurs - Gris Moyen
        Label twoPlayersDesc = new Label("Affrontez un autre joueur humain");
        twoPlayersDesc.setFont(Font.font("Arial", 14));
        twoPlayersDesc.setTextFill(Color.web("#8A8A8A")); // Gris Moyen
        
        // Bouton Mode Solo (vs Bot) - Vert Néon -> Vert Acide au hover
        Button soloGameBtn = createStyledButton("🤖 Mode Solo (vs Bot)", "#39FF14", "#00FF41");
        soloGameBtn.setPrefWidth(350);
        soloGameBtn.setPrefHeight(70);
        soloGameBtn.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        soloGameBtn.setOnAction(e -> app.showTeamSelection(true));
        
        // Label descriptif pour mode solo - Gris Moyen
        Label soloDesc = new Label("Affrontez un adversaire contrôlé par l'IA");
        soloDesc.setFont(Font.font("Arial", 14));
        soloDesc.setTextFill(Color.web("#8A8A8A")); // Gris Moyen
        
        // Bouton Quitter - Rouge Danger -> Orange Toxique au hover
        Button quitBtn = createStyledButton("Quitter", "#FF3838", "#FF6B35");
        quitBtn.setPrefWidth(300);
        quitBtn.setPrefHeight(60);
        quitBtn.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        quitBtn.setOnAction(e -> {
            javafx.application.Platform.exit();
        });
        
        // Container pour les boutons de mode de jeu
        VBox gameModeBox = new VBox(10);
        gameModeBox.setAlignment(Pos.CENTER);
        
        VBox twoPlayersBox = new VBox(5);
        twoPlayersBox.setAlignment(Pos.CENTER);
        twoPlayersBox.getChildren().addAll(newGameBtn, twoPlayersDesc);
        
        VBox soloBox = new VBox(5);
        soloBox.setAlignment(Pos.CENTER);
        soloBox.getChildren().addAll(soloGameBtn, soloDesc);
        
        gameModeBox.getChildren().addAll(twoPlayersBox, soloBox);
        
        root.getChildren().addAll(title, subtitle, modeLabel, gameModeBox, quitBtn);
        
        return new Scene(root, 1200, 800);
    }
    
    private Button createStyledButton(String text, String color1, String color2) {
        Button button = new Button(text);
        
        // Convertir hex en RGB pour l'effet glow
        String rgb1 = hexToRgb(color1);
        String rgb2 = hexToRgb(color2);
        
        button.setStyle(
            "-fx-background-color: " + color1 + ";" +
            "-fx-text-fill: #FFFFFF;" + // Blanc Pur
            "-fx-background-radius: 15;" +
            "-fx-border-color: " + color1 + ";" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 15;" +
            "-fx-effect: dropshadow(gaussian, " + rgb1 + ", 15, 0, 0, 0), " +
            "dropshadow(gaussian, rgba(0,0,0,0.5), 5, 0, 0, 2);" +
            "-fx-cursor: hand;"
        );
        
        button.setOnMouseEntered(e -> {
            button.setStyle(
                "-fx-background-color: " + color2 + ";" +
                "-fx-text-fill: #FFFFFF;" + // Blanc Pur
                "-fx-background-radius: 15;" +
                "-fx-border-color: " + color2 + ";" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 15;" +
                "-fx-effect: dropshadow(gaussian, " + rgb2 + ", 25, 0, 0, 0), " +
                "dropshadow(gaussian, rgba(0,0,0,0.7), 8, 0, 0, 3);" +
                "-fx-cursor: hand;"
            );
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle(
                "-fx-background-color: " + color1 + ";" +
                "-fx-text-fill: #FFFFFF;" + // Blanc Pur
                "-fx-background-radius: 15;" +
                "-fx-border-color: " + color1 + ";" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 15;" +
                "-fx-effect: dropshadow(gaussian, " + rgb1 + ", 15, 0, 0, 0), " +
                "dropshadow(gaussian, rgba(0,0,0,0.5), 5, 0, 0, 2);" +
                "-fx-cursor: hand;"
            );
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
}
