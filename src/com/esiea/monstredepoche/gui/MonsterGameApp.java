package com.esiea.monstredepoche.gui;

import com.esiea.monstredepoche.controllers.GameController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 * Application JavaFX principale pour Monstre de Poche.
 * Utilise les contrôleurs existants sans modifier leur fonctionnement.
 */
public class MonsterGameApp extends Application {
    private GameController gameController;
    private Stage primaryStage;
    private Scene mainMenuScene;
    private Scene teamSelectionScene;
    private Scene battleScene;
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.gameController = new GameController();
        
        primaryStage.setTitle("Monstre de Poche");
        primaryStage.setWidth(1200);
        primaryStage.setHeight(800);
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(700);
        
        // Charger le CSS (si disponible)
        String css = null;
        try {
            css = getClass().getResource("/styles/pokemon-style.css").toExternalForm();
        } catch (Exception e) {
            // CSS non disponible, continuer sans style
            System.out.println("Note: Fichier CSS non trouvé, utilisation des styles par défaut.");
        }
        
        // Créer les scènes
        createMainMenuScene(css);
        createTeamSelectionScene(css);
        createBattleScene(css);
        
        // Afficher le menu principal
        primaryStage.setScene(mainMenuScene);
        primaryStage.show();
        
        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            closeApplication();
        });
    }
    
    private void createMainMenuScene(String css) {
        MainMenuController controller = new MainMenuController(this);
        mainMenuScene = controller.createScene();
        if (css != null) {
            mainMenuScene.getStylesheets().add(css);
        }
    }
    
    private void createTeamSelectionScene(String css) {
        TeamSelectionController controller = new TeamSelectionController(this, gameController);
        teamSelectionScene = controller.createScene();
        if (css != null) {
            teamSelectionScene.getStylesheets().add(css);
        }
    }
    
    private void createBattleScene(String css) {
        BattleViewController controller = new BattleViewController(this, gameController);
        battleScene = controller.createScene();
        if (css != null) {
            battleScene.getStylesheets().add(css);
        }
    }
    
    public void showMainMenu() {
        primaryStage.setScene(mainMenuScene);
    }
    
    public void showTeamSelection() {
        primaryStage.setScene(teamSelectionScene);
    }
    
    public void showBattle() {
        primaryStage.setScene(battleScene);
    }
    
    private void closeApplication() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quitter");
        alert.setHeaderText("Voulez-vous vraiment quitter le jeu ?");
        alert.setContentText("Toute progression non sauvegardée sera perdue.");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Platform.exit();
            }
        });
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
