package com.esiea.monstredepoche.controllers;

import com.esiea.monstredepoche.models.*;
import com.esiea.monstredepoche.models.enums.AttackType;
import com.esiea.monstredepoche.models.enums.MonsterType;
import com.esiea.monstredepoche.utils.RandomGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Bot qui prend des décisions aléatoires pour jouer.
 * Utilisé pour le mode solo où un joueur humain affronte le bot.
 */
public class Bot {
    private Player botPlayer;
    private String name;
    
    /**
     * Constructeur du bot
     * @param name Le nom du bot
     */
    public Bot(String name) {
        this.name = name;
    }
    
    /**
     * Définit le joueur contrôlé par le bot
     * @param player Le joueur bot
     */
    public void setPlayer(Player player) {
        this.botPlayer = player;
    }
    
    /**
     * Retourne le nom du bot
     * @return Le nom du bot
     */
    public String getName() {
        return name;
    }
    
    /**
     * Retourne le joueur contrôlé par le bot
     * @return Le joueur bot
     */
    public Player getPlayer() {
        return botPlayer;
    }
    
    /**
     * Prend une décision aléatoire pour le tour du bot.
     * Le bot choisit aléatoirement entre attaquer, utiliser un objet ou changer de monstre.
     * 
     * @return L'action choisie par le bot
     */
    public TurnManager.Action makeDecision() {
        if (botPlayer == null || botPlayer.getActiveMonster() == null) {
            return null;
        }
        
        Monster activeMonster = botPlayer.getActiveMonster();
        if (!activeMonster.isAlive()) {
            // Si le monstre actif est KO, essayer de changer
            List<Monster> available = botPlayer.getAvailableMonsters();
            if (!available.isEmpty()) {
                return createSwitchAction(available);
            }
            return null;
        }
        
        // Choisir une action aléatoire (1: Attaquer, 2: Objet, 3: Changer)
        int actionChoice = RandomGenerator.randomInRange(1, 3);
        
        switch (actionChoice) {
            case 1:
                // Attaquer
                return createAttackAction(activeMonster);
            case 2:
                // Utiliser un objet (seulement si disponible)
                if (!botPlayer.getItems().isEmpty()) {
                    return createItemAction();
                } else {
                    // Pas d'objets, attaquer à la place
                    return createAttackAction(activeMonster);
                }
            case 3:
                // Changer de monstre (seulement si possible)
                List<Monster> available = botPlayer.getAvailableMonsters();
                if (!available.isEmpty()) {
                    return createSwitchAction(available);
                } else {
                    // Pas de monstre disponible, attaquer à la place
                    return createAttackAction(activeMonster);
                }
            default:
                // Par défaut, attaquer
                return createAttackAction(activeMonster);
        }
    }
    
    /**
     * Crée une action d'attaque aléatoire
     * @param monster Le monstre qui attaque
     * @return L'action d'attaque
     */
    private TurnManager.Action createAttackAction(Monster monster) {
        TurnManager.Action action = new TurnManager.Action(
            TurnManager.Action.ActionType.ATTACK, botPlayer
        );
        
        // Récupérer les attaques spéciales disponibles
        List<Attack> specialAttacks = new ArrayList<>();
        AttackType monsterAttackType = convertMonsterTypeToAttackType(monster.getType());
        
        for (Attack attack : monster.getAttacks()) {
            if (attack.getType() == monsterAttackType && attack.canUse()) {
                specialAttacks.add(attack);
            }
        }
        
        // Choisir aléatoirement entre attaque normale et spéciale (si disponible)
        if (!specialAttacks.isEmpty() && RandomGenerator.randomChance(0.7)) {
            // 70% de chance d'utiliser une attaque spéciale si disponible
            Attack selectedAttack = specialAttacks.get(RandomGenerator.randomInRange(0, specialAttacks.size() - 1));
            action.attackIndex = monster.getAttacks().indexOf(selectedAttack);
        } else {
            // Attaque normale (mains nues)
            action.attackIndex = -1;
        }
        
        System.out.println("🤖 " + name + " choisit d'attaquer !");
        return action;
    }
    
    /**
     * Crée une action d'utilisation d'objet aléatoire
     * @return L'action d'objet
     */
    private TurnManager.Action createItemAction() {
        TurnManager.Action action = new TurnManager.Action(
            TurnManager.Action.ActionType.USE_ITEM, botPlayer
        );
        
        List<Item> items = botPlayer.getItems();
        if (items.isEmpty()) {
            return null;
        }
        
        // Choisir un objet aléatoire
        int itemIndex = RandomGenerator.randomInRange(0, items.size() - 1);
        action.targetIndex = itemIndex;
        
        System.out.println("🤖 " + name + " choisit d'utiliser un objet : " + items.get(itemIndex).getName());
        return action;
    }
    
    /**
     * Crée une action de changement de monstre aléatoire
     * @param available La liste des monstres disponibles
     * @return L'action de changement
     */
    private TurnManager.Action createSwitchAction(List<Monster> available) {
        TurnManager.Action action = new TurnManager.Action(
            TurnManager.Action.ActionType.SWITCH_MONSTER, botPlayer
        );
        
        if (available.isEmpty()) {
            return null;
        }
        
        // Choisir un monstre aléatoire parmi ceux disponibles
        Monster selected = available.get(RandomGenerator.randomInRange(0, available.size() - 1));
        action.targetIndex = botPlayer.getMonsters().indexOf(selected);
        
        System.out.println("🤖 " + name + " choisit de changer de monstre : " + selected.getName());
        return action;
    }
    
    /**
     * Convertit un type de monstre en type d'attaque
     * @param monsterType Le type de monstre
     * @return Le type d'attaque correspondant
     */
    private AttackType convertMonsterTypeToAttackType(MonsterType monsterType) {
        switch (monsterType) {
            case ELECTRIC:
                return AttackType.ELECTRIC;
            case WATER:
                return AttackType.WATER;
            case GROUND:
                return AttackType.GROUND;
            case FIRE:
                return AttackType.FIRE;
            case PLANT:
            case INSECT:
                return AttackType.NATURE;
            default:
                return AttackType.NORMAL;
        }
    }
}
