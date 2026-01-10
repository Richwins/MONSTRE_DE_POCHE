# Monstre de Poche - Jeu de Combat en Tour par Tour

### Etudiant : SESSOU Winsou Richard 

## Description
Jeu de combat inspiré de Pokémon où deux joueurs s'affrontent avec leurs équipes de monstres. Chaque monstre possède un type élémentaire avec des forces et faiblesses, des attaques spéciales et des capacités uniques.

## Documentation et Ressources

### Vidéo de Démonstration
Une vidéo de démonstration est disponible dans le projet : [`demo_video.mp4`](demo_video.mp4)

Cette vidéo présente :
- Le menu principal et la sélection des modes de jeu
- La sélection d'équipe avec l'interface graphique
- Le système de combat avec animations et effets visuels
- Les différentes fonctionnalités de l'interface utilisateur
- Les interactions avec le bot en mode solo

### Diagramme UML
Le diagramme de classes UML du projet est disponible : [`diagram_project.png`](diagram_project.png)

Ce diagramme illustre :
- L'architecture complète du projet
- Les relations entre les différentes classes
- L'organisation des packages (models, controllers, gui, services, etc.)
- Les interactions entre les composants de l'application
- La structure de l'interface graphique JavaFX

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

**Lancement GUI (direct) :**
```powershell
$javafxPath = "javafx-sdk-17.0.17\lib"
$binPath = "javafx-sdk-17.0.17\bin"
$env:PATH = "$binPath;$env:PATH"
java --module-path "$javafxPath" --add-modules javafx.controls,javafx.fxml,javafx.graphics -cp "out\production\MONSTRE_DE_POCHE;resources" com.esiea.monstredepoche.Main gui
```

**Lancement avec menu interactif (Console ou GUI) :**
```powershell
# Pour pouvoir choisir le GUI depuis le menu, lancez avec les arguments JavaFX :
$javafxPath = "javafx-sdk-17.0.17\lib"
$binPath = "javafx-sdk-17.0.17\bin"
$env:PATH = "$binPath;$env:PATH"
java --module-path "$javafxPath" --add-modules javafx.controls,javafx.fxml,javafx.graphics -cp "out\production\MONSTRE_DE_POCHE;resources" com.esiea.monstredepoche.Main
```
*Note : Si vous choisissez l'option 2 (GUI) dans le menu interactif, le mode graphique se lancera automatiquement.*

### Linux (Bash)

**Compilation :**
```bash
cd /chemin/vers/MONSTRE_DE_POCHE
JAVAFX_PATH="javafx-sdk-17.0.17/lib"
javac --module-path "$JAVAFX_PATH" --add-modules javafx.controls,javafx.fxml,javafx.graphics -d out/production/MONSTRE_DE_POCHE -cp "src:resources" -encoding UTF-8 $(find src -name "*.java")
```

**Lancement GUI (direct) :**
```bash
JAVAFX_PATH="javafx-sdk-17.0.17/lib"
java --module-path "$JAVAFX_PATH" --add-modules javafx.controls,javafx.fxml,javafx.graphics -cp "out/production/MONSTRE_DE_POCHE:resources" com.esiea.monstredepoche.Main gui
```

**Lancement Console (avec choix interactif) :**
```bash
java -cp "out/production/MONSTRE_DE_POCHE:resources" com.esiea.monstredepoche.Main
```

**Lancement avec menu interactif (Console ou GUI) :**
```bash
# Pour pouvoir choisir le GUI depuis le menu, lancez avec les arguments JavaFX :
JAVAFX_PATH="javafx-sdk-17.0.17/lib"
java --module-path "$JAVAFX_PATH" --add-modules javafx.controls,javafx.fxml,javafx.graphics -cp "out/production/MONSTRE_DE_POCHE:resources" com.esiea.monstredepoche.Main
```
*Note : Si vous choisissez l'option 2 (GUI) dans le menu interactif, le mode graphique se lancera automatiquement.*

## Fonctionnalités du jeu

### Modes de jeu
- **Mode Deux Joueurs (1v1)** : Affrontement entre deux joueurs humains
- **Mode Solo (vs Bot)** : Affrontement contre une IA qui prend des décisions automatiques

### Système de combat
- **Combat au tour par tour** : Chaque joueur choisit une action par tour
- **Ordre d'exécution** : Changements de monstres → Objets → Attaques (selon la vitesse)
- **Système de vitesse** : Le monstre le plus rapide attaque en premier
- **Calcul de dégâts** : Formules complexes avec coefficients aléatoires et avantages de type
- **Attaques spéciales** : Chaque monstre peut utiliser des attaques de son type élémentaire
- **Attaques à mains nues** : Possibilité d'attaquer sans utiliser d'attaque spéciale

### Gestion des équipes
- **Sélection d'équipe** : Chaque joueur choisit 3 monstres parmi ceux disponibles
- **Changement de monstre** : Possibilité de remplacer le monstre actif pendant le combat
- **Gestion des réserves** : Suivi des monstres en réserve et de leur état

### Système de types élémentaires
- **6 types de monstres** : Foudre, Eau, Terre, Feu, Plante, Insecte
- **Avantages et faiblesses** : Système de type avec multiplicateurs de dégâts (x2.0 ou x0.5)
- **Correspondances** : Foudre > Eau, Eau > Feu, Feu > Nature, etc.

### Capacités spéciales par type
- **Foudre** : Peut paralyser l'adversaire (25% de chance de rater une attaque)
- **Eau** : Peut inonder le terrain (effets sur les monstres Nature et guérison des brûlures/empoisonnements)
- **Terre** : Peut s'enfouir sous terre (double la défense temporairement)
- **Feu** : Peut brûler l'adversaire (dégâts progressifs)
- **Plante** : Peut se soigner des altérations d'état
- **Insecte** : Peut empoisonner l'adversaire (dégâts progressifs)

### Système d'altérations d'état
- **Paralysie** : 25% de chance de rater une attaque, se dissipe progressivement
- **Brûlure** : Dégâts de 10% de l'attaque du monstre à chaque tour
- **Empoisonnement** : Dégâts de 10% de l'attaque du monstre à chaque tour
- **Un seul état à la fois** : Un monstre ne peut avoir qu'une seule altération d'état

### Système de terrain
- **Terrain normal** : État par défaut sans effets spéciaux
- **Terrain inondé** : Peut être activé par les monstres Eau
  - Soigne les monstres Nature (1/20 des PV max) après utilisation d'attaque spéciale
  - Guérit automatiquement les brûlures et empoisonnements
  - Peut faire glisser les monstres non-Eau (annule leur attaque et inflige des dégâts)

### Système d'objets
- **Potions** : Restaurent les points de vie et/ou augmentent l'attaque ou la défense
- **Médicaments** : Soignent les altérations d'état (paralysie, brûlure, empoisonnement)
- **Utilisation stratégique** : Chaque joueur reçoit 5 objets au début du combat

### Système d'attaques
- **Attaques spéciales** : Chaque monstre peut avoir jusqu'à 4 attaques
- **Nombre d'utilisations limité** : Chaque attaque a un nombre d'utilisations maximum
- **Probabilité d'échec** : Certaines attaques peuvent échouer selon leur probabilité
- **Types d'attaques** : Normal, Electric, Water, Ground, Fire, Nature

### Interface utilisateur
- **Interface console** : Mode texte complet avec toutes les fonctionnalités
- **Interface graphique JavaFX** : Interface moderne avec :
  - Menu principal interactif
  - Sélection d'équipe avec cartes visuelles
  - Vue de combat en temps réel avec barres de vie
  - Affichage des statistiques complètes (HP, ATK, DEF, SPD)
  - Indicateurs d'état visuels
  - Log de combat détaillé
  - Style dark/neon avec effets visuels

### Intelligence artificielle (Bot)
- **Sélection automatique d'équipe** : Le bot choisit aléatoirement 3 monstres
- **Décisions intelligentes** : Le bot choisit entre attaquer, utiliser un objet ou changer de monstre
- **Préférence pour les attaques spéciales** : 70% de chance d'utiliser une attaque spéciale si disponible
- **Gestion des situations critiques** : Le bot adapte ses choix selon l'état du combat

### Chargement de données
- **Fichiers de configuration** : Chargement des monstres et attaques depuis `monsters.txt` et `attacks.txt`
- **Plages de valeurs** : Statistiques générées aléatoirement dans des plages définies
- **Extensibilité** : Facile d'ajouter de nouveaux monstres et attaques via les fichiers texte

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

## Auteurs
Projet réalisé dans le cadre du cours INF3132 - Programmation Orientée Objet

## Licence
Projet éducatif - Usage libre pour l'apprentissage
