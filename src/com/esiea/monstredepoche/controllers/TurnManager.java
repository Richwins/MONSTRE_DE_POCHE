package com.esiea.monstredepoche.controllers;

import com.esiea.monstredepoche.models.BattleField;
import com.esiea.monstredepoche.models.Monster;
import com.esiea.monstredepoche.models.Player;
import com.esiea.monstredepoche.services.StatusEffectManager;

import java.util.ArrayList;
import java.util.List;

public class TurnManager {
    
    public static class Action {
        public enum ActionType {
            SWITCH_MONSTER,
            USE_ITEM,
            ATTACK
        }
        
        public ActionType type;
        public Player player;
        public int targetIndex;
        public Monster targetMonster;
        public int attackIndex;
        
        public Action(ActionType type, Player player) {
            this.type = type;
            this.player = player;
        }
    }
    
    private BattleField field;
    private List<Action> actions;
    
    public TurnManager(BattleField field) {
        this.field = field;
        this.actions = new ArrayList<>();
    }
    
    public void addAction(Action action) {
        actions.add(action);
    }
    
    public void executeActions() {
        // 1. Changements de monstres
        for (Action action : actions) {
            if (action.type == Action.ActionType.SWITCH_MONSTER) {
                action.player.switchMonster(action.targetIndex);
            }
        }
        
        // 2. Utilisation d'objets
        for (Action action : actions) {
            if (action.type == Action.ActionType.USE_ITEM) {
                Monster target = action.targetMonster != null ? action.targetMonster : action.player.getActiveMonster();
                action.player.useItem(action.targetIndex, target);
            }
        }
        
        // 3. Attaques (le plus rapide attaque en premier)
        List<Action> attackActions = new ArrayList<>();
        for (Action action : actions) {
            if (action.type == Action.ActionType.ATTACK) {
                attackActions.add(action);
            }
        }
        
        // Trier par vitesse
        attackActions.sort((a1, a2) -> {
            Monster m1 = a1.player.getActiveMonster();
            Monster m2 = a2.player.getActiveMonster();
            if (m1 == null || m2 == null) return 0;
            return Integer.compare(m2.getSpeed(), m1.getSpeed()); // Plus rapide en premier
        });
        
        for (Action action : attackActions) {
            executeAttack(action);
        }
        
        // Appliquer les effets de statut
        applyStatusEffects();
        
        // Mettre à jour le terrain
        field.updateTerrain();
        
        actions.clear();
    }
    
    private void executeAttack(Action action) {
        Monster attacker = action.player.getActiveMonster();
        if (attacker == null || !attacker.isAlive()) {
            return;
        }
        
        // Vérifier la paralysie
        if (StatusEffectManager.checkParalysis(attacker)) {
            System.out.println(attacker.getName() + " est paralysé et rate son attaque !");
            return;
        }
        
        // Trouver la cible (l'autre joueur)
        Player targetPlayer = (action.player == field.getPlayer1()) ? 
                              field.getPlayer2() : field.getPlayer1();
        Monster target = targetPlayer.getActiveMonster();
        
        if (target == null || !target.isAlive()) {
            return;
        }
        
        if (action.attackIndex >= 0 && action.attackIndex < attacker.getAttacks().size()) {
            com.esiea.monstredepoche.models.Attack attack = attacker.getAttacks().get(action.attackIndex);
            
            if (attack.canUse()) {
                if (attack.use()) {
                    double damage = attack.calculateDamage(attacker, target);
                    target.takeDamage((int) damage);
                    System.out.println(attacker.getName() + " utilise " + attack.getName() + 
                                     " et inflige " + (int)damage + " dégâts à " + target.getName());
                    
                    // Utiliser la capacité spéciale
                    attacker.useSpecialAbility(field, target);
                } else {
                    System.out.println(attacker.getName() + " rate son attaque " + attack.getName() + " !");
                }
            } else {
                System.out.println(attack.getName() + " n'a plus d'utilisations !");
            }
        }
    }
    
    private void applyStatusEffects() {
        // Appliquer les dégâts de brûlure et empoisonnement
        Monster m1 = field.getPlayer1().getActiveMonster();
        Monster m2 = field.getPlayer2().getActiveMonster();
        
        if (m1 != null && m1.isAlive()) {
            StatusEffectManager.applyBurnDamage(m1);
            StatusEffectManager.applyPoisonDamage(m1);
            StatusEffectManager.updateStatusDuration(m1);
        }
        
        if (m2 != null && m2.isAlive()) {
            StatusEffectManager.applyBurnDamage(m2);
            StatusEffectManager.applyPoisonDamage(m2);
            StatusEffectManager.updateStatusDuration(m2);
        }
        
        // Appliquer les effets du terrain
        field.applyTerrainEffects();
    }
    
    public void determineOrder() {
        // L'ordre est déterminé par la vitesse lors de l'exécution des attaques
    }
}

