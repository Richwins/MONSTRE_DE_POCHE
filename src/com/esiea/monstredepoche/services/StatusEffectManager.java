package com.esiea.monstredepoche.services;

import com.esiea.monstredepoche.models.Monster;
import com.esiea.monstredepoche.models.enums.StatusCondition;
import com.esiea.monstredepoche.utils.Constants;

/**
 * Service de gestion des altérations d'état.
 * Gère l'application, la résolution et les dégâts des états :
 * - Paralysie : 25% de chance de rater une attaque
 * - Brûlure : 10% de l'attaque en dégâts par tour
 * - Empoisonnement : 10% de l'attaque en dégâts par tour
 */
public class StatusEffectManager {
    
    /**
     * Applique la brûlure à un monstre.
     * Un monstre ne peut être brûlé que s'il n'a aucune altération d'état.
     * @param monster Le monstre à brûler
     */
    public static void applyBurn(Monster monster) {
        if (monster.getCurrentStatus() == StatusCondition.NONE) {
            monster.setCurrentStatus(StatusCondition.BURNED);
            System.out.println(monster.getName() + " est brûlé !");
        }
    }
    
    /**
     * Applique l'empoisonnement à un monstre.
     * Un monstre ne peut être empoisonné que s'il n'a aucune altération d'état.
     * @param monster Le monstre à empoisonner
     */
    public static void applyPoison(Monster monster) {
        if (monster.getCurrentStatus() == StatusCondition.NONE) {
            monster.setCurrentStatus(StatusCondition.POISONED);
            System.out.println(monster.getName() + " est empoisonné !");
        }
    }
    
    /**
     * Applique la paralysie à un monstre.
     * Un monstre déjà paralysé peut être re-paralysé (compteur remis à zéro).
     * @param monster Le monstre à paralyser
     */
    public static void applyParalysis(Monster monster) {
        monster.setCurrentStatus(StatusCondition.PARALYZED);
        monster.resetParalysisCounter(); // Redémarre le compteur
    }
    
    public static boolean checkParalysis(Monster monster) {
        if (monster.getCurrentStatus() == StatusCondition.PARALYZED) {
            return java.util.concurrent.ThreadLocalRandom.current().nextDouble() < Constants.PARALYSIS_FAIL_CHANCE;
        }
        return false;
    }
    
    /**
     * Applique les dégâts de brûlure à un monstre.
     * Dégâts = 1/10 de l'attaque du monstre
     * @param monster Le monstre brûlé
     */
    public static void applyBurnDamage(Monster monster) {
        if (monster.getCurrentStatus() == StatusCondition.BURNED) {
            int damage = (int) (monster.getAttack() * Constants.BURN_DAMAGE_RATIO);
            monster.takeDamage(damage);
            System.out.println(monster.getName() + " subit " + damage + " dégâts de brûlure !");
        }
    }
    
    /**
     * Applique les dégâts d'empoisonnement à un monstre.
     * Dégâts = 1/10 de l'attaque du monstre
     * @param monster Le monstre empoisonné
     */
    public static void applyPoisonDamage(Monster monster) {
        if (monster.getCurrentStatus() == StatusCondition.POISONED) {
            int damage = (int) (monster.getAttack() * Constants.POISON_DAMAGE_RATIO);
            monster.takeDamage(damage);
            System.out.println(monster.getName() + " subit " + damage + " dégâts de poison !");
        }
    }
    
    public static void removeStatus(Monster monster) {
        monster.setCurrentStatus(StatusCondition.NONE);
    }
    
    /**
     * Met à jour la durée des altérations d'état.
     * Pour la paralysie : probabilité de guérison = (tours passés) / 6
     * (100% de chances au 6ème tour)
     * @param monster Le monstre dont l'état doit être mis à jour
     */
    public static void updateStatusDuration(Monster monster) {
        // La paralysie se dissipe progressivement
        if (monster.getCurrentStatus() == StatusCondition.PARALYZED) {
            monster.incrementParalysisCounter();
            int turns = monster.getParalysisCounter();
            
            // Probabilité de guérison = tours / 6 (max 100% au tour 6)
            double healProbability = Math.min(1.0, turns / 6.0);
            
            if (java.util.concurrent.ThreadLocalRandom.current().nextDouble() < healProbability) {
                removeStatus(monster);
                System.out.println(monster.getName() + " n'est plus paralysé !");
            }
        }
    }
}

