package kira.mkxthreat.campaign.econ.impl;

import com.fs.starfarer.api.PluginPick;
import com.fs.starfarer.api.campaign.AICoreAdminPlugin;
import com.fs.starfarer.api.campaign.AICoreOfficerPlugin;
import com.fs.starfarer.api.campaign.BaseCampaignPlugin;

public class MkxCampaignPluginImpl extends BaseCampaignPlugin {
        public PluginPick<AICoreOfficerPlugin> pickAICoreOfficerPlugin(String commodityId) {
            return "threat_core".equals(commodityId) ? new PluginPick(new MkxCoreOfficerPlugin(), PickPriority.MOD_SET) : null;
        }

        public PluginPick<AICoreAdminPlugin> pickAICoreAdminPlugin(String commodityId) {
            return "threat_core".equals(commodityId) ? new PluginPick(new MkxCoreAdminPlugin(), PickPriority.MOD_SET) : null;
        }
}
