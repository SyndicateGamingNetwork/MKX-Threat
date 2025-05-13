package kira.mkxthreat;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import exerelin.campaign.SectorManager;
import kira.mkxthreat.campaign.econ.impl.MkxthreatItems;
import kira.mkxthreat.scripts.MkxthreatGen;

public class MkxthreatModPlugin extends BaseModPlugin {

    MkxthreatItems itemrepo=new MkxthreatItems();

    @Override
    public void onNewGame() { //Needed for Nexerelin to work properly
        super.onNewGame();

        boolean isNexerelinEnabled = Global.getSettings().getModManager().isModEnabled("nexerelin");

        if (!isNexerelinEnabled || SectorManager.getManager().isCorvusMode()) {
            new MkxthreatGen().generate(Global.getSector());
        }
    }
    @Override
    public void onGameLoad(boolean newGame){  //Needed to add in special items to an industry
        super.onGameLoad(newGame);
        itemrepo.putInfoForSpecialItems();
        itemrepo.setSpecialItemNewIndustries("mkxthreatfragmenthub", "heavyindustry,orbitalworks,mkxfragmentworks");
    }
}