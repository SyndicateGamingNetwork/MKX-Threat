package kira.mkxthreat.campaign.econ.industries;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SpecialItemData;
import com.fs.starfarer.api.campaign.econ.CommodityOnMarketAPI;
import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry;
import com.fs.starfarer.api.impl.campaign.ids.*;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.Pair;
import kira.mkxthreat.campaign.econ.impl.MkxthreatItems;

import java.awt.*;

public class MkxFragmentWorks extends BaseIndustry {

    public static float FRAGMENT_WORKS_QUALITY_BONUS = 0.4f;

    public static String POLLUTION_ID = Conditions.POLLUTION;
    public static float DAYS_BEFORE_POLLUTION = 0f;
    public static float DAYS_BEFORE_POLLUTION_PERMANENT = 90f;


    public void apply() {
        super.apply(true);

        int size = market.getSize();

        boolean works = MkxIndustries.FRAGMENTWORKS.equals(getId());
        int shipBonus = 0;
        float qualityBonus = 0;
        if (works) {
            //shipBonus = 2;
            qualityBonus = FRAGMENT_WORKS_QUALITY_BONUS;
        }

        demand(Commodities.METALS, size - 2);
        demand(Commodities.RARE_METALS, size - 2);

        supply(Commodities.HEAVY_MACHINERY, size - 1);
        supply(Commodities.SUPPLIES, size - 1);
        supply(Commodities.HAND_WEAPONS, size - 1);
        supply(Commodities.SHIPS, size -1);
        if (shipBonus > 0) {
            supply(1, Commodities.SHIPS, shipBonus, "Orbital works");
        }

        Pair<String, Integer> deficit = getMaxDeficit(Commodities.METALS, Commodities.RARE_METALS);
        int maxDeficit = size - 3;
        if (deficit.two > maxDeficit) deficit.two = maxDeficit;

        applyDeficitToProduction(2, deficit,
                Commodities.HEAVY_MACHINERY,
                Commodities.SUPPLIES,
                Commodities.SHIPS);

        if (qualityBonus > 0) {
            market.getStats().getDynamic().getMod(Stats.PRODUCTION_QUALITY_MOD).modifyFlat(getModId(1), qualityBonus, "Orbital works");
        }

        float stability = market.getPrevStability();
        if (stability < 5) {
            float stabilityMod = (stability - 5f) / 5f;
            stabilityMod *= 0.5f;
            market.getStats().getDynamic().getMod(Stats.PRODUCTION_QUALITY_MOD).modifyFlat(getModId(0), stabilityMod, getNameForModifier() + " - low stability");
        }

        if (!isFunctional()) {
            supply.clear();
            unapply();
        }
    }

    @Override
    public void unapply() {
        super.unapply();

        market.getStats().getDynamic().getMod(Stats.PRODUCTION_QUALITY_MOD).unmodifyFlat(getModId(0));
        market.getStats().getDynamic().getMod(Stats.PRODUCTION_QUALITY_MOD).unmodifyFlat(getModId(1));
    }


    @Override
    protected void addPostDemandSection(TooltipMakerAPI tooltip, boolean hasDemand, IndustryTooltipMode mode) {
        if (mode != IndustryTooltipMode.NORMAL || isFunctional()) {
            boolean works = MkxIndustries.FRAGMENTWORKS.equals(getId());
            if (works) {
                float total = FRAGMENT_WORKS_QUALITY_BONUS;
                String totalStr = "+" + (int)Math.round(total * 100f) + "%";
                Color h = Misc.getHighlightColor();
                if (total < 0) {
                    h = Misc.getNegativeHighlightColor();
                    totalStr = "" + (int)Math.round(total * 100f) + "%";
                }
                float opad = 10f;
                if (total >= 0) {
                    tooltip.addPara("Ship quality: %s", opad, h, totalStr);
                    tooltip.addPara("*Quality bonus only applies for the largest ship producer in the faction.",
                            Misc.getGrayColor(), opad);
                }
            }
        }
    }

    public boolean isDemandLegal(CommodityOnMarketAPI com) {
        return true;
    }

    public boolean isSupplyLegal(CommodityOnMarketAPI com) {
        return true;
    }

    @Override
    protected boolean canImproveToIncreaseProduction() {
        return true;
    }

    @Override
    public boolean wantsToUseSpecialItem(SpecialItemData data) {
        if (special != null && Items.CORRUPTED_NANOFORGE.equals(special.getId()) &&
                data != null && Items.PRISTINE_NANOFORGE.equals(data.getId()) &&
                data != null && MkxthreatItems.MKXTHREATFRAGMENTHUB.equals(data.getId())
        ) {
            return true;
        }
        return super.wantsToUseSpecialItem(data);
    }

    protected boolean permaPollution = false;
    protected boolean addedPollution = false;
    protected float daysWithNanoforge = 0f;

    @Override
    public void advance(float amount) {
        super.advance(amount);

        if (special != null) {
            float days = Global.getSector().getClock().convertToDays(amount);
            daysWithNanoforge += days;

            updatePollutionStatus();
        }
    }

    protected void updatePollutionStatus() {
        if (!market.hasCondition(Conditions.HABITABLE)) return;

        if (special != null) {
            if (!addedPollution && daysWithNanoforge >= DAYS_BEFORE_POLLUTION) {
                if (market.hasCondition(POLLUTION_ID)) {
                    permaPollution = true;
                } else {
                    market.addCondition(POLLUTION_ID);
                    addedPollution = true;
                }
            }
            if (addedPollution && !permaPollution) {
                if (daysWithNanoforge > DAYS_BEFORE_POLLUTION_PERMANENT) {
                    permaPollution = true;
                }
            }
        } else if (addedPollution && !permaPollution) {
            market.removeCondition(POLLUTION_ID);
            addedPollution = false;
        }
    }

    public boolean isPermaPollution() {
        return permaPollution;
    }

    public void setPermaPollution(boolean permaPollution) {
        this.permaPollution = permaPollution;
    }

    public boolean isAddedPollution() {
        return addedPollution;
    }

    public void setAddedPollution(boolean addedPollution) {
        this.addedPollution = addedPollution;
    }

    public float getDaysWithNanoforge() {
        return daysWithNanoforge;
    }

    public void setDaysWithNanoforge(float daysWithNanoforge) {
        this.daysWithNanoforge = daysWithNanoforge;
    }

    @Override
    public void setSpecialItem(SpecialItemData special) {
        super.setSpecialItem(special);

        updatePollutionStatus();
    }



//	@Override
//	public List<InstallableIndustryItemPlugin> getInstallableItems() {
//		ArrayList<InstallableIndustryItemPlugin> list = new ArrayList<InstallableIndustryItemPlugin>();
//		list.add(new GenericInstallableItemPlugin(this));
//		return list;
//	}


}
