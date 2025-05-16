package kira.mkxthreat.campaign.econ.industries;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.SpecialItemData;
import com.fs.starfarer.api.campaign.econ.CommoditySpecAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.MarketConditionAPI;
import com.fs.starfarer.api.campaign.econ.MarketImmigrationModifier;
import com.fs.starfarer.api.impl.campaign.econ.ResourceDepositsCondition;
import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.Items;
import com.fs.starfarer.api.impl.campaign.population.PopulationComposition;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.Pair;
import exerelin.utilities.StringHelper;
import kira.mkxthreat.ids.MkxthreatStrings;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MkxthreatMining extends BaseIndustry implements MarketImmigrationModifier{

        public void apply() {
            super.apply(true);

            int size = market.getSize();

            supply(Commodities.ORE, size +2);
            supply(Commodities.FUEL, size +2);
            supply(Commodities.METALS, size +2);
            supply(Commodities.RARE_METALS, size +2);
            supply(Commodities.RARE_ORE, size +2);
            supply(Commodities.VOLATILES, size +2);

            demand(Commodities.HEAVY_MACHINERY, size - 3);
            demand(Commodities.MARINES, size - 3);


            if (!isFunctional()) {
                supply.clear();
            }
        }


        @Override
        public void unapply() {
            super.unapply();
        }

        protected boolean hasPostDemandSection(boolean hasDemand, IndustryTooltipMode mode) {
            Pair<String, Integer> deficit = getMaxDeficit(Commodities.MARINES);
            if (deficit.two <= 0) return false;
            //return mode == IndustryTooltipMode.NORMAL && isFunctional();
            return mode != IndustryTooltipMode.NORMAL || isFunctional();
        }

        @Override
        protected void addPostDemandSection(TooltipMakerAPI tooltip, boolean hasDemand, IndustryTooltipMode mode) {
            //if (mode == IndustryTooltipMode.NORMAL && isFunctional()) {
            if (mode != IndustryTooltipMode.NORMAL || isFunctional()) {
                Color h = Misc.getHighlightColor();
                float opad = 10f;
                float pad = 3f;

                Pair<String, Integer> deficit = getMaxDeficit(Commodities.MARINES);
                if (deficit.two > 0) {
                    tooltip.addPara(getDeficitText(Commodities.MARINES) + ": %s units. Reduced colony growth.", pad, h, "" + deficit.two);
                }
            }
        }


        @Override
        public boolean isAvailableToBuild() {
            if (!super.isAvailableToBuild()) return false;

            for (MarketConditionAPI mc : market.getConditions()) {
                String commodity = ResourceDepositsCondition.COMMODITY.get(mc.getId());
                if (commodity != null) {
                    String industry = ResourceDepositsCondition.INDUSTRY.get(commodity);
                    if (getId().equals(industry)) return true;
                }
            }
            return false;
        }

        @Override
        public String getUnavailableReason() {
            if (!super.isAvailableToBuild()) return super.getUnavailableReason();

            return "Requires resource deposits";
        }

        public void modifyIncoming(MarketAPI market, PopulationComposition incoming) {
            Pair<String, Integer> deficit = getMaxDeficit(Commodities.MARINES);
            if (deficit.two > 0) {
                incoming.getWeight().modifyFlat(getModId(), -deficit.two, "Mining: marines shortage");
            }
        }

        @Override
        public String getCurrentImage() {
            float size = market.getSize();
            if (market.getPlanetEntity() != null && market.getPlanetEntity().isGasGiant()) {
                return Global.getSettings().getSpriteName("industry", "mining_gas_giant");
            }
            if (size <= SIZE_FOR_SMALL_IMAGE) {
                return Global.getSettings().getSpriteName("industry", "mining_low");
            }
            return super.getCurrentImage();
        }

        public float getPatherInterest() {
            return 1f + super.getPatherInterest();
        }

        @Override
        protected boolean canImproveToIncreaseProduction() {
            return true;
        }

        public void applyVisuals(PlanetAPI planet) {
            if (planet == null) return;
            planet.getSpec().setShieldTexture2(Global.getSettings().getSpriteName("industry", "plasma_net_texture"));
            planet.getSpec().setShieldThickness2(0.15f);
            //planet.getSpec().setShieldColor2(new Color(255,255,255,175));
            planet.getSpec().setShieldColor2(new Color(255,255,255,255));
            planet.applySpecChanges();
            shownPlasmaNetVisuals = true;
        }

        public void unapplyVisuals(PlanetAPI planet) {
            if (planet == null) return;
            planet.getSpec().setShieldTexture2(null);
            planet.getSpec().setShieldThickness2(0f);
            planet.getSpec().setShieldColor2(null);
            planet.applySpecChanges();
            shownPlasmaNetVisuals = false;
        }

        protected boolean shownPlasmaNetVisuals = false;

        @Override
        public void setSpecialItem(SpecialItemData special) {
            super.setSpecialItem(special);

            if (shownPlasmaNetVisuals && (special == null || !special.getId().equals(Items.PLASMA_DYNAMO))) {
                unapplyVisuals(market.getPlanetEntity());
            }

            if (special != null && special.getId().equals(Items.PLASMA_DYNAMO)) {
                applyVisuals(market.getPlanetEntity());
            }
        }

    }
