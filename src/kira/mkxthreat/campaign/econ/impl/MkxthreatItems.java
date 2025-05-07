package kira.mkxthreat.campaign.econ.impl;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SpecialItemData;
import com.fs.starfarer.api.campaign.SpecialItemSpecAPI;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.InstallableIndustryItemPlugin;
import com.fs.starfarer.api.impl.campaign.econ.impl.BoostIndustryInstallableItemEffect;
import com.fs.starfarer.api.impl.campaign.econ.impl.ItemEffectsRepo;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.security.PublicKey;

public class MkxthreatItems {

    public static String MKXTHREATFRAGMENTHUB = "mkxthreatfragmenthub";
    public static float PRISTINE_NANOFORGE_QUALITY_BONUS = 0.5f;
    public static int PRISTINE_NANOFORGE_PROD = 4;
    public void putInfoForSpecialItems() {
        ItemEffectsRepo.ITEM_EFFECTS.put("mkxthreatfragmenthub", new BoostIndustryInstallableItemEffect(
                "mkxthreatfragmenthub", 3, 2) {

            public void apply(Industry industry) {
                super.apply(industry);
                industry.getMarket().getStats().getDynamic().getMod(Stats.PRODUCTION_QUALITY_MOD)
                        .modifyFlat("mkxthreatfragmenthub", PRISTINE_NANOFORGE_QUALITY_BONUS, Misc.ucFirst(spec.getName().toLowerCase()));
            }
            public void unapply(Industry industry) {
                super.unapply(industry);
                industry.getMarket().getStats().getDynamic().getMod(Stats.PRODUCTION_QUALITY_MOD).unmodifyFlat("mkxthreatfragmenthub");
            }
            protected void addItemDescriptionImpl(Industry industry, TooltipMakerAPI text, SpecialItemData data,
                                                  InstallableIndustryItemPlugin.InstallableItemDescriptionMode mode, String pre, float pad) {
                String heavyIndustry = "heavy industry ";
                if (mode == InstallableIndustryItemPlugin.InstallableItemDescriptionMode.MANAGE_ITEM_DIALOG_LIST) {
                    heavyIndustry = "";
                }
                text.addPara(pre + "Increases ship and weapon production quality by %s. " +
                                "Increases " + heavyIndustry + "production by %s units." +
                                " On habitable worlds, causes pollution which becomes permanent.",
                        pad, Misc.getHighlightColor(),
                        "" + (int) Math.round(PRISTINE_NANOFORGE_QUALITY_BONUS * 100f) + "%",
                        "" + (int) PRISTINE_NANOFORGE_PROD);
            }
        });



    }
    public void setSpecialItemNewIndustries(String specialItemID, String listOfAdditionalIndustries) {
        SpecialItemSpecAPI spec = Global.getSettings().getSpecialItemSpec(specialItemID);
        String prevParams = spec.getParams();
        if (prevParams.contains(listOfAdditionalIndustries)) return;
        spec.setParams(prevParams + "," + listOfAdditionalIndustries);
    }

}
