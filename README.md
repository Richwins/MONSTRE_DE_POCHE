# Monstre de Poche - Jeu de Combat en Tour par Tour

### Etudiant : SESSOU Winsou Richard 

## Description
Jeu de combat inspiré de Pokémon où deux joueurs s'affrontent avec leurs équipes de monstres. Chaque monstre possède un type élémentaire avec des forces et faiblesses, des attaques spéciales et des capacités uniques.


## Installation

1. Extraire l'archive `monstre-de-poche.zip`
2. Ouvrir le projet dans votre IDE
3. Vérifier que les fichiers `monsters.txt` et `attacks.txt` sont présents dans le dossier `resources/`
4. Compiler le projet
5. Exécuter la classe `Main.java`

## Lancement du projet

Le jeu peut être lancé en **interface console** (par défaut) ou en **interface graphique JavaFX**.

### Choix du mode d'affichage

Au lancement, le programme vous demande de choisir :
- **1** : Interface console (par défaut)
- **2** : Interface graphique JavaFX

Vous pouvez aussi lancer directement avec un argument :
- `java com.esiea.monstredepoche.Main` → Interface console
- `java com.esiea.monstredepoche.Main gui` → Interface graphique

### Option 2 : En ligne de commande (PowerShell)

```powershell
# 1. Se placer à la racine du projet
cd "chemin\vers\MONSTRE_DE_POCHE"

# 2. Compiler le projet (si nécessaire)
javac -d out\production\MONSTRE_DE_POCHE -cp src -encoding UTF-8 src\com\esiea\monstredepoche\models\monsters\*.java src\com\esiea\monstredepoche\*.java

# 3. Lancer le jeu
# Interface console (par défaut)
java -cp "out\production\MONSTRE_DE_POCHE;resources" com.esiea.monstredepoche.Main

# Interface graphique (nécessite JavaFX)
java --module-path /chemin/vers/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml -cp "out\production\MONSTRE_DE_POCHE;resources" com.esiea.monstredepoche.Main gui
```

### Option 3 : En ligne de commande (Bash/Linux/Mac)

```bash
# 1. Se placer à la racine du projet
cd chemin/vers/MONSTRE_DE_POCHE

# 2. Compiler le projet
javac -d out/production/MONSTRE_DE_POCHE -cp src -encoding UTF-8 $(find src -name "*.java")

# 3. Lancer le jeu
# Interface console (par défaut)
java -cp "out/production/MONSTRE_DE_POCHE:resources" com.esiea.monstredepoche.Main

# Interface graphique (nécessite JavaFX)
java --module-path /chemin/vers/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml -cp "out/production/MONSTRE_DE_POCHE:resources" com.esiea.monstredepoche.Main gui
```

### Interface graphique JavaFX

Le projet inclut une interface graphique moderne avec un style inspiré de Pokémon. 

**Caractéristiques** :
- Menu principal avec navigation
- Sélection d'équipe interactive
- Vue de combat avec boutons d'action
- Palette de couleurs Pokémon
- **Les affichages console sont conservés** : toutes les actions s'affichent aussi dans la console

**Configuration requise** :
- JavaFX SDK (voir [README_JAVAFX.md](README_JAVAFX.md) pour les instructions détaillées)
- Java 11 ou supérieur

**Note** : L'interface graphique est optionnelle. Le jeu fonctionne parfaitement en mode console sans JavaFX.

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
│   └── TurnManager.java
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
Au lancement du jeu :
1. Chaque joueur entre son nom
2. Chaque joueur sélectionne 3 monstres parmi ceux disponibles
3. Chaque joueur reçoit 5 objets (potions et médicaments)
4. Le combat commence automatiquement

### Tour de jeu
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
- Développer une IA pour jouer en solo
- Ajouter un mode multijoueur en ligne
- Améliorer l'interface graphique (animations, effets visuels)

## Auteurs
Projet réalisé dans le cadre du cours INF3132 - Programmation Orientée Objet

## Licence
Projet éducatif - Usage libre pour l'apprentissage