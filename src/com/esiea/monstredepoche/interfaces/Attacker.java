package com.esiea.monstredepoche.interfaces;

import com.esiea.monstredepoche.models.Attack;
import com.esiea.monstredepoche.models.Monster;

/**
 * Interface pour les entités capables d'attaquer.
 * Définit les méthodes nécessaires pour effectuer une attaque et calculer les dégâts.
 */
public interface Attacker {
    void attack(Monster target);
    double calculateDamage(Monster target, Attack attack);
}

