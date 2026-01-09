# Interface JavaFX - Monstre de Poche

## Configuration requise

Pour utiliser l'interface graphique JavaFX, vous devez ajouter JavaFX à votre projet.

### Option 1 : JavaFX SDK (Recommandé)

1. **Télécharger JavaFX SDK** depuis [https://openjfx.io/](https://openjfx.io/)
   - Choisissez la version compatible avec votre JDK (Java 11+ recommandé)
   - Téléchargez le SDK pour votre système d'exploitation (Windows ou Linux)

2. **Ajouter JavaFX au classpath** :

   #### **VS Code (Windows et Linux)**

   **Méthode 1 : Via les paramètres du projet (Recommandé)**
   
   1. **Créer un dossier `lib` dans votre projet** :
      ```
      MONSTRE_DE_POCHE/
      ├── lib/          ← Créer ce dossier
      ├── src/
      ├── resources/
      └── ...
      ```
   
   2. **Extraire les JARs JavaFX** :
      - Extrayez le SDK JavaFX téléchargé
      - Copiez tous les fichiers `.jar` du dossier `lib` du SDK vers le dossier `lib` de votre projet
      - Vous devriez avoir des fichiers comme : `javafx-controls.jar`, `javafx-fxml.jar`, `javafx-graphics.jar`, etc.
   
   3. **Copier les DLL natives (Windows - IMPORTANT)** :
      - Dans le SDK JavaFX, il y a un dossier `bin/` qui contient les DLL natives nécessaires
      - **Utilisez le script `setup-javafx-native.ps1` fourni** (recommandé) :
        ```powershell
        .\setup-javafx-native.ps1
        ```
      - Le script copie automatiquement les DLL depuis le SDK vers le dossier `bin/` de votre projet
      - **Note** : Le SDK JavaFX doit être dans `..\openjfx-17.0.17_windows-x64_bin-sdk\javafx-sdk-17.0.17`
   
   3. **Configurer VS Code** :
      - Créez un fichier `.vscode/settings.json` à la racine du projet :
      ```json
      {
          "java.project.referencedLibraries": [
              "lib/**/*.jar"
          ],
          "java.configuration.runtimes": []
      }
      ```
   
   5. **Configurer le launch.json** (pour lancer avec JavaFX) :
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
                  "vmArgs": "--module-path \"${workspaceFolder}/lib\" --add-modules javafx.controls,javafx.fxml,javafx.graphics -Djava.library.path=\"${workspaceFolder}/bin\""
              }
          ]
      }
      ```
   
   **Méthode 2 : Via la ligne de commande**
   
   **Windows (PowerShell)** :
   ```powershell
   # 1. Copier les DLL natives (une seule fois) :
   .\setup-javafx-native.ps1
   
   # 2. Compiler avec JavaFX (utilise les JARs dans lib/)
   javac --module-path lib --add-modules javafx.controls,javafx.fxml,javafx.graphics -d out -cp src -encoding UTF-8 src\com\esiea\monstredepoche\models\monsters\*.java src\com\esiea\monstredepoche\*.java
   
   # 3. Lancer avec JavaFX (utilise lib/ et bin/ du projet)
   $env:PATH = "bin;$env:PATH"
   java --module-path lib --add-modules javafx.controls,javafx.fxml,javafx.graphics -cp "out;resources" com.esiea.monstredepoche.Main gui
   
   # Ou simplement utiliser le script :
   .\launch-gui.ps1
   ```
   
   **Linux** :
   ```bash
   # 1. Télécharger et extraire JavaFX SDK dans ~/javafx-sdk (par exemple)
   
   # 2. Compiler avec JavaFX
   javac --module-path ~/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml,javafx.graphics -d out -cp src -encoding UTF-8 $(find src -name "*.java")
   
   # 3. Lancer avec JavaFX
   java --module-path ~/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml,javafx.graphics -cp "out:resources" com.esiea.monstredepoche.Main gui
   ```

   #### **IntelliJ IDEA**
   
   - File → Project Structure → Libraries
   - Cliquez sur "+" → Java
   - Sélectionnez le dossier `lib` du SDK JavaFX téléchargé
   - Ajoutez les modules : `javafx.controls`, `javafx.fxml`, `javafx.graphics`

### Option 2 : Maven (si vous utilisez Maven)

Ajoutez dans votre `pom.xml` :

```xml
<dependencies>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-controls</artifactId>
        <version>17.0.2</version>
    </dependency>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-fxml</artifactId>
        <version>17.0.2</version>
    </dependency>
</dependencies>
```

## Utilisation

### Lancer depuis VS Code

1. **Ouvrez VS Code** dans le dossier du projet
2. **Installez l'extension Java** (si ce n'est pas déjà fait) :
   - Ouvrez les extensions (Ctrl+Shift+X)
   - Recherchez "Extension Pack for Java" et installez-la
3. **Lancez le programme** :
   - Appuyez sur `F5` ou allez dans Run → Start Debugging
   - Sélectionnez la configuration "Launch Console" ou "Launch GUI"
   - Ou utilisez le terminal intégré (voir ci-dessous)

### Lancer l'interface graphique

**Depuis VS Code (Terminal intégré)** :

**Windows (PowerShell)** :
```powershell
# Option 1 : Utiliser le script (recommandé)
.\launch-gui.ps1

# Option 2 : Commande manuelle (tout est dans le projet)
$env:PATH = "bin;$env:PATH"
java --module-path lib --add-modules javafx.controls,javafx.fxml,javafx.graphics -cp "out\production\MONSTRE_DE_POCHE;resources" com.esiea.monstredepoche.Main gui
```

**Linux** :
```bash
# Option 1 : Utiliser le script (si disponible)
./launch-gui.sh

# Option 2 : Commande manuelle
export LD_LIBRARY_PATH=bin:$LD_LIBRARY_PATH
java --module-path lib --add-modules javafx.controls,javafx.fxml,javafx.graphics -cp "out/production/MONSTRE_DE_POCHE:resources" com.esiea.monstredepoche.Main gui
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
- Les JARs JavaFX sont bien dans le dossier `lib/` du projet
- Le fichier `.vscode/settings.json` référence `lib/**/*.jar`
- Vous avez rechargé VS Code après avoir ajouté les JARs

### Erreur "Error initializing QuantumRenderer: no suitable pipeline found"

Cette erreur indique que les DLL natives JavaFX ne sont pas trouvées. Solutions :
- **Windows** : Exécutez `.\setup-javafx-native.ps1` pour copier les DLL dans `bin/`
- Vérifiez que le dossier `bin/` contient des fichiers `.dll`
- Le script `launch-gui.ps1` ajoute automatiquement `bin/` au PATH

### L'interface ne se lance pas

Vérifiez que :
- Les JARs JavaFX sont dans `lib/` et les DLL dans `bin/`
- Le projet est compilé (`out/production/MONSTRE_DE_POCHE` existe)
- Vous utilisez Java 11 ou supérieur
- Les modules JavaFX sont bien ajoutés (`--add-modules javafx.controls,javafx.fxml,javafx.graphics`)

### Les styles ne s'appliquent pas

Vérifiez que le fichier `pokemon-style.css` est bien dans `resources/styles/` et que le chemin est correct dans le code.
