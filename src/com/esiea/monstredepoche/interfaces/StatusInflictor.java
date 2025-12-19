package com.esiea.monstredepoche.interfaces;

import com.esiea.monstredepoche.models.Monster;
import com.esiea.monstredepoche.models.enums.StatusCondition;

public interface StatusInflictor {
    boolean inflictStatus(Monster target);
    StatusCondition getInflictedStatus();
}

