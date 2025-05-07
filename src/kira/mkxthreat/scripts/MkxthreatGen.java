package kira.mkxthreat.scripts;

import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.RepLevel;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import kira.mkxthreat.scripts.world.systems.Helia;

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

        mkxthreat.setRelationship(path.getId(), RepLevel.HOSTILE);
        mkxthreat.setRelationship(hegemony.getId(), RepLevel.HOSTILE);
        mkxthreat.setRelationship(pirates.getId(), RepLevel.HOSTILE);
        mkxthreat.setRelationship(tritachyon.getId(), RepLevel.HOSTILE);
        mkxthreat.setRelationship(church.getId(), RepLevel.HOSTILE);
        mkxthreat.setRelationship(kol.getId(), RepLevel.HOSTILE);
        mkxthreat.setRelationship(league.getId(), RepLevel.HOSTILE);
        mkxthreat.setRelationship(independent.getId(), RepLevel.HOSTILE);
        mkxthreat.setRelationship(threat.getId(), RepLevel.HOSTILE);
        mkxthreat.setRelationship(sleeper.getId(), RepLevel.HOSTILE);
        mkxthreat.setRelationship(sindrian_diktat.getId(), RepLevel.HOSTILE);
        mkxthreat.setRelationship(scavengers.getId(), RepLevel.HOSTILE);
        mkxthreat.setRelationship(remnants.getId(), RepLevel.HOSTILE);
        mkxthreat.setRelationship(poor.getId(), RepLevel.HOSTILE);
        mkxthreat.setRelationship(player.getId(), RepLevel.HOSTILE);
        mkxthreat.setRelationship(omega.getId(), RepLevel.HOSTILE);
        mkxthreat.setRelationship(neutral.getId(), RepLevel.HOSTILE);
        mkxthreat.setRelationship(mercenary.getId(), RepLevel.HOSTILE);
        mkxthreat.setRelationship(lions_guard.getId(), RepLevel.HOSTILE);
        mkxthreat.setRelationship(dweller.getId(), RepLevel.HOSTILE);
        mkxthreat.setRelationship(derelict.getId(), RepLevel.HOSTILE);
    }

    public void generate(SectorAPI sector) {

        initFactionRelationships(sector);
        new Helia().generate(sector);


    }
}