# Monstre de Poche - Jeu de Combat en Tour par Tour

### Etudiant : SESSOU Winsou Richard 

## Description
Jeu de combat inspiré de Pokémon où deux joueurs s'affrontent avec leurs équipes de monstres. Chaque monstre possède un type élémentaire avec des forces et faiblesses, des attaques spéciales et des capacités uniques.

## Prérequis
- **Java 11 ou supérieur** (JDK)
- **JavaFX SDK 17** (inclus dans le projet : `javafx-sdk-17.0.17/`)

## Installation
1. Extraire l'archive du projet
2. Vérifier que `resources/monsters.txt` et `resources/attacks.txt` sont présents
3. Le SDK JavaFX est déjà inclus dans le projet (pas besoin d'installation supplémentaire)

## Compilation et Lancement

### Windows (PowerShell)

**Compilation :**
```powershell
cd "chemin\vers\MONSTRE_DE_POCHE"
$javafxPath = "javafx-sdk-17.0.17\lib"
$javaFiles = Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac --module-path "$javafxPath" --add-modules javafx.controls,javafx.fxml,javafx.graphics -d out\production\MONSTRE_DE_POCHE -cp "src;resources" -encoding UTF-8 $javaFiles
```

**Lancement GUI :**
```powershell
$javafxPath = "javafx-sdk-17.0.17\lib"
$binPath = "javafx-sdk-17.0.17\bin"
$env:PATH = "$binPath;$env:PATH"
java --module-path "$javafxPath" --add-modules javafx.controls,javafx.fxml,javafx.graphics -cp "out\production\MONSTRE_DE_POCHE;resources" com.esiea.monstredepoche.Main gui
```

**Lancement Console :**
```powershell
java -cp "out\production\MONSTRE_DE_POCHE;resources" com.esiea.monstredepoche.Main
```

### Linux (Bash)

**Compilation :**
```bash
cd /chemin/vers/MONSTRE_DE_POCHE
JAVAFX_PATH="javafx-sdk-17.0.17/lib"
javac --module-path "$JAVAFX_PATH" --add-modules javafx.controls,javafx.fxml,javafx.graphics -d out/production/MONSTRE_DE_POCHE -cp "src:resources" -encoding UTF-8 $(find src -name "*.java")
```

**Lancement GUI :**
```bash
JAVAFX_PATH="javafx-sdk-17.0.17/lib"
java --module-path "$JAVAFX_PATH" --add-modules javafx.controls,javafx.fxml,javafx.graphics -cp "out/production/MONSTRE_DE_POCHE:resources" com.esiea.monstredepoche.Main gui
```

**Lancement Console :**
```bash
java -cp "out/production/MONSTRE_DE_POCHE:resources" com.esiea.monstredepoche.Main
```

## Modes de jeu

### Mode Deux Joueurs (1v1)
Les deux joueurs sélectionnent leur équipe de 3 monstres et s'affrontent.

### Mode Solo (vs Bot)
Un joueur affronte un bot contrôlé par l'IA. Le bot sélectionne automatiquement son équipe et prend des décisions intelligentes.

## Interface graphique JavaFX

L'interface graphique offre :
- **Menu principal** : Choix du mode de jeu (2 joueurs ou solo)
- **Sélection d'équipe** : Interface interactive avec cartes de monstres et statistiques
- **Vue de combat** : Affichage complet avec barres de vie, états, terrain, log de combat
- **Actions** : Attaquer, utiliser objet, changer de monstre
- **Style dark/neon** : Palette de couleurs cyberpunk avec effets glow

## Comment jouer

### Démarrage
1. Choisir le mode de jeu (2 joueurs ou solo)
2. Entrer le nom du joueur
3. Sélectionner 3 monstres parmi ceux disponibles
4. Le combat commence automatiquement

### Tour de jeu
À chaque tour, vous pouvez :
- **Attaquer** : Choisir une attaque normale ou spéciale
- **Utiliser un objet** : Soigner ou modifier les statistiques d'un monstre
- **Changer de monstre** : Remplacer le monstre actif

### Ordre d'exécution
1. Changements de monstres
2. Utilisation d'objets
3. Attaques (le plus rapide attaque en premier)

### Conditions de victoire
Gagner lorsque tous les monstres adverses sont KO (points de vie à 0).

## Types de monstres

- **⚡ Foudre (Electric)** : Fort contre Eau, faible contre Terre. Peut paralyser l'adversaire.
- **💧 Eau (Water)** : Fort contre Feu, faible contre Foudre. Inonde le terrain.
- **🪨 Terre (Ground)** : Fort contre Foudre, faible contre Nature. S'enfouit sous terre.
- **🔥 Feu (Fire)** : Fort contre Nature, faible contre Eau. Brûle l'adversaire.
- **🌿 Plante (Plant)** : Sous-type Nature. Se soigne des altérations d'état.
- **🐛 Insecte (Insect)** : Sous-type Nature. Empoisonne l'adversaire.

## États d'altération

- **Paralysie** : 25% de chance de rater son attaque, se dissipe progressivement
- **Brûlure** : Subit 10% de son attaque en début de tour
- **Empoisonnement** : Subit 10% de son attaque en début de tour

Un monstre ne peut avoir qu'un seul état à la fois.

## Système de combat

### Attaque basique
```
Dégâts = 20 × (attaque / défense adverse) × coef × avantage
```
- `coef` : aléatoire entre 0.85 et 1.0
- `avantage` : 2.0 si fort, 0.5 si faible, 1.0 sinon

### Attaque spéciale
```
Dégâts = ((11 × attaque × puissance) / (25 × défense adverse) + 2) × avantage × coef
```

## Structure du projet

```
com.esiea.monstredepoche/
├── models/          # Entités du jeu (Monster, Player, Attack, Item)
├── controllers/      # Logique de jeu (GameController, BattleController, Bot)
├── services/        # Services (DamageCalculator, StatusEffectManager)
├── loaders/         # Chargement des données (MonsterLoader, AttackLoader)
├── utils/           # Utilitaires (RandomGenerator, Constants)
├── gui/             # Interface graphique JavaFX
└── Main.java        # Point d'entrée
```

## Résolution des problèmes

**Erreur "package javafx does not exist"**
- Vérifier que JavaFX SDK est présent dans `javafx-sdk-17.0.17/lib`
- Utiliser `--module-path` et `--add-modules` lors de la compilation

**Erreur "no suitable pipeline found" (Windows)**
- Ajouter `javafx-sdk-17.0.17/bin` au PATH avant de lancer

**Erreur "ClassNotFoundException"**
- Vérifier que `out/production/MONSTRE_DE_POCHE` existe
- Vérifier que `resources` est dans le classpath

## Auteurs
Projet réalisé dans le cadre du cours INF3132 - Programmation Orientée Objet

## Licence
Projet éducatif - Usage libre pour l'apprentissage
