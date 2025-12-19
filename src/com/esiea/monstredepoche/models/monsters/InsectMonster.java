package com.esiea.monstredepoche.models.monsters;

import com.esiea.monstredepoche.models.BattleField;
import com.esiea.monstredepoche.models.Monster;
import com.esiea.monstredepoche.models.enums.MonsterType;
import com.esiea.monstredepoche.services.StatusEffectManager;

/**
 * Monstre de type Insecte (sous-type de Nature).
 * Capacité spéciale : empoisonne l'adversaire de façon garantie 1 attaque sur 3.
 * Bénéficie aussi de la récupération 1/20 PV sur terrain inondé (capacité Nature).
 */
public class InsectMonster extends Monster {
    private int poisonCounter;
    
    /**
     * Constructeur d'un monstre insecte
     */
    public InsectMonster(String name, int hp, int speed, int attack, int defense) {
        super(name, MonsterType.NATURE, hp, speed, attack, defense);
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

