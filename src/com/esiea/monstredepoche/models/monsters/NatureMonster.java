package com.esiea.monstredepoche.models.monsters;

import com.esiea.monstredepoche.models.Monster;
import com.esiea.monstredepoche.models.enums.MonsterType;

/**
 * Classe abstraite représentant la catégorie Nature.
 * Regroupe les types Plante et Insecte.
 * 
 * Les monstres de type Nature ont la faculté commune de récupérer 
 * 1/20 de leurs PV au début de chaque tour sur un terrain inondé 
 * (après avoir utilisé une attaque spéciale).
 */
public abstract class NatureMonster extends Monster {

    /**
     * Constructeur pour les monstres de catégorie Nature.
     * 
     * @param name    Le nom du monstre
     * @param type    Le type élémentaire (PLANTE ou INSECTE)
     * @param hp      Les points de vie du monstre
     * @param speed   La vitesse du monstre
     * @param attack  L'attaque du monstre
     * @param defense La défense du monstre
     */
    public NatureMonster(String name, MonsterType type, int hp, int speed, int attack, int defense) {
        super(name, type, hp, speed, attack, defense);
    }

    /**
     * Les capacités spéciales sont implémentées par les sous-classes
     * (PlantMonster et InsectMonster).
     */
    // useSpecialAbility() est définie dans les sous-classes
}

