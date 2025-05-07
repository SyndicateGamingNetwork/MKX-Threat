package kira.mkxthreat.campaign.rulecmd;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.RepLevel;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.impl.campaign.ids.Ranks;
import com.fs.starfarer.api.impl.campaign.intel.BaseMissionIntel;
import com.fs.starfarer.api.impl.campaign.intel.FactionCommissionIntel;
import com.fs.starfarer.api.impl.campaign.intel.BaseMissionIntel.MissionState;
import com.fs.starfarer.api.impl.campaign.rulecmd.BaseCommandPlugin;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import kira.mkxthreat.utility.MkxthreatUtils;
import exerelin.campaign.SectorManager;
import java.awt.Color;
import java.util.List;
import java.util.Map;

    public class MkxthreatCommission extends BaseCommandPlugin {
    public static RepLevel COMMISSION_REQ;
    protected CampaignFleetAPI playerFleet;
    protected SectorEntityToken entity;
    protected FactionAPI playerFaction;
    protected FactionAPI entityFaction;
    protected TextPanelAPI text;
    protected OptionPanelAPI options;
    protected CargoAPI playerCargo;
    protected MemoryAPI memory;
    protected InteractionDialogAPI dialog;
    protected Map<String, MemoryAPI> memoryMap;
    protected PersonAPI person;
    protected FactionAPI faction;
    protected boolean offersCommissions;

    public boolean execute(String ruleId, InteractionDialogAPI dialog, List<Misc.Token> params, Map<String, MemoryAPI> memoryMap) {
        this.dialog = dialog;
        this.memoryMap = memoryMap;
        String command = params.get(0).getString(memoryMap);
        if (command == null) {
            return false;
        } else {
            this.memory = getEntityMemory(memoryMap);
            this.entity = dialog.getInteractionTarget();
            this.text = dialog.getTextPanel();
            this.options = dialog.getOptionPanel();
            this.playerFleet = Global.getSector().getPlayerFleet();
            this.playerCargo = this.playerFleet.getCargo();
            this.playerFaction = Global.getSector().getPlayerFaction();
            this.entityFaction = this.entity.getFaction();
            this.person = dialog.getInteractionTarget().getActivePerson();
            this.faction = Global.getSector().getFaction("mkxthreat");
            if (command.equals("printRequirements")) {
                this.printRequirements();
            } else {
                if (command.equals("playerMeetsCriteria")) {
                    return this.playerMeetsCriteria();
                }

                if (command.equals("printInfo")) {
                    this.printInfo();
                } else {
                    if (command.equals("hasFactionCommission")) {
                        return this.hasFactionCommission();
                    }

                    if (command.equals("hasOtherCommission")) {
                        if (this.hasOtherCommission()) {
                            this.memory.set("$theOtherCommissionFaction", Misc.getCommissionFaction().getDisplayNameWithArticle(), 0.0F);
                            return true;
                        }

                        return false;
                    }

                    if (command.equals("accept")) {
                        this.accept();
                    } else if (command.equals("resign")) {
                        this.resign();
                    } else {
                        if (command.equals("personCanGiveCommission")) {
                            return this.personCanGiveCommission();
                        }

                        if (command.equals("mkxthreatRelationMinimum")) {
                            float secondCommand = params.get(1).getFloat(memoryMap);
                            FactionAPI uaf = Global.getSector().getFaction("mkxthreat");
                            return (float)uaf.getRelToPlayer().getRepInt() >= secondCommand;
                        }

                        if (command.equals("mkxthreatAccessEligibility")) {
                            return this.checkmkxthreatEligibility();
                        }

                        if (command.equals("mkxthreatAccessEligibilityExclusive")) {
                            return this.checkmkxthreatEligibilityExclusive();
                        }

                        if (command.equals("IsRandomSector")) {
                            return this.checkRandomSector();
                        }
                    }
                }
            }

            return true;
        }
    }

    protected boolean checkRandomSector() {
        boolean haveNexerelin = Global.getSettings().getModManager().isModEnabled("nexerelin");
        return haveNexerelin && !SectorManager.getManager().isCorvusMode();
    }

    protected boolean checkmkxthreatEligibilityExclusive() {
        String commissioningFaction = Misc.getCommissionFactionId();
        if (this.faction.getRelToPlayer().getRepInt() < 20) {
            return false;
        } else {
            return commissioningFaction != null && commissioningFaction.equals("mkxthreat");
        }
    }

    protected boolean checkmkxthreatEligibility() {
        if (this.faction.getRelToPlayer().getRepInt() < 20) {
            return false;
        } else {
            return MkxthreatUtils.isPlayerCommissionedOrAllianced();
        }
    }

    protected boolean hasFactionCommission() {
        return this.faction.getId().equals(Misc.getCommissionFactionId());
    }

    protected boolean hasOtherCommission() {
        return Misc.getCommissionFactionId() != null && !this.hasFactionCommission();
    }

    protected boolean personCanGiveCommission() {
        if (this.person == null) {
            return false;
        } else if (this.person.getFaction().isPlayerFaction()) {
            return false;
        } else {
            return Ranks.POST_BASE_COMMANDER.equals(this.person.getPostId()) || Ranks.POST_STATION_COMMANDER.equals(this.person.getPostId()) || Ranks.POST_ADMINISTRATOR.equals(this.person.getPostId()) || Ranks.POST_OUTPOST_COMMANDER.equals(this.person.getPostId());
        }
    }

    protected void resign() {
        FactionCommissionIntel intel = Misc.getCommissionIntel();
        BaseMissionIntel.MissionResult result = intel.createResignedCommissionResult(true, true, this.dialog);
        intel.setMissionResult(result);
        intel.setMissionState(MissionState.ABANDONED);
        intel.endMission(this.dialog);
    }

    protected void accept() {
        if (Misc.getCommissionFactionId() == null) {
            FactionCommissionIntel intel = new FactionCommissionIntel(this.faction);
            intel.missionAccepted();
            intel.sendUpdate(FactionCommissionIntel.UPDATE_PARAM_ACCEPTED, this.dialog.getTextPanel());
            intel.makeRepChanges(this.dialog);
        }

    }

    protected void printInfo() {
        TooltipMakerAPI info = this.dialog.getTextPanel().beginTooltip();
        FactionCommissionIntel temp = new FactionCommissionIntel(this.faction);
        Color h = Misc.getHighlightColor();
        Color g = Misc.getGrayColor();
        float pad = 3.0F;
        float opad = 10.0F;
        info.setParaSmallInsignia();
        int stipend = (int)temp.computeStipend();
        info.addPara("At your experience level, you would receive a %s monthly stipend, as well as a modest bounty for destroying enemy ships.", 0.0F, h, Misc.getDGSCredits((float)stipend));
        List<FactionAPI> hostile = temp.getHostileFactions();
        if (hostile.isEmpty()) {
            info.addPara(Misc.ucFirst(this.faction.getDisplayNameWithArticle()) + " is not currently hostile to any major factions.", 0.0F);
        } else {
            info.addPara(Misc.ucFirst(this.faction.getDisplayNameWithArticle()) + " is currently hostile to:", opad);
            info.setParaFontDefault();
            info.setBulletedListMode("      ");
            float initPad = opad;

            for(FactionAPI other : hostile) {
                info.addPara(Misc.ucFirst(other.getDisplayName()), other.getBaseUIColor(), initPad);
                initPad = 3.0F;
            }

            info.setBulletedListMode(null);
        }

        this.dialog.getTextPanel().addTooltip();
    }

    protected boolean playerMeetsCriteria() {
        return this.person.getRelToPlayer().getRepInt() > 30 && this.faction.getRelToPlayer().getRepInt() >= 20;
    }

    protected void printRequirements() {
        this.dialog.getTextPanel().setFontSmallInsignia();
        String requireText = "Requires 31 or more standing with " + this.person.getNameString();
        requireText = requireText + " and requires atleast 20 standing with the faction";
        String currentText = "Current standing with " + this.person.getNameString() + " is " + this.person.getRelToPlayer().getRepInt() + "1";
        this.dialog.getTextPanel().addParagraph(requireText);
        this.dialog.getTextPanel().addParagraph(currentText);
        this.dialog.getTextPanel().setFontInsignia();
    }

    static {
        COMMISSION_REQ = RepLevel.FAVORABLE;
    }
}