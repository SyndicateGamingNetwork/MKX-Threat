package kira.mkxthreat;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import exerelin.campaign.SectorManager;
import kira.mkxthreat.campaign.econ.impl.MkxCampaignPluginImpl;
import kira.mkxthreat.campaign.econ.impl.MkxthreatItems;
import kira.mkxthreat.scripts.MkxthreatGen;

public class MkxthreatModPlugin extends BaseModPlugin {

    MkxthreatItems itemrepo=new MkxthreatItems();

    @Override
    public void onNewGame() {
        super.onNewGame();

        boolean isNexerelinEnabled = Global.getSettings().getModManager().isModEnabled("nexerelin"); //Needed for Nexerelin to work properly

        if (!isNexerelinEnabled || SectorManager.getManager().isCorvusMode()) {
            new MkxthreatGen().generate(Global.getSector());
        }
    }
    @Override
    public void onGameLoad(boolean newGame){
        super.onGameLoad(newGame);
        itemrepo.putInfoForSpecialItems(); //Needed to add in special items to an industry
        itemrepo.setSpecialItemNewIndustries("mkxthreatfragmenthub", "heavyindustry,orbitalworks,mkxfragmentworks");
        Global.getSector().registerPlugin(new MkxCampaignPluginImpl());
    }
}