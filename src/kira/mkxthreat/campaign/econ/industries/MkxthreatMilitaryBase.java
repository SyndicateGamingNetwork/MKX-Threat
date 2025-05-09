package kira.mkxthreat.campaign.econ.industries;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.BattleAPI;
import com.fs.starfarer.api.campaign.CampaignEventListener;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.econ.CommodityOnMarketAPI;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.listeners.FleetEventListener;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry;
import com.fs.starfarer.api.impl.campaign.econ.impl.MilitaryBase;
import com.fs.starfarer.api.impl.campaign.fleets.*;
import com.fs.starfarer.api.impl.campaign.ids.*;
import com.fs.starfarer.api.impl.campaign.rulecmd.salvage.MarketCMD;
import com.fs.starfarer.api.loading.VariantSource;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.Pair;
import com.fs.starfarer.api.util.WeightedRandomPicker;
import kira.mkxthreat.ids.MkxthreatStrings;

import java.util.Random;

public class MkxthreatMilitaryBase extends BaseIndustry implements RouteManager.RouteFleetSpawner, FleetEventListener {

    @Override
    public boolean isHidden() {
        return !market.getFactionId().equals(MkxthreatStrings.MKXTHREAT);
    }

    @Override
    public boolean isFunctional() {
        return super.isFunctional() && market.getFactionId().equals(MkxthreatStrings.MKXTHREAT);
    }


    public void apply() {
        super.apply(true);

        int size = market.getSize();

        supply(Commodities.CREW, size);
        supply(Commodities.MARINES, size);

        demand(Commodities.SUPPLIES, size - 2);
        demand(Commodities.FUEL, size - 2);
        demand(Commodities.SHIPS, size - 2);
        demand(Commodities.HAND_WEAPONS, size - 2);


        Pair<String, Integer> deficit = getMaxDeficit(Commodities.HAND_WEAPONS);
        applyDeficitToProduction(1, deficit, Commodities.MARINES);

        modifyStabilityWithBaseMod();

        MemoryAPI memory = market.getMemoryWithoutUpdate();
        Misc.setFlagWithReason(memory, MemFlags.MARKET_PATROL, getModId(), true, -1);
        Misc.setFlagWithReason(memory, MemFlags.MARKET_MILITARY, getModId(), true, -1);

        if (!isFunctional()) {
            supply.clear();
            unapply();
        }

    }

    @Override
    public void unapply() {
        super.unapply();

        MemoryAPI memory = market.getMemoryWithoutUpdate();
        Misc.setFlagWithReason(memory, MemFlags.MARKET_PATROL, getModId(), false, -1);
        Misc.setFlagWithReason(memory, MemFlags.MARKET_MILITARY, getModId(), false, -1);

        unmodifyStabilityWithBaseMod();
    }

    protected boolean hasPostDemandSection(boolean hasDemand, IndustryTooltipMode mode) {
        return mode != IndustryTooltipMode.NORMAL || isFunctional();
    }

    @Override
    protected void addPostDemandSection(TooltipMakerAPI tooltip, boolean hasDemand, IndustryTooltipMode mode) {
        if (mode != IndustryTooltipMode.NORMAL || isFunctional()) {
            addStabilityPostDemandSection(tooltip, hasDemand, mode);
        }
    }

    @Override
    protected int getBaseStabilityMod() {
        return 2;
    }

    public String getNameForModifier() {
        if (getSpec().getName().contains("HQ")) {
            return getSpec().getName();
        }
        return Misc.ucFirst(getSpec().getName());
    }

    @Override
    protected Pair<String, Integer> getStabilityAffectingDeficit() {
        return getMaxDeficit(Commodities.SUPPLIES, Commodities.FUEL, Commodities.SHIPS, Commodities.HAND_WEAPONS);
    }

    @Override
    public String getCurrentImage() {
        return super.getCurrentImage();
    }


    public boolean isDemandLegal(CommodityOnMarketAPI com) {
        return true;
    }

    public boolean isSupplyLegal(CommodityOnMarketAPI com) {
        return true;
    }

    protected IntervalUtil tracker = new IntervalUtil(Global.getSettings().getFloat("averagePatrolSpawnInterval") * 0.7f,
            Global.getSettings().getFloat("averagePatrolSpawnInterval") * 1.3f);

    protected float returningPatrolValue = 0f;

    @Override
    protected void buildingFinished() {
        super.buildingFinished();

        tracker.forceIntervalElapsed();
    }

    @Override
    protected void upgradeFinished(Industry previous) {
        super.upgradeFinished(previous);

        tracker.forceIntervalElapsed();
    }

    @Override
    public void advance(float amount) {
        super.advance(amount);

        if (Global.getSector().getEconomy().isSimMode()) return;

        if (!isFunctional()) return;

        float days = Global.getSector().getClock().convertToDays(amount);

        float spawnRate = 1f;
        float rateMult = market.getStats().getDynamic().getStat(Stats.COMBAT_FLEET_SPAWN_RATE_MULT).getModifiedValue();
        spawnRate *= rateMult;


        float extraTime = 0f;
        if (returningPatrolValue > 0) {
            // apply "returned patrols" to spawn rate, at a maximum rate of 1 interval per day
            float interval = tracker.getIntervalDuration();
            extraTime = interval * days;
            returningPatrolValue -= days;
            if (returningPatrolValue < 0) returningPatrolValue = 0;
        }
        tracker.advance(days * spawnRate + extraTime);

        //tracker.advance(days * spawnRate * 100f);

        if (tracker.intervalElapsed()) {
            String sid = getRouteSourceId();

            int light = getCount(FleetFactory.PatrolType.FAST);
            int medium = getCount(FleetFactory.PatrolType.COMBAT);
            int heavy = getCount(FleetFactory.PatrolType.HEAVY);

            int maxLight = 2;
            int maxMedium = 2;
            int maxHeavy = 3;

            WeightedRandomPicker<FleetFactory.PatrolType> picker = new WeightedRandomPicker<FleetFactory.PatrolType>();
            picker.add(FleetFactory.PatrolType.HEAVY, maxHeavy - heavy);
            picker.add(FleetFactory.PatrolType.COMBAT, maxMedium - medium);
            picker.add(FleetFactory.PatrolType.FAST, maxLight - light);

            if (picker.isEmpty()) return;

            FleetFactory.PatrolType type = picker.pick();
            MilitaryBase.PatrolFleetData custom = new MilitaryBase.PatrolFleetData(type);

            RouteManager.OptionalFleetData extra = new RouteManager.OptionalFleetData(market);
            extra.fleetType = type.getFleetType();

            RouteManager.RouteData route = RouteManager.getInstance().addRoute(sid, market, Misc.genRandomSeed(), extra, this, custom);
            float patrolDays = 35f + (float) Math.random() * 10f;

            route.addSegment(new RouteManager.RouteSegment(patrolDays, market.getPrimaryEntity()));
        }
    }

    public void reportAboutToBeDespawnedByRouteManager(RouteManager.RouteData route) {
    }

    public boolean shouldRepeat(RouteManager.RouteData route) {
        return false;
    }

    public int getCount(FleetFactory.PatrolType... types) {
        int count = 0;
        for (RouteManager.RouteData data : RouteManager.getInstance().getRoutesForSource(getRouteSourceId())) {
            if (data.getCustom() instanceof MilitaryBase.PatrolFleetData) {
                MilitaryBase.PatrolFleetData custom = (MilitaryBase.PatrolFleetData) data.getCustom();
                for (FleetFactory.PatrolType type : types) {
                    if (type == custom.type) {
                        count++;
                        break;
                    }
                }
            }
        }
        return count;
    }

    public int getMaxPatrols(FleetFactory.PatrolType type) {
        if (type == FleetFactory.PatrolType.FAST) {
            return (int) market.getStats().getDynamic().getMod(Stats.PATROL_NUM_LIGHT_MOD).computeEffective(0);
        }
        if (type == FleetFactory.PatrolType.COMBAT) {
            return (int) market.getStats().getDynamic().getMod(Stats.PATROL_NUM_MEDIUM_MOD).computeEffective(0);
        }
        if (type == FleetFactory.PatrolType.HEAVY) {
            return (int) market.getStats().getDynamic().getMod(Stats.PATROL_NUM_HEAVY_MOD).computeEffective(0);
        }
        return 0;
    }

    public boolean shouldCancelRouteAfterDelayCheck(RouteManager.RouteData route) {
        return false;
    }

    public void reportBattleOccurred(CampaignFleetAPI fleet, CampaignFleetAPI primaryWinner, BattleAPI battle) {

    }

    public void reportFleetDespawnedToListener(CampaignFleetAPI fleet, CampaignEventListener.FleetDespawnReason reason, Object param) {
        if (!isFunctional()) return;

        if (reason == CampaignEventListener.FleetDespawnReason.REACHED_DESTINATION) {
            RouteManager.RouteData route = RouteManager.getInstance().getRoute(getRouteSourceId(), fleet);
            if (route.getCustom() instanceof MilitaryBase.PatrolFleetData) {
                MilitaryBase.PatrolFleetData custom = (MilitaryBase.PatrolFleetData) route.getCustom();
                if (custom.spawnFP > 0) {
                    float fraction  = fleet.getFleetPoints() / custom.spawnFP;
                    returningPatrolValue += fraction;
                }
            }
        }
    }

    public CampaignFleetAPI spawnFleet(RouteManager.RouteData route) {

        MilitaryBase.PatrolFleetData custom = (MilitaryBase.PatrolFleetData) route.getCustom();
        FleetFactory.PatrolType type = custom.type;

        Random random = route.getRandom();

        float combat = 0f;
        float tanker = 0f;
        float freighter = 0f;
        String fleetType = type.getFleetType();
        switch (type) {
            case FAST:
                combat = Math.round(3f + (float) random.nextFloat() * 2f) * 5f;
                break;
            case COMBAT:
                combat = Math.round(6f + (float) random.nextFloat() * 3f) * 5f;
                tanker = Math.round((float) random.nextFloat()) * 5f;
                break;
            case HEAVY:
                combat = Math.round(10f + (float) random.nextFloat() * 5f) * 5f;
                tanker = Math.round((float) random.nextFloat()) * 10f;
                freighter = Math.round((float) random.nextFloat()) * 10f;
                break;
        }

        FleetParamsV3 params = new FleetParamsV3(
                market,
                null, // loc in hyper; don't need if have market
                MkxthreatStrings.MKXTHREAT,
                route.getQualityOverride(), // quality override
                fleetType,
                combat, // combatPts
                freighter, // freighterPts
                tanker, // tankerPts
                0f, // transportPts
                0f, // linerPts
                0f, // utilityPts
                0f // qualityMod - since the Lion's Guard is in a different-faction market, counter that penalty
        );
        params.timestamp = route.getTimestamp();
        params.random = random;
        params.modeOverride = Misc.getShipPickMode(market);
        params.modeOverride = FactionAPI.ShipPickMode.PRIORITY_THEN_ALL;
        CampaignFleetAPI fleet = FleetFactoryV3.createFleet(params);

        if (fleet == null || fleet.isEmpty()) return null;

        fleet.setFaction(market.getFactionId(), true);
        fleet.setNoFactionInName(true);

        fleet.addEventListener(this);

//		PatrolAssignmentAIV2 ai = new PatrolAssignmentAIV2(fleet, custom);
//		fleet.addScript(ai);

        fleet.getMemoryWithoutUpdate().set(MemFlags.MEMORY_KEY_PATROL_FLEET, true);
        fleet.getMemoryWithoutUpdate().set(MemFlags.FLEET_IGNORES_OTHER_FLEETS, true, 0.3f);

        if (type == FleetFactory.PatrolType.FAST || type == FleetFactory.PatrolType.COMBAT) {
            fleet.getMemoryWithoutUpdate().set(MemFlags.MEMORY_KEY_CUSTOMS_INSPECTOR, true);
        }

        String postId = Ranks.POST_PATROL_COMMANDER;
        String rankId = Ranks.SPACE_COMMANDER;
        switch (type) {
            case FAST:
                rankId = Ranks.SPACE_LIEUTENANT;
                break;
            case COMBAT:
                rankId = Ranks.SPACE_COMMANDER;
                break;
            case HEAVY:
                rankId = Ranks.SPACE_CAPTAIN;
                break;
        }

        fleet.getCommander().setPostId(postId);
        fleet.getCommander().setRankId(rankId);

        for (FleetMemberAPI member : fleet.getFleetData().getMembersListCopy()) {
            if (member.isCapital()) {
                member.setVariant(member.getVariant().clone(), false, false);

                member.getVariant().setSource(VariantSource.REFIT);
                member.getVariant().addTag(Tags.TAG_NO_AUTOFIT);
                member.getVariant().addTag(Tags.VARIANT_CONSISTENT_WEAPON_DROPS);
            }
        }

        market.getContainingLocation().addEntity(fleet);
        fleet.setFacing((float) Math.random() * 360f);
        // this will get overridden by the patrol assignment AI, depending on route-time elapsed etc
        fleet.setLocation(market.getPrimaryEntity().getLocation().x, market.getPrimaryEntity().getLocation().y);

        fleet.addScript(new PatrolAssignmentAIV4(fleet, route));

        //market.getContainingLocation().addEntity(fleet);
        //fleet.setLocation(market.getPrimaryEntity().getLocation().x, market.getPrimaryEntity().getLocation().y);

        if (custom.spawnFP <= 0) {
            custom.spawnFP = fleet.getFleetPoints();
        }

        return fleet;
    }

    public String getRouteSourceId() {
        return getMarket().getId() + "_" + "mkxthreat";
    }

    @Override
    public boolean isAvailableToBuild() {
        return false;
    }

    public boolean showWhenUnavailable() {
        return false;
    }

    @Override
    public boolean canImprove() {
        return false;
    }

    @Override
    public MarketCMD.RaidDangerLevel adjustCommodityDangerLevel(String commodityId, MarketCMD.RaidDangerLevel level) {
        return level.next();
    }

    @Override
    public MarketCMD.RaidDangerLevel adjustItemDangerLevel(String itemId, String data, MarketCMD.RaidDangerLevel level) {
        return level.next();
    }

}
