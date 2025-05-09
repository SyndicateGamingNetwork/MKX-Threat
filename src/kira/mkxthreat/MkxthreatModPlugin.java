package kira.mkxthreat;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import exerelin.campaign.SectorManager;
import kira.mkxthreat.campaign.econ.impl.MkxthreatItems;
import kira.mkxthreat.scripts.MkxthreatGen;
import kira.mkxthreat.scripts.world.systems.Helia;

public class MkxthreatModPlugin extends BaseModPlugin {

    MkxthreatItems itemrepo=new MkxthreatItems();

    @Override
    public void onNewGame() {
        super.onNewGame();

        boolean isNexerelinEnabled = Global.getSettings().getModManager().isModEnabled("nexerelin");

        if (!isNexerelinEnabled || SectorManager.getManager().isCorvusMode()) {
            new MkxthreatGen().generate(Global.getSector());
        }
    }
    @Override
    public void onGameLoad(boolean newGame){
        super.onGameLoad(newGame);
        itemrepo.putInfoForSpecialItems();
        itemrepo.setSpecialItemNewIndustries("mkxthreatfragmenthub", "heavyindustry,orbitalworks,fragmentworks");
    }
}