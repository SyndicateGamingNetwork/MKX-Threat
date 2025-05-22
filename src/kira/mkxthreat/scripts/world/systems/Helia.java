package kira.mkxthreat.scripts.world.systems;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.characters.FullName;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.impl.campaign.ids.*;
import com.fs.starfarer.api.impl.campaign.intel.deciv.DecivTracker;
import com.fs.starfarer.api.impl.campaign.procgen.NebulaEditor;
import com.fs.starfarer.api.impl.campaign.terrain.HyperspaceAbyssPluginImpl;
import com.fs.starfarer.api.impl.campaign.terrain.HyperspaceTerrainPlugin;
import com.fs.starfarer.api.util.Misc;
import kira.mkxthreat.campaign.econ.MkxthreatHabitatCondition;
import kira.mkxthreat.campaign.econ.industries.MkxIndustries;
import kira.mkxthreat.campaign.submarkets.Mkxthreathab;
import kira.mkxthreat.ids.MkxthreatStrings;
import org.magiclib.util.MagicCampaign;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Helia {

    public void generate(SectorAPI sector) {

        StarSystemAPI system = sector.createStarSystem("Helia");

        system.getLocation().set(-68300, -42600); //position of the system on the map

        PlanetAPI helia = system.initStar("Helia", "Helia", 1000f, 600);

        helia.setCustomDescriptionId("Helia");
        system.setBackgroundTextureFilename("graphics/backgrounds/background4.jpg");
        system.setLightColor(new Color(130, 155, 145)); // light color in entire system, affects all entities

        system.addAsteroidBelt(helia, 150, 3200, 500, 290, 310, Terrain.ASTEROID_BELT, "Fragmented planet");
        system.addRingBand(helia, "misc", "rings_ice0", 256f, 0, Color.gray, 256f, 3200, 360f, null, null);

        JumpPointAPI jumpPoint1 = Global.getFactory().createJumpPoint(null, "Jump-point");
        jumpPoint1.setStandardWormholeToHyperspaceVisual();
        jumpPoint1.setCircularOrbit(helia, 180.0F, 13000.0F, 365.0F);
        system.addEntity(jumpPoint1);
        SectorEntityToken debris2 = MagicCampaign.createDebrisField(
                "debrisfield", 600, 2f, 10000, 0, 250, 100.0F, MkxthreatStrings.MKXTHREAT, 80, 0.2f, true, null, jumpPoint1, 90, 0, 365
        );
        MagicCampaign.addSalvage(null, debris2, MagicCampaign.lootType.SUPPLIES, null, 231);

        system.autogenerateHyperspaceJumpPoints(false, false);
        system.setHyperspaceAnchor(helia);
        setAbyssalDetectedRanges(system); //sets the system to only be detected when close//

        SectorEntityToken mkxthreatSensorArray = system.addCustomEntity("mkxthreat_sensor", "Domain Sensor Array", "sensor_array", "mkxthreat");
        SectorEntityToken mkxthreatNavArray = system.addCustomEntity("mkxthreat_nav", "Domain Navigation Array", "nav_buoy", "mkxthreat");
        SectorEntityToken mkxthreatRelay = system.addCustomEntity("mkxthreat_relay", "Domain Communication Array", "comm_relay", "mkxthreat");

        mkxthreatSensorArray.setCircularOrbitPointingDown(helia, 120.0F, 11000.0F, 260.0F);
        mkxthreatNavArray.setCircularOrbitPointingDown(helia, 240.0F, 8000.0F, 200.0F);
        mkxthreatRelay.setCircularOrbitPointingDown(helia, 80.0F, 4300.0F, 100.0F);

        SectorEntityToken inactiveGate = system.addCustomEntity(null, null, Entities.INACTIVE_GATE, "mkxthreat");
        inactiveGate.setCircularOrbit(helia, 0.0F, 13000.0F, 365.0F);
        SectorEntityToken debris1 = MagicCampaign.createDebrisField(
                "debrisfield", 600, 2f, 10000, 0, 250, 100.0F, MkxthreatStrings.MKXTHREAT, 80, 0.2f, true, null, inactiveGate, 90, 0, 365
        );
        MagicCampaign.addSalvage(null, debris1, MagicCampaign.lootType.SUPPLIES, null, 231);

        PlanetAPI hel = system.addPlanet("Hel", helia, "Hel", "Hel", 900, 180f, 6000, 220f);
        hel.setCustomDescriptionId("Hel");
        hel.setInteractionImage("illustrations", "sentinel");
        hel.getSpec().setGlowTexture(Global.getSettings().getSpriteName("hab_glows", "hel_glow"));
        hel.applySpecChanges();
        system.addRingBand(hel, "misc", "rings_ice0", 300f, 1, Color.gray, 250f, 500, 300f, null, null);
        system.addAsteroidBelt(hel, 20, 280, 55, 100, 150);
        MarketAPI helmarket = AddMarketplace.addMarketplace("mkxthreat", hel, null, "Hel", 8,

                new ArrayList<>(Arrays.asList( //conditions
                        Conditions.ORE_ULTRARICH,
                        Conditions.RARE_ORE_ULTRARICH,
                        Conditions.VOLATILES_PLENTIFUL,
                        Conditions.POPULATION_8,
                        Conditions.STEALTH_MINEFIELDS,
                        Conditions.RUINS_VAST,
                        MkxthreatHabitatCondition.MKXTHREATHABITATCONDITION
                )),

                new ArrayList<>(Arrays.asList( //industries
                        MkxIndustries.MKXTHREATFRAGMENTCORE,
                        Industries.MEGAPORT,
                        Industries.WAYSTATION,
                        MkxIndustries.MKXTHREATPLANETARYSHIELD,
                        MkxIndustries.MKXTHREATMINING,
                        MkxIndustries.MKXTHREATMILITARYBASE,
                        MkxIndustries.MKXFRAGMENTSTATION,
                        MkxIndustries.MKXHEAVYBATTERIES
                )),

                new ArrayList<>(Arrays.asList( //markets
                        Submarkets.SUBMARKET_STORAGE,
                        Submarkets.SUBMARKET_OPEN,
                        Mkxthreathab.MKXTHREATHAB,
                        Submarkets.GENERIC_MILITARY
                )),

                0.15f

        );

        helmarket.addIndustry("mkxfragmentworks", new ArrayList(Arrays.asList("mkxthreatfragmenthub")));
        helmarket.getMemoryWithoutUpdate().set(DecivTracker.NO_DECIV_KEY, true);

        PersonAPI x_mk = MagicCampaign.createCaptainBuilder("mkxthreat")
                .setIsAI(true)
                .setAICoreType(MkxthreatStrings.THREAT_CORE)
                .setFirstName("X")
                .setLastName("MK")
                .setPortraitId("X_MK")
                .setGender(FullName.Gender.ANY)
                .setFactionId("mkxthreat")
                .setRankId(Ranks.SPACE_COMMANDER)
                .setPostId(Ranks.POST_FLEET_COMMANDER)
                .setPersonality(Personalities.RECKLESS)
                .setLevel(9)
                .setEliteSkillsOverride(9)
                .create();

        SectorEntityToken defencefleet = MagicCampaign.createFleetBuilder()
                .setFleetName("X MK Defence Fleet")
                .setFleetFaction("mkxthreat")
                .setFlagshipName("X MK Defabricator")
                .setFlagshipVariant("mkx_fabricator_unit_type450")
                .setCaptain(x_mk)
                .setMinFP(280)
                .setReinforcementFaction(MkxthreatStrings.MKXTHREAT)
                .setQualityOverride(50f)
                .setSpawnLocation(hel)
                .setAssignment(FleetAssignment.ORBIT_AGGRESSIVE)
                .setAssignmentTarget(hel)
                .setIsImportant(true)
                .setTransponderOn(false)
                .create();
        defencefleet.setDiscoverable(true);

        SectorEntityToken fragmentedstation = system.addCustomEntity(null, null, MkxthreatStrings.THREAT_STATION, "mkxthreat");
        fragmentedstation.setCircularOrbitPointingDown(inactiveGate, 185.0F, 420.0F, 125);
        fragmentedstation.setCustomDescriptionId("mkxthreat_station");
        fragmentedstation.setInteractionImage("illustrations", "volturn_shrine_fake");

        MarketAPI helstationmarket = AddMarketplace.addMarketplace("mkxthreat", fragmentedstation, null, "Fragmented Habitat", 8,

                new ArrayList<>(Arrays.asList( //conditions
                        Conditions.POPULATION_8,
                        Conditions.STEALTH_MINEFIELDS,
                        MkxthreatHabitatCondition.MKXTHREATHABITATCONDITION
                )),

                new ArrayList<>(Arrays.asList( //industries
                        MkxIndustries.MKXTHREATFRAGMENTCORE,
                        Industries.MEGAPORT,
                        Industries.WAYSTATION,
                        MkxIndustries.MKXTHREATPLANETARYSHIELD,
                        MkxIndustries.MKXTHREATMINING,
                        MkxIndustries.MKXTHREATMILITARYBASE,
                        MkxIndustries.MKXFRAGMENTSTATION,
                        MkxIndustries.MKXHEAVYBATTERIES
                )),

                new ArrayList<>(Arrays.asList( //markets
                        Submarkets.SUBMARKET_STORAGE,
                        Submarkets.SUBMARKET_OPEN,
                        Mkxthreathab.MKXTHREATHAB,
                        Submarkets.GENERIC_MILITARY
                )),

        0.15f

        );

        helstationmarket.addIndustry("mkxfragmentworks", new ArrayList(Arrays.asList("mkxthreatfragmenthub")));
        helstationmarket.getMemoryWithoutUpdate().set(DecivTracker.NO_DECIV_KEY, true);

        SectorEntityToken fragmentedstation2 = system.addCustomEntity(null, "Fragmented Blockade", MkxthreatStrings.THREAT_STATION, "mkxthreat");
        fragmentedstation2.setCircularOrbitPointingDown(jumpPoint1, 185.0F, 420.0F, 125);
        fragmentedstation2.setCustomDescriptionId("mkxthreat_station");
        fragmentedstation2.setInteractionImage("illustrations", "volturn_shrine_fake");

        MarketAPI helstationmarket2 = AddMarketplace.addMarketplace("mkxthreat", fragmentedstation2, null, "Fragmented Blockade", 8,

                new ArrayList<>(Arrays.asList( //conditions
                        Conditions.POPULATION_8,
                        Conditions.STEALTH_MINEFIELDS,
                        MkxthreatHabitatCondition.MKXTHREATHABITATCONDITION
                )),

                new ArrayList<>(Arrays.asList( //industries
                        MkxIndustries.MKXTHREATFRAGMENTCORE,
                        Industries.MEGAPORT,
                        Industries.WAYSTATION,
                        MkxIndustries.MKXTHREATPLANETARYSHIELD,
                        MkxIndustries.MKXTHREATMINING,
                        MkxIndustries.MKXTHREATMILITARYBASE,
                        MkxIndustries.MKXFRAGMENTSTATION,
                        MkxIndustries.MKXHEAVYBATTERIES
                )),

                new ArrayList<>(Arrays.asList( //markets
                        Submarkets.SUBMARKET_STORAGE,
                        Submarkets.SUBMARKET_OPEN,
                        Mkxthreathab.MKXTHREATHAB,
                        Submarkets.GENERIC_MILITARY
                )),

                0.15f

        );

        helstationmarket2.addIndustry("mkxfragmentworks", new ArrayList(Arrays.asList("mkxthreatfragmenthub")));
        helstationmarket2.getMemoryWithoutUpdate().set(DecivTracker.NO_DECIV_KEY, true);

        HyperspaceTerrainPlugin plugin = (HyperspaceTerrainPlugin) Misc.getHyperspaceTerrain().getPlugin(); //these lines clear the hyperspace clouds around the system
        NebulaEditor editor = new NebulaEditor(plugin);
        float minRadius = plugin.getTileSize() * 2f;
        float radius = system.getMaxRadiusInHyperspace();
        editor.clearArc(system.getLocation().x, system.getLocation().y, 0, radius + minRadius, 0, 360f);
        editor.clearArc(system.getLocation().x, system.getLocation().y, 0, radius + minRadius, 0, 360f, 0.25f);

    }


    public static void setAbyssalDetectedRanges(StarSystemAPI system) {   //Needed to get the star to not show on the map//
        if (system.getAutogeneratedJumpPointsInHyper() != null) {
            for (JumpPointAPI jp : system.getAutogeneratedJumpPointsInHyper()) {
                if (jp.isStarAnchor()) {
                    jp.addTag(Tags.STAR_HIDDEN_ON_MAP);
                }
                float range = HyperspaceAbyssPluginImpl.JUMP_POINT_DETECTED_RANGE;
                if (jp.isGasGiantAnchor()) {
                    range = HyperspaceAbyssPluginImpl.GAS_GIANT_DETECTED_RANGE;
                } else if (jp.isStarAnchor()) {
                    range = HyperspaceAbyssPluginImpl.STAR_DETECTED_RANGE;
                }
                setAbyssalDetectedRange(jp, range);
            }
        }

        if (system.getAutogeneratedNascentWellsInHyper() != null) {
            for (NascentGravityWellAPI well : system.getAutogeneratedNascentWellsInHyper()) {
                setAbyssalDetectedRange(well, HyperspaceAbyssPluginImpl.NASCENT_WELL_DETECTED_RANGE);
            }
        }
    }

    public static void setAbyssalDetectedRange(SectorEntityToken entity, float range) {
        float detectedRange = range / HyperspaceTerrainPlugin.ABYSS_SENSOR_RANGE_MULT;

        float maxSensorRange = Global.getSettings().getSensorRangeMaxHyper();
        float desired = detectedRange * HyperspaceTerrainPlugin.ABYSS_SENSOR_RANGE_MULT;
        if (desired > maxSensorRange) {
            entity.setExtendedDetectedAtRange(desired - maxSensorRange);
        }

        entity.setSensorProfile(1f);
        entity.setDiscoverable(false);
        entity.getDetectedRangeMod().modifyFlat("jpDetRange", detectedRange);

        float mult = Math.min(0.5f, 400f / range);
        entity.setDetectionRangeDetailsOverrideMult(mult);
    }   //Needed to get the star to not show on the map//


}
