package com.esiea.monstredepoche.models.monsters;

import com.esiea.monstredepoche.models.BattleField;
import com.esiea.monstredepoche.models.Monster;
import com.esiea.monstredepoche.models.enums.MonsterType;
import com.esiea.monstredepoche.services.StatusEffectManager;

/**
 * Monstre de type Insecte (sous-type de la catégorie Nature).
 * Hérite de NatureMonster pour bénéficier de la récupération de PV sur terrain inondé.
 * 
 * Capacité spéciale spécifique : empoisonne l'adversaire de façon garantie 1 attaque sur 3.
 */
public class InsectMonster extends NatureMonster {
    private int poisonCounter;
    
    /**
     * Constructeur d'un monstre de type Insecte.
     * @param name    Le nom du monstre
     * @param hp      Les points de vie du monstre
     * @param speed   La vitesse du monstre
     * @param attack  L'attaque du monstre
     * @param defense La défense du monstre
     */
    public InsectMonster(String name, int hp, int speed, int attack, int defense) {
        super(name, MonsterType.INSECT, hp, speed, attack, defense);
        this.poisonCounter = 0;
    }
    
    /**
     * Utilise la capacité spéciale : empoisonne l'adversaire
     */
    @Override
    public void useSpecialAbility(BattleField field, Monster opponent) {
        if (opponent != null && opponent.isAlive()) {
            inflictPoison(opponent);
        }
    }
    
    /**
     * Empoisonne l'adversaire de façon garantie 1 attaque sur 3.
     * Pas de variable aléatoire, contrairement aux monstres de feu.
     * @param target Le monstre à empoisonner
     */
    public void inflictPoison(Monster target) {
        poisonCounter++;
        // Empoisonne une attaque sur trois (garanti)
        if (poisonCounter % 3 == 0) {
            StatusEffectManager.applyPoison(target);
            System.out.println(getName() + " empoisonne " + target.getName() + " !");
        }
    }
    
    public int getPoisonCounter() {
        return poisonCounter;
    }
}

