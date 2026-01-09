# Monstre de Poche - Jeu de Combat en Tour par Tour

### Etudiant : SESSOU Winsou Richard 

## Description
Jeu de combat inspiré de Pokémon où deux joueurs s'affrontent avec leurs équipes de monstres. Chaque monstre possède un type élémentaire avec des forces et faiblesses, des attaques spéciales et des capacités uniques.

## Démarrage rapide

### Windows (PowerShell)
```powershell
# Se placer dans le dossier du projet
cd "chemin\vers\MONSTRE_DE_POCHE"

# Compiler le projet avec JavaFX
$javafxPath = "javafx-sdk-17.0.17\lib"
$javaFiles = Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac --module-path "$javafxPath" --add-modules javafx.controls,javafx.fxml,javafx.graphics -d out\production\MONSTRE_DE_POCHE -cp "src;resources" -encoding UTF-8 $javaFiles

# Lancer l'interface graphique (nécessite JavaFX)
$javafxPath = "javafx-sdk-17.0.17\lib"
$binPath = "javafx-sdk-17.0.17\bin"
$env:PATH = "$binPath;$env:PATH"
java --module-path "$javafxPath" --add-modules javafx.controls,javafx.fxml,javafx.graphics -cp "out\production\MONSTRE_DE_POCHE;resources" com.esiea.monstredepoche.Main gui

# OU lancer l'interface console
java -cp "out\production\MONSTRE_DE_POCHE;resources" com.esiea.monstredepoche.Main
```

### Linux (Bash)
```bash
# Se placer dans le dossier du projet
cd /chemin/vers/MONSTRE_DE_POCHE

# Rendre les scripts exécutables (première fois)
chmod +x launch-gui.sh launch-console.sh

# Modifier le chemin JavaFX dans launch-gui.sh si nécessaire
# Puis lancer l'interface graphique
./launch-gui.sh

# OU lancer l'interface console
./launch-console.sh
```

**Note** : Pour plus de détails, consultez les guides complets ci-dessous pour Windows et Linux.

---

## Prérequis

- **Java 11 ou supérieur** (JDK recommandé)
- **JavaFX SDK** (uniquement pour l'interface graphique)
  - Télécharger depuis : https://openjfx.io/
  - Version recommandée : JavaFX 17 ou supérieur

## Installation

1. **Extraire l'archive** `monstre-de-poche.zip`
2. **Vérifier les fichiers** : Les fichiers `monsters.txt` et `attacks.txt` doivent être présents dans le dossier `resources/`
3. **Installer JavaFX SDK** (uniquement pour l'interface graphique) :
   - Télécharger depuis https://openjfx.io/
   - Extraire l'archive
   - Noter le chemin vers le dossier `lib` du SDK
4. **Compiler le projet** (voir guides ci-dessous selon votre système)

## Compilation dans VS Code

Si vous utilisez **VS Code**, vous pouvez compiler directement dans le terminal intégré (`Ctrl + ù` ou `Ctrl + '`).

### Windows (PowerShell dans VS Code)

#### Compiler avec JavaFX (recommandé - pour GUI et console) :
```powershell
$javafxPath = "javafx-sdk-17.0.17\lib"
$javaFiles = Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac --module-path "$javafxPath" --add-modules javafx.controls,javafx.fxml,javafx.graphics -d out\production\MONSTRE_DE_POCHE -cp "src;resources" -encoding UTF-8 $javaFiles
```

#### Compiler sans JavaFX (console uniquement) :
```powershell
$javaFiles = Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -d out\production\MONSTRE_DE_POCHE -cp "src;resources" -encoding UTF-8 $javaFiles
```

### Linux (Bash dans VS Code)

#### Compiler avec JavaFX (recommandé - pour GUI et console) :
```bash
JAVAFX_PATH="javafx-sdk-17.0.17/lib"
javac --module-path "$JAVAFX_PATH" --add-modules javafx.controls,javafx.fxml,javafx.graphics -d out/production/MONSTRE_DE_POCHE -cp "src:resources" -encoding UTF-8 $(find src -name "*.java")
```

#### Compiler sans JavaFX (console uniquement) :
```bash
javac -d out/production/MONSTRE_DE_POCHE -cp "src:resources" -encoding UTF-8 $(find src -name "*.java")
```

**Note** : VS Code utilise automatiquement le terminal approprié (PowerShell sur Windows, Bash sur Linux). Les commandes ci-dessus fonctionnent directement dans le terminal intégré.

## Lancement dans VS Code (Windows)

### Méthode 1 : Utiliser les scripts PowerShell (Recommandé)

1. **Ouvrir le terminal dans VS Code** :
   - Appuyez sur `Ctrl + ù` (ou `Ctrl + '`)
   - Ou allez dans **Terminal** → **Nouveau terminal**

2. **Vérifier que vous êtes dans le bon dossier** :
   ```powershell
   pwd
   ```
   Si vous n'êtes pas dans le dossier du projet, naviguez-y :
   ```powershell
   cd "G:\Cours ESIEA\Semestre 1\java\MONSTRE_DE_POCHE"
   ```

3. **Lancer l'interface graphique** :
   ```powershell
   .\launch-gui.ps1
   ```

4. **OU lancer l'interface console** :
   ```powershell
   .\launch-console.ps1
   ```

**Note** : Si vous obtenez une erreur de politique d'exécution PowerShell, exécutez d'abord :
```powershell
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
```

### Méthode 2 : Commandes manuelles dans le terminal VS Code

#### Étape 1 : Compiler le projet

**Pour l'interface graphique (avec JavaFX)** :
```powershell
$javafxPath = "javafx-sdk-17.0.17\lib"
$javaFiles = Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac --module-path "$javafxPath" --add-modules javafx.controls,javafx.fxml,javafx.graphics -d out\production\MONSTRE_DE_POCHE -cp "src;resources" -encoding UTF-8 $javaFiles
```

**Pour l'interface console uniquement** :
```powershell
$javaFiles = Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -d out\production\MONSTRE_DE_POCHE -cp "src;resources" -encoding UTF-8 $javaFiles
```

#### Étape 2 : Lancer le projet

**Interface graphique** :
```powershell
$javafxPath = "javafx-sdk-17.0.17\lib"
$binPath = "javafx-sdk-17.0.17\bin"
$env:PATH = "$binPath;$env:PATH"
java --module-path "$javafxPath" --add-modules javafx.controls,javafx.fxml,javafx.graphics -cp "out\production\MONSTRE_DE_POCHE;resources" com.esiea.monstredepoche.Main gui
```

**Interface console** :
```powershell
java -cp "out\production\MONSTRE_DE_POCHE;resources" com.esiea.monstredepoche.Main
```

### Résolution des problèmes courants dans VS Code

#### Erreur "javac n'est pas reconnu"
- Vérifiez que Java JDK est installé : `java -version`
- Vérifiez que `javac` est dans le PATH : `javac -version`
- Si nécessaire, ajoutez Java au PATH dans les variables d'environnement Windows

#### Erreur "cannot find symbol: class javafx"
- Le projet n'est pas compilé avec JavaFX
- Utilisez la commande de compilation avec `--module-path` et `--add-modules`

#### Erreur "no suitable pipeline found"
- Les DLL natives JavaFX ne sont pas trouvées
- Vérifiez que `javafx-sdk-17.0.17\bin\` contient des fichiers `.dll`
- Le script `launch-gui.ps1` ajoute automatiquement le dossier `bin/` au PATH

#### Erreur de politique d'exécution PowerShell
```powershell
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
```

## Lancement du projet

Le jeu peut être lancé en **interface console** ou en **interface graphique JavaFX**.

### Choix du mode d'affichage

Il existe **deux façons** de choisir le mode :

#### Méthode 1 : Argument en ligne de commande (recommandé)

Lancez directement avec un argument pour éviter la question interactive :

**Interface graphique (GUI)** :
```powershell
# Windows - Compilation
cd "chemin\vers\MONSTRE_DE_POCHE"
$javafxPath = "javafx-sdk-17.0.17\lib"
$javaFiles = Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac --module-path "$javafxPath" --add-modules javafx.controls,javafx.fxml,javafx.graphics -d out\production\MONSTRE_DE_POCHE -cp "src;resources" -encoding UTF-8 $javaFiles

# Windows - Lancement
$javafxPath = "javafx-sdk-17.0.17\lib"
$binPath = "javafx-sdk-17.0.17\bin"
$env:PATH = "$binPath;$env:PATH"
java --module-path "$javafxPath" --add-modules javafx.controls,javafx.fxml,javafx.graphics -cp "out\production\MONSTRE_DE_POCHE;resources" com.esiea.monstredepoche.Main gui
```

```bash
# Linux - Compilation
cd /chemin/vers/MONSTRE_DE_POCHE
JAVAFX_PATH="/chemin/vers/javafx-sdk/lib"
javac --module-path "$JAVAFX_PATH" --add-modules javafx.controls,javafx.fxml,javafx.graphics -d out/production/MONSTRE_DE_POCHE -cp "src:resources" -encoding UTF-8 $(find src -name "*.java")

# Linux - Lancement
java --module-path "$JAVAFX_PATH" --add-modules javafx.controls,javafx.fxml,javafx.graphics -cp "out/production/MONSTRE_DE_POCHE:resources" com.esiea.monstredepoche.Main gui
```

**Interface console** :
```powershell
# Windows - Compilation
cd "chemin\vers\MONSTRE_DE_POCHE"
$javaFiles = Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -d out\production\MONSTRE_DE_POCHE -cp "src;resources" -encoding UTF-8 $javaFiles

# Windows - Lancement
java -cp "out\production\MONSTRE_DE_POCHE;resources" com.esiea.monstredepoche.Main
```

```bash
# Linux - Compilation
cd /chemin/vers/MONSTRE_DE_POCHE
javac -d out/production/MONSTRE_DE_POCHE -cp "src:resources" -encoding UTF-8 $(find src -name "*.java")

# Linux - Lancement
java -cp "out/production/MONSTRE_DE_POCHE:resources" com.esiea.monstredepoche.Main
```

**Arguments acceptés pour le mode GUI** :
- `gui`
- `--gui`
- `-g`

#### Méthode 2 : Choix interactif

Si vous lancez **sans argument**, le programme vous demande de choisir :
```
=== Monstre de Poche ===
Choisissez le mode d'affichage :
1. Interface console (par défaut)
2. Interface graphique (JavaFX)
Votre choix (1 ou 2) : 
```

- Tapez **`1`** puis Entrée → Interface console
  - Ensuite, vous pourrez choisir entre :
    - **1. Mode Deux Joueurs (1v1)** : Affrontez un autre joueur humain
    - **2. Mode Solo (vs Bot)** : Affrontez un adversaire contrôlé par l'IA
- Tapez **`2`** puis Entrée → Interface graphique
  - Ensuite, vous pourrez choisir entre les mêmes modes de jeu dans le menu graphique

---

## Guide de lancement - Windows

### Méthode 1 : Utiliser les scripts PowerShell (Recommandé)

Le projet inclut des scripts PowerShell pour faciliter le lancement :

#### Compilation et lancement de l'interface graphique

```powershell
# Se placer à la racine du projet
cd "chemin\vers\MONSTRE_DE_POCHE"

# Compiler le projet avec JavaFX
$javafxPath = "javafx-sdk-17.0.17\lib"
$javaFiles = Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac --module-path "$javafxPath" --add-modules javafx.controls,javafx.fxml,javafx.graphics -d out\production\MONSTRE_DE_POCHE -cp "src;resources" -encoding UTF-8 $javaFiles

# Lancer l'interface graphique
$javafxPath = "javafx-sdk-17.0.17\lib"
$binPath = "javafx-sdk-17.0.17\bin"
$env:PATH = "$binPath;$env:PATH"
java --module-path "$javafxPath" --add-modules javafx.controls,javafx.fxml,javafx.graphics -cp "out\production\MONSTRE_DE_POCHE;resources" com.esiea.monstredepoche.Main gui
```

#### Compilation et lancement de l'interface console

```powershell
# Se placer à la racine du projet
cd "chemin\vers\MONSTRE_DE_POCHE"

# Compiler le projet (sans JavaFX)
$javaFiles = Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -d out\production\MONSTRE_DE_POCHE -cp "src;resources" -encoding UTF-8 $javaFiles

# Lancer l'interface console
java -cp "out\production\MONSTRE_DE_POCHE;resources" com.esiea.monstredepoche.Main
```

### Méthode 2 : Ligne de commande manuelle

#### Compilation

```powershell
# Se placer à la racine du projet
cd "chemin\vers\MONSTRE_DE_POCHE"

# Compiler avec JavaFX (pour l'interface graphique)
$javafxPath = "javafx-sdk-17.0.17\lib"
$javaFiles = Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac --module-path "$javafxPath" --add-modules javafx.controls,javafx.fxml,javafx.graphics -d out\production\MONSTRE_DE_POCHE -cp "src;resources" -encoding UTF-8 $javaFiles

# OU compiler sans JavaFX (pour l'interface console uniquement)
$javaFiles = Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -d out\production\MONSTRE_DE_POCHE -cp "src;resources" -encoding UTF-8 $javaFiles
```

#### Lancement

```powershell
# Interface console (par défaut)
java -cp "out\production\MONSTRE_DE_POCHE;resources" com.esiea.monstredepoche.Main

# Interface graphique (nécessite JavaFX)
$javafxPath = "javafx-sdk-17.0.17\lib"
$binPath = "javafx-sdk-17.0.17\bin"
$env:PATH = "$binPath;$env:PATH"  # Ajouter les DLL natives au PATH
java --module-path "$javafxPath" --add-modules javafx.controls,javafx.fxml,javafx.graphics -cp "out\production\MONSTRE_DE_POCHE;resources" com.esiea.monstredepoche.Main gui
```

### Configuration JavaFX sur Windows

**JavaFX inclus dans le projet** :
- Le dossier `javafx-sdk-17.0.17` est inclus dans le projet
- **Lib** : `javafx-sdk-17.0.17\lib` (chemin relatif)
- **Bin** : `javafx-sdk-17.0.17\bin` (chemin relatif)

Le projet est maintenant **autonome** et ne dépend plus de fichiers externes. Tous les chemins sont relatifs au dossier du projet.

---

## Guide de lancement - Linux

### Méthode 1 : Script Bash (si disponible)

```bash
# Se placer à la racine du projet
cd /chemin/vers/MONSTRE_DE_POCHE

# Rendre le script exécutable (si nécessaire)
chmod +x launch-gui.sh
chmod +x launch-console.sh

# Lancer l'interface graphique
./launch-gui.sh

# OU lancer l'interface console
./launch-console.sh
```

### Méthode 2 : Ligne de commande manuelle

#### Compilation

```bash
# Se placer à la racine du projet
cd /chemin/vers/MONSTRE_DE_POCHE

# Compiler avec JavaFX (pour l'interface graphique)
JAVAFX_PATH="/chemin/vers/javafx-sdk/lib"
javac --module-path "$JAVAFX_PATH" --add-modules javafx.controls,javafx.fxml,javafx.graphics \
  -d out/production/MONSTRE_DE_POCHE \
  -cp "src:resources" \
  -encoding UTF-8 \
  $(find src -name "*.java")

# OU compiler sans JavaFX (pour l'interface console uniquement)
javac -d out/production/MONSTRE_DE_POCHE \
  -cp "src:resources" \
  -encoding UTF-8 \
  $(find src -name "*.java")
```

#### Lancement

```bash
# Interface console (par défaut)
java -cp "out/production/MONSTRE_DE_POCHE:resources" com.esiea.monstredepoche.Main

# Interface graphique (nécessite JavaFX)
JAVAFX_PATH="/chemin/vers/javafx-sdk/lib"
java --module-path "$JAVAFX_PATH" \
  --add-modules javafx.controls,javafx.fxml,javafx.graphics \
  -cp "out/production/MONSTRE_DE_POCHE:resources" \
  com.esiea.monstredepoche.Main gui
```

### Configuration JavaFX sur Linux

1. **Télécharger JavaFX SDK** depuis https://openjfx.io/ (choisir la version Linux)
2. **Extraire** l'archive :
   ```bash
   tar -xzf openjfx-17.0.17_linux-x64_bin-sdk.tar.gz
   ```
3. **Noter le chemin** vers le dossier `lib` (ex: `/opt/javafx-sdk-17.0.17/lib`)
4. **Installer les dépendances système** (si nécessaire) :
   ```bash
   # Ubuntu/Debian
   sudo apt-get install openjfx
   
   # Fedora
   sudo dnf install java-openjfx
   ```

### Scripts Bash inclus

Le projet inclut des scripts Bash pour faciliter le lancement :

#### Utiliser les scripts (recommandé)

```bash
# Rendre les scripts exécutables (première fois uniquement)
chmod +x launch-gui.sh launch-console.sh

# Modifier le chemin JavaFX dans launch-gui.sh si nécessaire
# Éditer la ligne: JAVAFX_PATH="/chemin/vers/javafx-sdk/lib"

# Lancer l'interface graphique
./launch-gui.sh

# OU lancer l'interface console
./launch-console.sh
```

**Note** : Vous devez modifier le chemin `JAVAFX_PATH` dans `launch-gui.sh` avec votre chemin d'installation JavaFX.

---

## Interface graphique JavaFX

Le projet inclut une interface graphique moderne et complète avec un style dark/neon inspiré des jeux cyberpunk. 

**Caractéristiques complètes** :

#### Menu principal
- **Choix du mode de jeu** :
  - 🎮 **Mode Deux Joueurs (1v1)** : Affrontez un autre joueur humain
  - 🤖 **Mode Solo (vs Bot)** : Affrontez un adversaire contrôlé par l'IA
- Style dark/neon avec effets glow

#### Sélection d'équipe
- **Saisie du nom** : Chaque joueur peut entrer son nom personnalisé
- **Sélection interactive** : Cartes de monstres avec toutes les statistiques
- **Affichage détaillé** : Type, PV, ATK, DEF, SPD pour chaque monstre
- **Gestion d'équipe** : Ajouter/retirer des monstres avant de confirmer

#### Vue de combat
- **Affichage complet des informations** :
  - Cartes de monstres avec barres de vie colorées
  - Statistiques complètes (ATK, DEF, SPD)
  - États d'altération (Paralysé, Brûlé, Empoisonné)
  - Indicateur ACTIF pour le monstre en combat
  - État du terrain (Normal/Inondé)
- **Actions disponibles** :
  - ⚔️ **Attaquer** : Attaque normale ou spéciales avec sélection
  - 💊 **Objet** : Utiliser un objet sur un monstre cible
  - 🔄 **Changer** : Remplacer le monstre actif
  - 🏠 **Menu** : Retour au menu principal
- **Gestion des tours** :
  - Boutons désactivés automatiquement quand ce n'est pas votre tour
  - Indication claire du joueur actif
  - Mode solo avec bot automatique
- **Log de combat** :
  - Messages de toutes les actions en temps réel
  - État du combat avant/après chaque tour
  - Barres de vie textuelles
  - Informations complètes sur les monstres

#### Style dark/neon
- **Palette de couleurs toxiques** :
  - Fonds sombres (#0A0E0D, #1A1F1E)
  - Accents néon verts (#39FF14, #00FF41)
  - Cyan néon (#00FFFF) pour les éléments spéciaux
  - Violet néon (#8B00FF, #B026FF) pour les actions
  - Rouge danger (#FF3838) pour les alertes
- **Effets visuels** :
  - Glow néon sur les éléments interactifs
  - Bordures néon colorées
  - Transitions au survol
  - Barres de vie colorées selon les PV restants

#### Parité avec la console
- **Même fonctionnement** : Toutes les fonctionnalités de la console sont disponibles
- **Mêmes interactions** : Même logique de jeu, mêmes règles
- **Messages console conservés** : Toutes les actions s'affichent aussi dans la console

**Mode Solo (vs Bot)** :
- Le joueur sélectionne son équipe de 3 monstres
- Le bot sélectionne automatiquement son équipe de manière aléatoire
- Le bot prend des décisions intelligentes : attaques, objets, changements de monstre
- Le bot adapte ses stratégies selon la situation du combat

**Configuration requise** :
- JavaFX SDK (voir [README_JAVAFX.md](README_JAVAFX.md) pour les instructions détaillées)
- Java 11 ou supérieur

**Note** : L'interface graphique est optionnelle. Le jeu fonctionne parfaitement en mode console sans JavaFX.

---

## Résolution des problèmes courants

### Erreur "package javafx does not exist"
**Cause** : JavaFX n'est pas dans le classpath/module path  
**Solution** : 
- Vérifier que JavaFX SDK est bien installé
- Vérifier que le chemin vers `javafx-sdk/lib` est correct
- Utiliser `--module-path` et `--add-modules` lors de la compilation et du lancement

### Erreur "no suitable pipeline found" (Windows)
**Cause** : Les DLL natives JavaFX ne sont pas trouvées  
**Solution** :
- Ajouter le dossier `javafx-sdk/bin` au PATH
- Vérifier que les DLL sont présentes dans le dossier `bin` du SDK

### Erreur "ClassNotFoundException"
**Cause** : Le classpath est incorrect  
**Solution** :
- Vérifier que le dossier `out/production/MONSTRE_DE_POCHE` existe
- Vérifier que le dossier `resources` est dans le classpath
- Utiliser `;` comme séparateur sur Windows, `:` sur Linux/Mac

### L'interface graphique ne s'affiche pas
**Cause** : Problème avec JavaFX ou les DLL natives  
**Solution** :
- Vérifier que JavaFX est correctement installé
- Vérifier les messages d'erreur dans la console
- Essayer de lancer en mode console pour vérifier que le projet fonctionne

## Structure du projet

### Arborescence des packages

```
com.esiea.monstredepoche/
│
├── models/
│   ├── Monster.java (classe abstraite)
│   ├── monsters/
│   │   ├── ElectricMonster.java
│   │   ├── WaterMonster.java
│   │   ├── GroundMonster.java
│   │   ├── FireMonster.java
│   │   ├── PlantMonster.java
│   │   └── InsectMonster.java
│   │
│   ├── Attack.java
│   ├── Item.java
│   ├── items/
│   │   ├── Potion.java
│   │   └── Medicine.java
│   │
│   ├── Player.java
│   ├── BattleField.java
│   │
│   └── enums/
│       ├── MonsterType.java
│       ├── AttackType.java
│       ├── StatusCondition.java
│       └── TerrainStatus.java
│
├── interfaces/
│   ├── Attacker.java
│   ├── StatusInflictor.java
│   └── SpecialAbility.java
│
├── controllers/
│   ├── GameController.java
│   ├── BattleController.java
│   ├── TurnManager.java
│   └── Bot.java (IA pour le mode solo)
│
├── services/
│   ├── DamageCalculator.java
│   ├── StatusEffectManager.java
│   └── TypeAdvantageCalculator.java
│
├── loaders/
│   ├── MonsterLoader.java
│   ├── AttackLoader.java
│   └── DataParser.java
│
├── utils/
│   ├── RandomGenerator.java
│   └── Constants.java
│
├── gui/                    # Interface graphique JavaFX (optionnelle)
│   ├── MonsterGameApp.java
│   ├── MainMenuController.java
│   ├── TeamSelectionController.java
│   └── BattleViewController.java
│
└── Main.java
```

### Description des packages

#### `models/`
Contient toutes les entités du jeu.

**Monster.java** (classe abstraite)
- Attributs : name, type, hp, maxHp, attack, defense, speed
- Attributs : currentStatus, attacks (List<Attack>)
- Méthodes abstraites : useSpecialAbility(), getTypeAdvantage()
- Méthodes communes : takeDamage(), heal(), addAttack(), isAlive()

**monsters/** (classes concrètes)
- Chaque type de monstre implémente sa capacité spéciale
- ElectricMonster : paralysisChance, paralyze()
- WaterMonster : floodChance, fallChance, triggerFlood()
- GroundMonster : digChance, burrowUnderground()
- FireMonster : burnChance, inflictBurn()
- PlantMonster : healChance, removeStatus()
- InsectMonster : poisonCounter, inflictPoison()

**Attack.java**
- Attributs : name, type, power, nbUse, maxUses, failProbability
- Méthodes : use(), canUse(), calculateDamage()

**Item.java** (classe abstraite)
- Attributs : name, description
- Méthode abstraite : use(Monster target)

**items/**
- Potion : hpRestore, attackBoost, defenseBoost
- Medicine : curesStatus, driesTerrain

**Player.java**
- Attributs : name, monsters (List<Monster>), items (List<Item>)
- Attributs : activeMonster, availableMonsters
- Méthodes : switchMonster(), useItem(), hasLost()

**BattleField.java**
- Attributs : isFlooded, floodDuration, player1, player2
- Méthodes : applyTerrainEffects(), updateTerrain()

**enums/**
- MonsterType : FOUDRE, EAU, TERRE, FEU, PLANTE, INSECTE
- AttackType : NORMAL, ELECTRIC, WATER, GROUND, FIRE, NATURE
- StatusCondition : NONE, PARALYZED, BURNED, POISONED
- TerrainStatus : NORMAL, FLOODED

#### `interfaces/`

**Attacker.java**
```java
void attack(Monster target);
double calculateDamage(Monster target, Attack attack);
```

**StatusInflictor.java**
```java
boolean inflictStatus(Monster target);
StatusCondition getInflictedStatus();
```

**SpecialAbility.java**
```java
void activateAbility(BattleField field, Monster opponent);
```

#### `controllers/`

**GameController.java**
- Gère le flux global du jeu
- Méthodes : startGame(), setupPlayers(), endGame()

**BattleController.java**
- Gère la logique du combat
- Méthodes : initializeBattle(), processTurn(), checkWinner()

**TurnManager.java**
- Gère l'ordre des actions
- Méthodes : determineOrder(), executeActions(), applyStatusEffects()

**Bot.java**
- IA pour le mode solo
- Prend des décisions automatiques : attaques, objets, changements de monstre
- Méthodes : makeDecision(), createAttackAction(), createItemAction(), createSwitchAction()

#### `services/`

**DamageCalculator.java**
- Méthodes statiques pour calculer les dégâts
- calculateBareDamage(), calculateAttackDamage()
- Applique les formules du TP

**StatusEffectManager.java**
- Gère l'application et la résolution des états
- applyBurn(), applyPoison(), checkParalysis()
- removeStatus(), updateStatusDuration()

**TypeAdvantageCalculator.java**
- Calcule les avantages/désavantages de type
- getAdvantageMultiplier(MonsterType attacker, MonsterType defender)

#### `loaders/`

**MonsterLoader.java**
- Charge les monstres depuis le fichier texte
- parseMonsterFile(), createMonster()

**AttackLoader.java**
- Charge les attaques depuis le fichier texte
- parseAttackFile(), createAttack()

**DataParser.java**
- Utilitaire pour parser les fichiers
- parseField(), parseRange()

#### `utils/`

**RandomGenerator.java**
- Génère les nombres aléatoires
- randomInRange(min, max), randomDouble(min, max)
- randomChance(probability)

**Constants.java**
- Constantes du jeu
- BARE_DAMAGE_MULTIPLIER = 20
- ATTACK_DAMAGE_BASE = 11
- POISON_DAMAGE_RATIO = 0.1

#### `Main.java`
Point d'entrée du programme.

### Fichiers de données

```
resources/
├── monsters.txt
├── attacks.txt
├── styles/
│   └── pokemon-style.css    # Feuille de style pour l'interface JavaFX
└── README.txt
```

### Diagramme de classes simplifié

```
Monster (abstract)
  ↑
  ├── ElectricMonster
  ├── WaterMonster
  ├── GroundMonster
  ├── FireMonster
  ├── PlantMonster (extends NatureMonster)
  └── InsectMonster (extends NatureMonster)

Monster "1" --> "*" Attack
Player "1" --> "1..3" Monster
Player "1" --> "0..5" Item
BattleField "1" --> "2" Player

GameController --> BattleController
BattleController --> TurnManager
BattleController --> BattleField
```

### Concepts clés

**Polymorphisme**
- Monster est une classe abstraite avec des méthodes abstraites
- Chaque type de monstre implémente ses capacités spéciales

**Composition**
- Player contient des Monsters et des Items
- BattleField contient des Players

**Services**
- Séparation de la logique métier dans des services réutilisables

**Factory Pattern** (optionnel)
- MonsterFactory pour créer les monstres selon leur type
- AttackFactory pour créer les attaques

**Strategy Pattern** (optionnel)
- Différentes stratégies pour le bot IA

Cette structure respecte les principes SOLID et facilite l'extension du jeu.

## Comment jouer

### Démarrage

**En mode graphique (JavaFX)** :
1. Au lancement, choisissez votre mode de jeu :
   - **🎮 Deux Joueurs (1v1)** : Les deux joueurs sélectionnent leur équipe tour à tour
   - **🤖 Mode Solo (vs Bot)** : Vous sélectionnez votre équipe, le bot sélectionne la sienne automatiquement
2. **Entrez votre nom** : Un dialogue vous demande votre nom (ou utilisez le nom par défaut)
3. **Sélectionnez votre équipe** : 
   - Cliquez sur "Sélectionner" pour chaque monstre souhaité
   - Vous pouvez voir toutes les statistiques (Type, PV, ATK, DEF, SPD)
   - Vous pouvez retirer un monstre en cliquant sur "Retirer"
4. **Confirmez votre équipe** : Cliquez sur "Confirmer l'équipe" une fois 3 monstres sélectionnés
5. **En mode 2 joueurs** : Le deuxième joueur entre son nom et sélectionne son équipe
6. Le combat commence automatiquement avec l'interface graphique complète

**En mode console** :
1. Choisissez le mode de jeu :
   - **1. Mode Deux Joueurs (1v1)** : Les deux joueurs sélectionnent leur équipe tour à tour
   - **2. Mode Solo (vs Bot)** : Vous sélectionnez votre équipe, le bot sélectionne la sienne automatiquement
2. **En mode 2 joueurs** :
   - Chaque joueur entre son nom
   - Chaque joueur sélectionne 3 monstres parmi ceux disponibles
   - Chaque joueur reçoit 5 objets (potions et médicaments)
3. **En mode solo** :
   - Vous entrez votre nom
   - Vous sélectionnez 3 monstres parmi ceux disponibles
   - Le bot sélectionne automatiquement son équipe de 3 monstres
   - Vous recevez 5 objets (potions et médicaments)
4. Le combat commence automatiquement

### Tour de jeu

**En mode graphique** :
- Les boutons d'action sont **automatiquement activés/désactivés** selon le tour
- Cliquez sur l'action souhaitée :
  1. **⚔️ Attaquer** : 
     - Choisir entre attaque normale (mains nues) ou attaques spéciales
     - Voir la puissance et les utilisations restantes de chaque attaque
  2. **💊 Objet** : 
     - Sélectionner un objet dans la liste
     - Choisir le monstre cible (actif ou en réserve)
  3. **🔄 Changer** : 
     - Sélectionner un monstre de remplacement parmi ceux disponibles
     - Voir les PV et le type de chaque monstre
- Toutes les actions sont enregistrées dans le **log de combat** en bas de l'écran
- L'état du combat est mis à jour en temps réel

**En mode console** :
À chaque tour, chaque joueur peut :
1. **Attaquer** : Choisir une attaque parmi celles disponibles pour le monstre actif
2. **Utiliser un objet** : Soigner un monstre ou modifier ses statistiques
3. **Changer de monstre** : Remplacer le monstre actif par un autre de votre équipe

### Ordre d'exécution
Les actions sont résolues dans cet ordre :
1. Changements de monstres
2. Utilisation d'objets
3. Attaques (le monstre le plus rapide attaque en premier)

### Conditions de victoire
Vous gagnez lorsque tous les monstres adverses sont KO (points de vie à 0).

## Types de monstres

### ⚡ Foudre (Electric)
- **Fort contre** : Eau
- **Faible contre** : Terre
- **Capacité** : Peut paralyser l'adversaire (25% de chance de rater une attaque)

### 💧 Eau (Water)
- **Fort contre** : Feu
- **Faible contre** : Foudre
- **Capacité** : Inonde le terrain, faisant glisser l'adversaire

### 🪨 Terre (Ground)
- **Fort contre** : Foudre
- **Faible contre** : Nature
- **Capacité** : S'enfouit sous terre, doublant sa défense

### 🔥 Feu (Fire)
- **Fort contre** : Nature
- **Faible contre** : Eau
- **Capacité** : Brûle l'adversaire (subit 10% de son attaque par tour)

### 🌿 Plante (Plant)
- Sous-type de Nature
- **Capacité** : 20% de chance de se soigner des altérations d'état
- Récupère des PV sur terrain inondé

### 🐛 Insecte (Insect)
- Sous-type de Nature
- **Capacité** : Empoisonne l'adversaire une attaque sur trois
- Récupère des PV sur terrain inondé

## États d'altération

- **Paralysie** : 25% de chance de rater son attaque, se dissipe progressivement
- **Brûlure** : Subit 10% de son attaque en début de tour, guéri par inondation
- **Empoisonnement** : Subit 10% de son attaque en début de tour, guéri par inondation

Un monstre ne peut avoir qu'un seul état à la fois.

## Système de combat

### Calcul des dégâts - Attaque basique
```
Dégâts = 20 × (attaque / défense adverse) × coef × avantage
```
- `coef` : nombre aléatoire entre 0.85 et 1.0
- `avantage` : 2.0 si fort, 0.5 si faible, 1.0 sinon

### Calcul des dégâts - Attaque spéciale
```
Dégâts = ((11 × attaque × puissance) / (25 × défense adverse) + 2) × avantage × coef
```

## Objets

### Potions
- Restaurent les points de vie
- Augmentent temporairement l'attaque ou la défense

### Médicaments
- Soignent les altérations d'état
- Assèchent le terrain inondé

## Format des fichiers de données

### monsters.txt
```
Monster
    Name Pikachu
    Type Electric
    HP 110 141
    Speed 110 141
    Attack 75 106
    Defense 50 82
    Paralysis 0.2
EndMonster
```

**Notes importantes :**
- Les champs doivent être indentés avec 4 espaces (même format que `attacks.txt`)
- Les champs peuvent être entrés dans n'importe quel ordre
- Les valeurs avec deux nombres (ex: `HP 110 141`) représentent les bornes min/max
- **Les types de monstres sont en anglais** : `Electric`, `Water`, `Ground`, `Fire`, `Plant`, `Insect`
- Champs optionnels selon le type : `Paralysis`, `Flood`, `Fall`, `Dig`, `Burn`, `Heal`

### attacks.txt
```
Attack
    Name Eclair
    Type Electric
    Power 40
    NbUse 10
    Fail 0.07
EndAttack
```

**Notes importantes :**
- Les champs doivent être indentés avec 4 espaces
- Les champs peuvent être entrés dans n'importe quel ordre
- Les types d'attaques disponibles : `Electric`, `Water`, `Ground`, `Fire`, `Nature`, `Normal`

## Commandes en jeu

Pendant le combat, suivez les instructions à l'écran :
- Entrez le numéro correspondant à l'action souhaitée
- Pour les attaques : entrez le numéro de l'attaque (1-4)
- Pour les objets : sélectionnez l'objet puis la cible
- Pour changer de monstre : sélectionnez le monstre de remplacement

## Conseils stratégiques

1. **Exploitez les avantages de type** : Double les dégâts infligés
2. **Gérez vos attaques** : Nombre d'utilisations limité
3. **Surveillez la vitesse** : Le monstre le plus rapide attaque en premier
4. **Utilisez le terrain** : L'inondation peut être un avantage ou un désavantage
5. **Conservez des objets** : Gardez des potions pour les situations critiques
6. **Changez de monstre** : Si votre monstre est désavantagé par le type adverse

## Extensions possibles

Le jeu a été conçu pour être facilement extensible :
- Ajouter de nouveaux types de monstres
- Créer de nouvelles attaques
- Implémenter des objets personnalisés
- ✅ Menu de sélection d'équipe (implémenté)
- ✅ Interface graphique JavaFX (implémentée)
- ✅ Mode Solo avec IA (implémenté)
- ✅ Style dark/neon avec effets glow (implémenté)
- Améliorer l'IA du bot (stratégies plus avancées)
- Ajouter un mode multijoueur en ligne
- Améliorer l'interface graphique (animations, effets visuels)

---

## Récapitulatif des commandes

### Windows (PowerShell) - Commandes à copier-coller

```powershell
# Se placer dans le projet
cd "chemin\vers\MONSTRE_DE_POCHE"

# Compilation avec JavaFX (pour GUI)
$javafxPath = "javafx-sdk-17.0.17\lib"
$javaFiles = Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac --module-path "$javafxPath" --add-modules javafx.controls,javafx.fxml,javafx.graphics -d out\production\MONSTRE_DE_POCHE -cp "src;resources" -encoding UTF-8 $javaFiles

# Lancement GUI
$javafxPath = "javafx-sdk-17.0.17\lib"
$binPath = "javafx-sdk-17.0.17\bin"
$env:PATH = "$binPath;$env:PATH"
java --module-path "$javafxPath" --add-modules javafx.controls,javafx.fxml,javafx.graphics -cp "out\production\MONSTRE_DE_POCHE;resources" com.esiea.monstredepoche.Main gui

# Compilation sans JavaFX (pour Console uniquement)
$javaFiles = Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -d out\production\MONSTRE_DE_POCHE -cp "src;resources" -encoding UTF-8 $javaFiles

# Lancement Console
java -cp "out\production\MONSTRE_DE_POCHE;resources" com.esiea.monstredepoche.Main
```

### Linux (Bash) - Commandes à copier-coller

```bash
# Se placer dans le projet
cd /chemin/vers/MONSTRE_DE_POCHE

# Compilation avec JavaFX (pour GUI)
# MODIFIER LE CHEMIN JAVAFX CI-DESSOUS
JAVAFX_PATH="/chemin/vers/javafx-sdk/lib"
javac --module-path "$JAVAFX_PATH" --add-modules javafx.controls,javafx.fxml,javafx.graphics \
  -d out/production/MONSTRE_DE_POCHE -cp "src:resources" -encoding UTF-8 \
  $(find src -name "*.java")

# Lancement GUI
java --module-path "$JAVAFX_PATH" --add-modules javafx.controls,javafx.fxml,javafx.graphics \
  -cp "out/production/MONSTRE_DE_POCHE:resources" com.esiea.monstredepoche.Main gui

# Compilation sans JavaFX (pour Console uniquement)
javac -d out/production/MONSTRE_DE_POCHE -cp "src:resources" -encoding UTF-8 \
  $(find src -name "*.java")

# Lancement Console
java -cp "out/production/MONSTRE_DE_POCHE:resources" com.esiea.monstredepoche.Main
```

### Scripts disponibles

- **Windows** : `launch-gui.ps1`, `launch-console.ps1`
- **Linux** : `launch-gui.sh`, `launch-console.sh` (à rendre exécutables avec `chmod +x`)

---

## Auteurs
Projet réalisé dans le cadre du cours INF3132 - Programmation Orientée Objet

## Licence
Projet éducatif - Usage libre pour l'apprentissage