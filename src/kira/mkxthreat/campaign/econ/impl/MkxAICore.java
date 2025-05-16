package kira.mkxthreat.campaign.econ.impl;

import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.Industries;
import exerelin.world.ExerelinProcGen;
import exerelin.world.industry.bonus.BonusGen;
import kira.mkxthreat.ids.MkxthreatStrings;

public class MkxAICore extends BonusGen {

    public MkxAICore(String... industryIds) {
        super(industryIds);
    }

    @Override
    public boolean canApply(Industry ind, ExerelinProcGen.ProcGenEntity entity) {
        if (ind.getAICoreId() != null && ind.getAICoreId().equals(MkxthreatStrings.THREAT_CORE))
            return false;
        return true;
    }

    @Override
    public void apply(Industry ind, ExerelinProcGen.ProcGenEntity entity) {
        if (ind.getSpec().hasTag(Industries.TAG_STATION))
            ind.setAICoreId(MkxthreatStrings.THREAT_CORE);
        super.apply(ind, entity);
    }
}
