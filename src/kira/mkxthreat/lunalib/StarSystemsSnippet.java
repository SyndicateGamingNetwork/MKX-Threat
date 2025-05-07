package kira.mkxthreat.lunalib;

import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.procgen.Constellation;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kira.mkxthreat.scripts.world.systems.Helia;
import kira.mkxthreat.utility.MkxthreatUtils;
import lunalib.lunaDebug.LunaSnippet;
import lunalib.lunaDebug.SnippetBuilder;
import org.json.JSONObject;
import kira.mkxthreat.ids.MkxthreatStrings;

    public class StarSystemsSnippet extends LunaSnippet {
        public String getName() {
            return MkxthreatStrings.SNIPPETS_SPAWN_SYSTEM_NAME;
        }

        public String getDescription() {
            return MkxthreatStrings.SNIPPETS_SPAWN_SYSTEM_DESC;
        }

        public String getModId() {
            return MkxthreatStrings.MOD_ID_CUSTOMIZABLE_STAR_SYSTEMS;
        }

        public List<String> getTags() {
            List<String> tags = new ArrayList();
            tags.add(SnippetTags.Cheat.toString());
            tags.add(SnippetTags.Entity.toString());
            return tags;
        }

        public void addParameters(SnippetBuilder builder) {
            JSONObject systems;
            try {
                systems = MkxthreatUtils.getMergedSystemJSON();
            } catch (Exception var5) {
                builder.addBooleanParameter(MkxthreatStrings.COMMANDS_ERROR_BAD_JSON, MkxthreatStrings.COMMANDS_ERROR_BAD_JSON, true);
                return;
            }

            Iterator<String> it = systems.keys();

            while (it.hasNext()) {
                String systemId = (String) it.next();
                builder.addBooleanParameter(systemId, systemId, false);
            }

        }

        public void execute(Map<String, Object> parameters, TooltipMakerAPI output) {
            List<String> enabledParams = new ArrayList();

            for (String param : parameters.keySet()) {
                if ((Boolean) parameters.get(param)) {
                    enabledParams.add(param);
                }
            }

            if (enabledParams.isEmpty()) {
                output.addPara(MkxthreatStrings.SNIPPETS_SPAWN_SYSTEM_NO_SELECTED, 0.0F, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), new String[0]);
            } else {
                this.generateSystems(enabledParams, output);
            }
        }

        private void generateSystems(List<String> enabledParams, TooltipMakerAPI output) {
            JSONObject systems;
            try {
                systems = MkxthreatUtils.getMergedSystemJSON();
            } catch (Exception e) {
                output.addPara(MkxthreatStrings.COMMANDS_ERROR_BAD_JSON + e, 0.0F, Misc.getNegativeHighlightColor(), Misc.getHighlightColor(), new String[0]);
                return;
            }

            for (String systemId : enabledParams) {
                if (!systems.has(systemId)) {
                    output.addPara(String.format(MkxthreatStrings.COMMANDS_ERROR_NO_SYSTEM_ID, systemId), 0.0F, Misc.getNegativeHighlightColor(), Misc.getHighlightColor(), new String[0]);
                    return;
                }
            }

            StringBuilder print = new StringBuilder();
            StarSystemAPI teleportSystem = null;
            List<Constellation> constellations = MkxthreatUtils.getProcgenConstellations();
            Map<MarketAPI, String> marketsToOverrideAdmin = new HashMap();

            for (String systemId : enabledParams) {
                try {
                    JSONObject systemOptions = systems.getJSONObject(systemId);

                    for (int numOfSystems = systemOptions.optInt(MkxthreatStrings.OPT_NUMBER_OF_SYSTEMS, 1); numOfSystems > 0; --numOfSystems) {
                        Helia newSystem = new Helia();

                        print.append(String.format(MkxthreatStrings.COMMANDS_GENERATED_SYSTEM, systemId));
                    }
                } catch (Exception e) {
                    print.append(String.format(MkxthreatStrings.COMMANDS_ERROR_BAD_SYSTEM, systemId));
                    output.addPara(print.append(e).toString(), 0.0F, Misc.getNegativeHighlightColor(), Misc.getHighlightColor(), new String[0]);
                    return;
                }
            }

            if (teleportSystem != null) {
                MkxthreatUtils.teleportPlayerToSystem(teleportSystem);
            }

            MkxthreatUtils.generateAdminsOnMarkets(marketsToOverrideAdmin);
            output.addPara(print.toString(), 0.0F, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), new String[0]);
        }
    }