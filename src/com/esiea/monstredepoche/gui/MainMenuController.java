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
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

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
        newGameBtn.setPrefWidth(300);
        newGameBtn.setPrefHeight(70);
        newGameBtn.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        newGameBtn.setOnAction(e -> app.showTeamSelection(false));
        
        // Bouton Mode Solo (vs Bot) - Vert Néon -> Vert Acide au hover
        Button soloGameBtn = createStyledButton("🤖 Mode Solo (vs Bot)", "#39FF14", "#00FF41");
        soloGameBtn.setPrefWidth(300);
        soloGameBtn.setPrefHeight(70);
        soloGameBtn.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        soloGameBtn.setOnAction(e -> app.showTeamSelection(true));
        
        // Bouton Quitter - Rouge Danger -> Orange Toxique au hover
        Button quitBtn = createStyledButton("Quitter", "#FF3838", "#FF6B35");
        quitBtn.setPrefWidth(300);
        quitBtn.setPrefHeight(70);
        quitBtn.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        quitBtn.setOnAction(e -> {
            javafx.application.Platform.exit();
        });
        
        // Container horizontal pour les trois boutons sur la même ligne
        HBox buttonsBox = new HBox(30);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.getChildren().addAll(newGameBtn, soloGameBtn, quitBtn);
        
        root.getChildren().addAll(title, subtitle, modeLabel, buttonsBox);
        
        return new Scene(root, 1200, 800);
    }
    
    private Button createStyledButton(String text, String color1, String color2) {
        Button button = new Button(text);
        
        // Convertir hex en RGB pour l'effet glow
        String rgb1 = hexToRgb(color1);
        String rgb2 = hexToRgb(color2);
        
        // Style de base - la taille du texte reste la même
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
        
        // Transition de zoom pour l'effet hover
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), button);
        
        button.setOnMouseEntered(e -> {
            // Changer la couleur et augmenter le glow
            button.setStyle(
                "-fx-background-color: " + color2 + ";" +
                "-fx-text-fill: #FFFFFF;" + // Blanc Pur - même taille de texte
                "-fx-background-radius: 15;" +
                "-fx-border-color: " + color2 + ";" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 15;" +
                "-fx-effect: dropshadow(gaussian, " + rgb2 + ", 25, 0, 0, 0), " +
                "dropshadow(gaussian, rgba(0,0,0,0.7), 8, 0, 0, 3);" +
                "-fx-cursor: hand;"
            );
            // Effet de zoom (1.1 = 10% plus grand)
            scaleTransition.setToX(1.1);
            scaleTransition.setToY(1.1);
            scaleTransition.play();
        });
        
        button.setOnMouseExited(e -> {
            // Revenir au style de base
            button.setStyle(
                "-fx-background-color: " + color1 + ";" +
                "-fx-text-fill: #FFFFFF;" + // Blanc Pur - même taille de texte
                "-fx-background-radius: 15;" +
                "-fx-border-color: " + color1 + ";" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 15;" +
                "-fx-effect: dropshadow(gaussian, " + rgb1 + ", 15, 0, 0, 0), " +
                "dropshadow(gaussian, rgba(0,0,0,0.5), 5, 0, 0, 2);" +
                "-fx-cursor: hand;"
            );
            // Revenir à la taille normale
            scaleTransition.setToX(1.0);
            scaleTransition.setToY(1.0);
            scaleTransition.play();
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
}
