package com.esiea.monstredepoche.interfaces;

import com.esiea.monstredepoche.models.BattleField;
import com.esiea.monstredepoche.models.Monster;

public interface SpecialAbility {
    void activateAbility(BattleField field, Monster opponent);
}

