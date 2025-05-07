package kira.mkxthreat.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class Extendedautomation extends BaseHullMod {

    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getPeakCRDuration().modifyPercent(id, 100f);
        stats.getMaxCombatReadiness().modifyFlat(id , 1f);
    }
}