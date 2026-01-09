package com.esiea.monstredepoche.controllers;

import com.esiea.monstredepoche.models.Attack;
import com.esiea.monstredepoche.models.BattleField;
import com.esiea.monstredepoche.models.Monster;
import com.esiea.monstredepoche.models.Player;
import com.esiea.monstredepoche.models.enums.AttackType;
import com.esiea.monstredepoche.models.enums.MonsterType;
import com.esiea.monstredepoche.services.DamageCalculator;
import com.esiea.monstredepoche.services.StatusEffectManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestionnaire de tours de combat.
 * Gère l'ordre d'exécution des actions (changement de monstre, objets, attaques)
 * et applique les effets de statut et du terrain.
 */
public class TurnManager {
    
    /**
     * Classe interne représentant une action d'un joueur
     */
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
    
    /**
     * Ajoute une action à la file d'exécution
     * @param action L'action à ajouter
     */
    public void addAction(Action action) {
        actions.add(action);
    }
    
    /**
     * Exécute toutes les actions dans l'ordre :
     * 1. Changements de monstres
     * 2. Utilisation d'objets
     * 3. Attaques (triées par vitesse)
     * Puis applique les effets de statut et met à jour le terrain
     */
    public void executeActions() {
        // Réinitialiser le flag d'utilisation de capacité spéciale pour tous les monstres
        resetSpecialAbilityFlags();
        
        // 1. Changements de monstres
        for (Action action : actions) {
            if (action.type == Action.ActionType.SWITCH_MONSTER) {
                action.player.switchMonster(action.targetIndex);
            }
        }
        
        // Vérifier si le monstre qui a inondé est toujours actif
        field.checkFloodSource();
        
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
        
        // Vérifier la glissade sur terrain inondé
        if (checkSlip(attacker)) {
            return; // L'attaque est annulée
        }
        
        // Trouver la cible (l'autre joueur)
        Player targetPlayer = (action.player == field.getPlayer1()) ? 
                              field.getPlayer2() : field.getPlayer1();
        Monster target = targetPlayer.getActiveMonster();
        
        if (target == null || !target.isAlive()) {
            return;
        }
        
        // Vérifier si une attaque est disponible
        boolean hasAvailableAttack = false;
        for (Attack a : attacker.getAttacks()) {
            if (a.canUse()) {
                hasAvailableAttack = true;
                break;
            }
        }
        
        if (action.attackIndex >= 0 && action.attackIndex < attacker.getAttacks().size()) {
            Attack attack = attacker.getAttacks().get(action.attackIndex);
            
            if (attack.canUse()) {
                if (attack.use()) {
                    double damage = attack.calculateDamage(attacker, target);
                    target.takeDamage((int) damage);
                    System.out.println(attacker.getName() + " utilise " + attack.getName() + 
                                     " et inflige " + (int)damage + " dégâts à " + target.getName());
                    
                    // Utiliser la capacité spéciale uniquement si l'attaque est du même type que le monstre
                    if (isSpecialAttack(attacker, attack)) {
                        attacker.setHasUsedSpecialAbility(true);
                        attacker.useSpecialAbility(field, target);
                    }
                } else {
                    System.out.println(attacker.getName() + " rate son attaque " + attack.getName() + " !");
                }
            } else {
                System.out.println(attack.getName() + " n'a plus d'utilisations !");
                // Attaque à mains nues si aucune attaque disponible
                if (!hasAvailableAttack) {
                    performBareHandAttack(attacker, target);
                }
            }
        } else if (action.attackIndex == -1) {
            // Attaque normale (mains nues) - dégâts de base
            performBareHandAttack(attacker, target);
        }
    }
    
    private void performBareHandAttack(Monster attacker, Monster target) {
        double damage = DamageCalculator.calculateBareDamage(attacker, target);
        target.takeDamage((int) damage);
        System.out.println(attacker.getName() + " attaque à mains nues et inflige " + 
                          (int)damage + " dégâts à " + target.getName());
        
        // Les attaques à mains nues ne déclenchent PAS les capacités spéciales
    }
    
    /**
     * Vérifie si une attaque est une attaque spéciale (même type que le monstre)
     * @param monster Le monstre attaquant
     * @param attack L'attaque utilisée
     * @return true si l'attaque est du même type que le monstre
     */
    private boolean isSpecialAttack(Monster monster, Attack attack) {
        MonsterType monsterType = monster.getType();
        AttackType attackType = attack.getType();
        
        // Correspondance entre types de monstre et types d'attaque
        switch (monsterType) {
            case ELECTRIC:
                return attackType == AttackType.ELECTRIC;
            case WATER:
                return attackType == AttackType.WATER;
            case GROUND:
                return attackType == AttackType.GROUND;
            case FIRE:
                return attackType == AttackType.FIRE;
            case PLANT:
            case INSECT:
                return attackType == AttackType.NATURE;
            default:
                return false;
        }
    }
    
    /**
     * Vérifie si le monstre glisse sur le terrain inondé.
     * Les monstres de type eau ne glissent jamais.
     * En cas de glissade : l'attaque est annulée et le monstre subit 1/4 de sa propre attaque.
     * @param attacker Le monstre attaquant
     * @return true si le monstre a glissé (attaque annulée)
     */
    private boolean checkSlip(Monster attacker) {
        // Vérifier si le terrain est inondé
        if (!field.isFlooded()) {
            return false;
        }
        
        // Les monstres de type eau ne glissent pas
        if (attacker.getType() == MonsterType.WATER) {
            return false;
        }
        
        // Récupérer la probabilité de glissade depuis le monstre d'eau qui a inondé
        Monster floodSource = field.getFloodSource();
        if (floodSource instanceof com.esiea.monstredepoche.models.monsters.WaterMonster) {
            com.esiea.monstredepoche.models.monsters.WaterMonster waterMonster = 
                (com.esiea.monstredepoche.models.monsters.WaterMonster) floodSource;
            double fallChance = waterMonster.getFallChance();
            
            // Tester la probabilité de glissade
            if (java.util.concurrent.ThreadLocalRandom.current().nextDouble() < fallChance) {
                // Le monstre glisse !
                int damage = attacker.getAttack() / 4; // 1/4 de sa propre attaque
                attacker.takeDamage(damage);
                System.out.println(attacker.getName() + " glisse sur le terrain inondé !");
                System.out.println(attacker.getName() + " subit " + damage + " dégâts de chute !");
                return true; // L'attaque est annulée
            }
        }
        
        return false; // Pas de glissade
    }
    
    private void applyStatusEffects() {
        // Appliquer les dégâts de brûlure et empoisonnement
        Monster m1 = field.getPlayer1().getActiveMonster();
        Monster m2 = field.getPlayer2().getActiveMonster();
        
        if (m1 != null && m1.isAlive()) {
            StatusEffectManager.applyBurnDamage(m1);
            StatusEffectManager.applyPoisonDamage(m1);
            StatusEffectManager.updateStatusDuration(m1);
            
            // Mettre à jour la durée d'enfouissement pour les monstres de terre
            if (m1 instanceof com.esiea.monstredepoche.models.monsters.GroundMonster) {
                ((com.esiea.monstredepoche.models.monsters.GroundMonster) m1).updateUndergroundDuration();
            }
        }
        
        if (m2 != null && m2.isAlive()) {
            StatusEffectManager.applyBurnDamage(m2);
            StatusEffectManager.applyPoisonDamage(m2);
            StatusEffectManager.updateStatusDuration(m2);
            
            // Mettre à jour la durée d'enfouissement pour les monstres de terre
            if (m2 instanceof com.esiea.monstredepoche.models.monsters.GroundMonster) {
                ((com.esiea.monstredepoche.models.monsters.GroundMonster) m2).updateUndergroundDuration();
            }
        }
        
        // Appliquer les effets du terrain
        field.applyTerrainEffects();
    }
    
    public void determineOrder() {
        // L'ordre est déterminé par la vitesse lors de l'exécution des attaques
    }
    
    /**
     * Réinitialise le flag d'utilisation de capacité spéciale pour tous les monstres
     */
    private void resetSpecialAbilityFlags() {
        if (field.getPlayer1().getActiveMonster() != null) {
            field.getPlayer1().getActiveMonster().setHasUsedSpecialAbility(false);
        }
        if (field.getPlayer2().getActiveMonster() != null) {
            field.getPlayer2().getActiveMonster().setHasUsedSpecialAbility(false);
        }
    }
}

