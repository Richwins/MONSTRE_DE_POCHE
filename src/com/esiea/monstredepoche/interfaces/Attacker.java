package com.esiea.monstredepoche.interfaces;

import com.esiea.monstredepoche.models.Attack;
import com.esiea.monstredepoche.models.Monster;

public interface Attacker {
    void attack(Monster target);
    double calculateDamage(Monster target, Attack attack);
}

