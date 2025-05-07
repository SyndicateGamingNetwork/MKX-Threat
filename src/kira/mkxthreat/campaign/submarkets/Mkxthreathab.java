package kira.mkxthreat.campaign.submarkets;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.econ.CommodityOnMarketAPI;
import com.fs.starfarer.api.campaign.econ.SubmarketAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.*;
import com.fs.starfarer.api.impl.campaign.submarkets.OpenMarketPlugin;
import com.fs.starfarer.api.util.Misc;
import org.lazywizard.lazylib.MathUtils;

import java.util.Random;

public class Mkxthreathab extends OpenMarketPlugin {
    private final RepLevel MIN_REP;
    String curr_faction;

    public Mkxthreathab() {
        this.MIN_REP = RepLevel.FRIENDLY;
        this.curr_faction = "mkxtrheat";
    }

    public void updateCargoPrePlayerInteraction() {
        if (sinceLastCargoUpdate < 30) return;
        sinceLastCargoUpdate = 0f;

        CargoAPI cargo = getCargo();
        for (CargoStackAPI s : cargo.getStacksCopy()) {
            float qty = s.getSize();
            cargo.removeItems(s.getType(), s.getData(), qty);
        }

        if (okToUpdateShipsAndWeapons()) {
            sinceSWUpdate = 0f;

            boolean military = Misc.isMilitary(market);
            boolean hiddenBase = market.getMemoryWithoutUpdate().getBoolean(MemFlags.HIDDEN_BASE_MEM_FLAG);

            float extraShips = 0f;
            //int extraShipSize = 0;
            if (military && hiddenBase && !market.hasSubmarket(Submarkets.GENERIC_MILITARY)) {
                extraShips = 150f;
                //extraShipSize = 1;
            }

            getCargo().getMothballedShips().clear();

            CommodityOnMarketAPI com;

            float freighters = 10f;
            com = market.getCommodityData(Commodities.SHIPS);
            freighters += com.getMaxSupply() * 2f;
            if (freighters > 30) freighters = 30;
            addShips(market.getFactionId(),
                    10f + extraShips, freighters, 0f, 10f, 10f, 5f, null, 0f, FactionAPI.ShipPickMode.PRIORITY_THEN_ALL, null);
            addShips(market.getFactionId(),
                    40f, 0f, 0f, 0f, 0f, 0f, null, -1f, null, null, 5);

            float tankers = 20f;
            com = market.getCommodityData(Commodities.FUEL);
            tankers += com.getMaxSupply() * 3f;
            if (tankers > 40) tankers = 40;
            addShips(market.getFactionId(),
                    0f, 0f, tankers, 0, 0f, 0f, null, 0f, FactionAPI.ShipPickMode.PRIORITY_THEN_ALL, null);
        }
        cargo.addCommodity("alpha_core", MathUtils.getRandomNumberInRange(0, 2));
        cargo.addCommodity("beta_core", MathUtils.getRandomNumberInRange(2, 3));
        cargo.addCommodity("gamma_core", MathUtils.getRandomNumberInRange(3, 5));
        getCargo().sort();
    }

    public boolean isEnabled(CoreUIAPI ui) {
        RepLevel level = this.market.getFaction().getRelationshipLevel(Global.getSector().getFaction("player"));
        return level.isAtWorst(this.MIN_REP);
    }

    public static String MKXTHREATHAB = "mkxthreathab";

    public void init(SubmarketAPI submarket) {
        super.init(submarket);
    }

    @Override
    public int getStockpileLimit(CommodityOnMarketAPI com) {

        float limit = OpenMarketPlugin.getBaseStockpileLimit(com);

        Random random = new Random(market.getId().hashCode() + submarket.getSpecId().hashCode() + Global.getSector().getClock().getMonth() * 170000);
        limit *= 0.9f + 0.2f * random.nextFloat();

        float sm = market.getStabilityValue() / 10f;
        limit *= (0.25f + 0.75f * sm);

        // more fuel and supplies
        if (com.getId().equals(Commodities.SUPPLIES)) {
            limit *= 2f;
        } else if (com.isFuel()) {
            limit *= 3f;
        }

        if (limit < 0) limit = 0;

        return (int) limit;
    }

    @Override
    public String getIllegalTransferText(FleetMemberAPI member, TransferAction action) {
        if (action == TransferAction.PLAYER_BUY) {
            return "Untrained in Automated Ships";
        } else {
            if (isFreeTransfer()) {
                return "Illegal to store";
            }
            return "Illegal to sell";
        }
    }
}