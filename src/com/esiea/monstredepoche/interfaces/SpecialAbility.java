package com.esiea.monstredepoche.interfaces;

import com.esiea.monstredepoche.models.BattleField;
import com.esiea.monstredepoche.models.Monster;

/**
 * Interface pour les capacités spéciales des monstres.
 * Chaque type de monstre implémente sa capacité unique (paralysie, inondation, etc.)
 */
public interface SpecialAbility {
    void activateAbility(BattleField field, Monster opponent);
}

