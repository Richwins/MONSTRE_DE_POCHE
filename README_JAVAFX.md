# Interface JavaFX - Monstre de Poche

## Configuration requise

**✅ Le SDK JavaFX est maintenant inclus dans le projet !**

Le projet contient le SDK JavaFX 17.0.17 dans le dossier `javafx-sdk-17.0.17/`. Aucune installation supplémentaire n'est nécessaire.

### Structure du SDK inclus

```
MONSTRE_DE_POCHE/
├── javafx-sdk-17.0.17/     ← SDK JavaFX inclus
│   ├── lib/                ← JARs JavaFX
│   ├── bin/                ← DLL natives (Windows) / .so (Linux)
│   └── legal/              ← Licences
├── src/
├── resources/
└── ...
```

## Utilisation

### Lancer depuis VS Code

1. **Ouvrez VS Code** dans le dossier du projet
2. **Installez l'extension Java** (si ce n'est pas déjà fait) :
   - Ouvrez les extensions (Ctrl+Shift+X)
   - Recherchez "Extension Pack for Java" et installez-la
3. **Configurer VS Code** (optionnel) :
   - Créez un fichier `.vscode/settings.json` à la racine du projet :
   ```json
   {
       "java.project.referencedLibraries": [
           "javafx-sdk-17.0.17/lib/**/*.jar"
       ],
       "java.configuration.runtimes": []
   }
   ```
4. **Configurer le launch.json** (optionnel) :
   - Créez ou modifiez `.vscode/launch.json` :
   ```json
   {
       "version": "0.2.0",
       "configurations": [
           {
               "type": "java",
               "name": "Launch Console",
               "request": "launch",
               "mainClass": "com.esiea.monstredepoche.Main",
               "projectName": "MONSTRE_DE_POCHE",
               "args": []
           },
           {
               "type": "java",
               "name": "Launch GUI",
               "request": "launch",
               "mainClass": "com.esiea.monstredepoche.Main",
               "projectName": "MONSTRE_DE_POCHE",
               "args": ["gui"],
               "vmArgs": "--module-path \"${workspaceFolder}/javafx-sdk-17.0.17/lib\" --add-modules javafx.controls,javafx.fxml,javafx.graphics -Djava.library.path=\"${workspaceFolder}/javafx-sdk-17.0.17/bin\""
           }
       ]
   }
   ```
5. **Lancez le programme** :
   - Appuyez sur `F5` ou allez dans Run → Start Debugging
   - Sélectionnez la configuration "Launch Console" ou "Launch GUI"
   - Ou utilisez le terminal intégré (voir ci-dessous)

### Lancer l'interface graphique

**Depuis VS Code (Terminal intégré)** :

**Windows (PowerShell)** :
```powershell
# Option 1 : Utiliser le script (recommandé)
.\launch-gui.ps1

# Option 2 : Commande manuelle
$javafxPath = "javafx-sdk-17.0.17\lib"
$binPath = "javafx-sdk-17.0.17\bin"
$env:PATH = "$binPath;$env:PATH"
java --module-path "$javafxPath" --add-modules javafx.controls,javafx.fxml,javafx.graphics -cp "out\production\MONSTRE_DE_POCHE;resources" com.esiea.monstredepoche.Main gui
```

**Linux** :
```bash
# Option 1 : Utiliser le script (si disponible)
./launch-gui.sh

# Option 2 : Commande manuelle
JAVAFX_PATH="javafx-sdk-17.0.17/lib"
export LD_LIBRARY_PATH=javafx-sdk-17.0.17/bin:$LD_LIBRARY_PATH
java --module-path "$JAVAFX_PATH" --add-modules javafx.controls,javafx.fxml,javafx.graphics -cp "out/production/MONSTRE_DE_POCHE:resources" com.esiea.monstredepoche.Main gui
```

### Lancer l'interface console (par défaut)

**Windows (PowerShell)** :
```powershell
# Option 1 : Utiliser le script (recommandé)
.\launch-console.ps1

# Option 2 : Commande manuelle
java -cp "out\production\MONSTRE_DE_POCHE;resources" com.esiea.monstredepoche.Main
```

**Linux** :
```bash
# Option 1 : Utiliser le script (si disponible)
./launch-console.sh

# Option 2 : Commande manuelle
java -cp "out/production/MONSTRE_DE_POCHE:resources" com.esiea.monstredepoche.Main
```

### Choix interactif

Si vous lancez sans argument, le programme vous demandera de choisir :
- `1` pour l'interface console
- `2` pour l'interface graphique JavaFX

## Notes importantes

- **Le SDK JavaFX est inclus** : Aucune installation manuelle nécessaire
- **Les affichages console sont conservés** : Toutes les actions de combat s'affichent également dans la console
- **Le code existant n'est pas modifié** : L'interface JavaFX utilise les contrôleurs existants sans les modifier
- **Structure préservée** : Tous les fichiers GUI sont dans le package `gui/` pour séparer l'interface du code métier

## Structure des fichiers GUI

```
src/com/esiea/monstredepoche/gui/
├── MonsterGameApp.java          # Application JavaFX principale
├── MainMenuController.java      # Contrôleur du menu principal
├── TeamSelectionController.java # Contrôleur de sélection d'équipe
└── BattleViewController.java    # Contrôleur de la vue de combat

resources/styles/
└── pokemon-style.css            # Feuille de style Pokémon
```

## Palette de couleurs utilisée

- **Bleu Pokémon** : #3B4CCA, #0075BE
- **Jaune Pikachu** : #FFCB05, #F4D03F
- **Feu** : #FF9741, #F08030
- **Eau** : #6890F0, #4592C4
- **Plante** : #78C850, #7AC74C
- **Électrique** : #F8D030, #F7D02C
- **Combat** : #C03028, #C22E28

## Dépannage

### Erreur "javafx cannot be resolved"

JavaFX n'est pas dans le classpath. Vérifiez que :
- Le dossier `javafx-sdk-17.0.17/lib/` existe et contient les JARs JavaFX
- Le fichier `.vscode/settings.json` référence `javafx-sdk-17.0.17/lib/**/*.jar` (si vous utilisez VS Code)
- Vous avez rechargé VS Code après avoir modifié les paramètres

### Erreur "Error initializing QuantumRenderer: no suitable pipeline found"

Cette erreur indique que les DLL natives JavaFX ne sont pas trouvées. Solutions :
- **Windows** : Vérifiez que le dossier `javafx-sdk-17.0.17/bin/` contient des fichiers `.dll`
- **Linux** : Vérifiez que le dossier `javafx-sdk-17.0.17/bin/` contient des fichiers `.so`
- Le script `launch-gui.ps1` (Windows) ou `launch-gui.sh` (Linux) ajoute automatiquement le dossier `bin/` au PATH/LD_LIBRARY_PATH

### L'interface ne se lance pas

Vérifiez que :
- Le SDK JavaFX est présent dans `javafx-sdk-17.0.17/`
- Les JARs JavaFX sont dans `javafx-sdk-17.0.17/lib/`
- Les DLL natives sont dans `javafx-sdk-17.0.17/bin/` (Windows) ou `.so` (Linux)
- Le projet est compilé (`out/production/MONSTRE_DE_POCHE` existe)
- Vous utilisez Java 11 ou supérieur
- Les modules JavaFX sont bien ajoutés (`--add-modules javafx.controls,javafx.fxml,javafx.graphics`)

### Les styles ne s'appliquent pas

Vérifiez que le fichier `pokemon-style.css` est bien dans `resources/styles/` et que le chemin est correct dans le code.

## Installation manuelle (si nécessaire)

Si pour une raison quelconque le SDK JavaFX n'est pas inclus dans le projet :

1. **Télécharger JavaFX SDK** depuis [https://openjfx.io/](https://openjfx.io/)
   - Choisissez la version compatible avec votre JDK (Java 11+ recommandé)
   - Téléchargez le SDK pour votre système d'exploitation (Windows ou Linux)

2. **Extraire le SDK** dans le dossier du projet :
   - Renommez le dossier extrait en `javafx-sdk-17.0.17`
   - Placez-le à la racine du projet

3. **Vérifier la structure** :
   ```
   MONSTRE_DE_POCHE/
   ├── javafx-sdk-17.0.17/
   │   ├── lib/     ← Doit contenir les JARs
   │   └── bin/     ← Doit contenir les DLL/.so
   ```
