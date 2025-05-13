package kira.mkxthreat.scripts;

import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.RepLevel;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import kira.mkxthreat.scripts.world.systems.Helia;

import java.util.List;

public class MkxthreatGen {
    public static void initFactionRelationships(SectorAPI sector) {
        FactionAPI hegemony = sector.getFaction(Factions.HEGEMONY);
        FactionAPI tritachyon = sector.getFaction(Factions.TRITACHYON);
        FactionAPI pirates = sector.getFaction(Factions.PIRATES);
        FactionAPI kol = sector.getFaction(Factions.KOL);
        FactionAPI church = sector.getFaction(Factions.LUDDIC_CHURCH);
        FactionAPI path = sector.getFaction(Factions.LUDDIC_PATH);
        FactionAPI league = sector.getFaction(Factions.PERSEAN);
        FactionAPI independent = sector.getFaction(Factions.INDEPENDENT);
        FactionAPI threat = sector.getFaction(Factions.THREAT);
        FactionAPI sleeper = sector.getFaction(Factions.SLEEPER);
        FactionAPI sindrian_diktat = sector.getFaction(Factions.DIKTAT);
        FactionAPI scavengers = sector.getFaction(Factions.SCAVENGERS);
        FactionAPI remnants = sector.getFaction(Factions.REMNANTS);
        FactionAPI poor = sector.getFaction(Factions.POOR);
        FactionAPI player = sector.getFaction(Factions.PLAYER);
        FactionAPI omega = sector.getFaction(Factions.OMEGA);
        FactionAPI neutral = sector.getFaction(Factions.NEUTRAL);
        FactionAPI mercenary = sector.getFaction(Factions.MERCENARY);
        FactionAPI lions_guard = sector.getFaction(Factions.LIONS_GUARD);
        FactionAPI dweller = sector.getFaction(Factions.DWELLER);
        FactionAPI derelict = sector.getFaction(Factions.DERELICT);
        FactionAPI mkxthreat = sector.getFaction("mkxthreat");

        mkxthreat.setRelationship(path.getId(), RepLevel.VENGEFUL);
        mkxthreat.setRelationship(hegemony.getId(), RepLevel.VENGEFUL);
        mkxthreat.setRelationship(pirates.getId(), RepLevel.VENGEFUL);
        mkxthreat.setRelationship(tritachyon.getId(), RepLevel.VENGEFUL);
        mkxthreat.setRelationship(church.getId(), RepLevel.VENGEFUL);
        mkxthreat.setRelationship(kol.getId(), RepLevel.VENGEFUL);
        mkxthreat.setRelationship(league.getId(), RepLevel.VENGEFUL);
        mkxthreat.setRelationship(independent.getId(), RepLevel.VENGEFUL);
        mkxthreat.setRelationship(threat.getId(), RepLevel.VENGEFUL);
        mkxthreat.setRelationship(sleeper.getId(), RepLevel.VENGEFUL);
        mkxthreat.setRelationship(sindrian_diktat.getId(), RepLevel.VENGEFUL);
        mkxthreat.setRelationship(scavengers.getId(), RepLevel.VENGEFUL);
        mkxthreat.setRelationship(remnants.getId(), RepLevel.VENGEFUL);
        mkxthreat.setRelationship(poor.getId(), RepLevel.VENGEFUL);
        mkxthreat.setRelationship(player.getId(), RepLevel.VENGEFUL);
        mkxthreat.setRelationship(omega.getId(), RepLevel.VENGEFUL);
        mkxthreat.setRelationship(neutral.getId(), RepLevel.VENGEFUL);
        mkxthreat.setRelationship(mercenary.getId(), RepLevel.VENGEFUL);
        mkxthreat.setRelationship(lions_guard.getId(), RepLevel.VENGEFUL);
        mkxthreat.setRelationship(dweller.getId(), RepLevel.VENGEFUL);
        mkxthreat.setRelationship(derelict.getId(), RepLevel.VENGEFUL);
        mkxthreat.setRelationship("SCY", RepLevel.VENGEFUL);
        mkxthreat.setRelationship("pn_colony", RepLevel.VENGEFUL);
        mkxthreat.setRelationship("neutrinocorp", RepLevel.VENGEFUL);
        mkxthreat.setRelationship("dassault_mikoyan", RepLevel.VENGEFUL);
        mkxthreat.setRelationship("JYD", RepLevel.VENGEFUL);
        mkxthreat.setRelationship("diableavionics", RepLevel.VENGEFUL);
        mkxthreat.setRelationship("cabal", RepLevel.VENGEFUL);
        mkxthreat.setRelationship("the_deserter", RepLevel.VENGEFUL);
        mkxthreat.setRelationship("blade_breakers", RepLevel.VENGEFUL);
        mkxthreat.setRelationship("kingdom_of_terra", RepLevel.VENGEFUL);
        mkxthreat.setRelationship("unitedpamed", RepLevel.VENGEFUL);
        mkxthreat.setRelationship("brighton", RepLevel.VENGEFUL);
        mkxthreat.setRelationship("hiigaran_descendants", RepLevel.VENGEFUL);
        mkxthreat.setRelationship("prv", RepLevel.VENGEFUL);
        mkxthreat.setRelationship("scalartech", RepLevel.VENGEFUL);
        mkxthreat.setRelationship("star_federation", RepLevel.VENGEFUL);
        mkxthreat.setRelationship("kadur_remnant", RepLevel.VENGEFUL);
        mkxthreat.setRelationship("keruvim", RepLevel.VENGEFUL);
        mkxthreat.setRelationship("mayasura", RepLevel.VENGEFUL);
        mkxthreat.setRelationship("noir", RepLevel.VENGEFUL);
        mkxthreat.setRelationship("Lte", RepLevel.VENGEFUL);
        mkxthreat.setRelationship("GKSec", RepLevel.VENGEFUL);
        mkxthreat.setRelationship("gmda", RepLevel.VENGEFUL);
        mkxthreat.setRelationship("oculus", RepLevel.VENGEFUL);
        mkxthreat.setRelationship("nomads", RepLevel.VENGEFUL);
        mkxthreat.setRelationship("thulelegacy", RepLevel.VENGEFUL);
        mkxthreat.setRelationship("infected", RepLevel.VENGEFUL);
        mkxthreat.setRelationship("ORA", RepLevel.VENGEFUL);
        mkxthreat.setRelationship("HMI", RepLevel.VENGEFUL);
        mkxthreat.setRelationship("draco", RepLevel.VENGEFUL);
        mkxthreat.setRelationship("roider", RepLevel.VENGEFUL);
        mkxthreat.setRelationship("ironshell", RepLevel.VENGEFUL);
        mkxthreat.setRelationship("magellan_protectorate", RepLevel.VENGEFUL);
        mkxthreat.setRelationship("exalted", RepLevel.VENGEFUL);
        mkxthreat.setRelationship("fang", RepLevel.VENGEFUL);
        mkxthreat.setRelationship("xhanempire", RepLevel.VENGEFUL);
        mkxthreat.setRelationship("xlu", RepLevel.VENGEFUL);
        mkxthreat.setRelationship("fpe", RepLevel.VENGEFUL);
        mkxthreat.setRelationship("al_ars", RepLevel.VENGEFUL);
        mkxthreat.setRelationship("UAF", RepLevel.VENGEFUL);
        mkxthreat.setRelationship("Imperium", RepLevel.VENGEFUL);
        mkxthreat.setRelationship("sotf_dustkeepers", RepLevel.VENGEFUL);
        mkxthreat.setRelationship("sotf_dustkeepers_proxies", RepLevel.VENGEFUL);
        mkxthreat.setRelationship("sotf_sierra_faction", RepLevel.VENGEFUL);
        mkxthreat.setRelationship("sotf_taken", RepLevel.VENGEFUL);
    }

    public void generate(SectorAPI sector) {

        initFactionRelationships(sector);
        new Helia().generate(sector);
    }
}