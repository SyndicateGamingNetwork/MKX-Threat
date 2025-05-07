package kira.mkxthreat.utility;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.impl.campaign.AICoreAdminPluginImpl;
import com.fs.starfarer.api.impl.campaign.procgen.Constellation;
import com.fs.starfarer.api.util.Misc;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import kira.mkxthreat.ids.MkxthreatStrings;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lwjgl.util.vector.Vector2f;
import exerelin.campaign.AllianceManager;

    public class MkxthreatUtils {
    public static boolean isPlayerCommissionedOrAllianced() {
        String commissioningFaction = Misc.getCommissionFactionId();
        if (commissioningFaction != null && commissioningFaction.equals("mkxthreat")) {
            return true;
        } else if (commissioningFaction != null && AllianceManager.areFactionsAllied(commissioningFaction, "mkxthreat")) {
            return true;
        } else {
            return AllianceManager.areFactionsAllied("player", "mkxthreat");
        }
    }

    public static boolean isPlayerInAlliance() {
        String commissioningFaction = Misc.getCommissionFactionId();
        if (commissioningFaction != null && AllianceManager.areFactionsAllied(commissioningFaction, "mkxthreat")) {
            return true;
        } else {
            return AllianceManager.areFactionsAllied("player", "mkxthreat");
        }
    }

        public static final boolean LUNALIB_ENABLED = Global.getSettings().getModManager().isModEnabled("lunalib");

        public static JSONObject getMergedSystemJSON() throws JSONException, IOException {
            return Global.getSettings().getMergedJSONForMod(MkxthreatStrings.PATH_MERGED_JSON_CUSTOM_STAR_SYSTEMS, MkxthreatStrings.MOD_ID_CUSTOMIZABLE_STAR_SYSTEMS);
        }

        public static void generateAdminsOnMarkets(Map<MarketAPI, String> marketMap) {
            if (marketMap != null && !marketMap.isEmpty()) {
                AICoreAdminPluginImpl aiPlugin = new AICoreAdminPluginImpl();

                for(MarketAPI market : marketMap.keySet()) {
                    switch ((String)marketMap.get(market)) {
                        case "player":
                            market.setAdmin((PersonAPI)null);
                            break;
                        case "alpha_core":
                            market.setAdmin(aiPlugin.createPerson("alpha_core", market.getFaction().getId(), 0L));
                    }
                }

                marketMap.clear();
            }

        }

        public static void teleportPlayerToSystem(StarSystemAPI system) {
            CampaignFleetAPI player = Global.getSector().getPlayerFleet();
            PlanetAPI star = system.getStar();
            player.getContainingLocation().removeEntity(player);
            star.getContainingLocation().addEntity(player);
            Global.getSector().setCurrentLocation(star.getContainingLocation());
            player.setNoEngaging(2.0F);
            player.clearAssignments();
        }

        public static List<Constellation> getProcgenConstellations() {
            HashSet<Constellation> constellations = new HashSet();

            for(StarSystemAPI sys : Global.getSector().getStarSystems()) {
                if (sys.isProcgen() && sys.isInConstellation()) {
                    constellations.add(sys.getConstellation());
                }
            }

            ArrayList<Constellation> sortedConstellations = new ArrayList(constellations);
            Vector2f centroidPoint = getHyperspaceCenter();
            sortedConstellations.sort((c1, c2) -> c1 == c2 ? 0 : Float.compare(Misc.getDistance(centroidPoint, c1.getLocation()), Misc.getDistance(centroidPoint, c2.getLocation())));
            return sortedConstellations;
        }

        public static Vector2f getHyperspaceCenter() {
            try {
                JSONArray coordinates = Global.getSettings().getJSONArray(MkxthreatStrings.SETTINGS_HYPERSPACE_CENTER);
                return new Vector2f((float)coordinates.getInt(0), (float)coordinates.getInt(1));
            } catch (JSONException var1) {
                return new Vector2f(-4531.0F, -5865.0F);
            }
        }
}