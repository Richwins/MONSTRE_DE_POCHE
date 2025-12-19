package com.esiea.monstredepoche.interfaces;

import com.esiea.monstredepoche.models.Monster;
import com.esiea.monstredepoche.models.enums.StatusCondition;

/**
 * Interface pour les entités capables d'infliger des altérations d'état.
 * Permet d'appliquer des états (paralysie, brûlure, empoisonnement) aux monstres.
 */
public interface StatusInflictor {
    boolean inflictStatus(Monster target);
    StatusCondition getInflictedStatus();
}

