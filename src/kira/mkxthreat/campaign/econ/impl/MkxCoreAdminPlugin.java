package kira.mkxthreat.campaign.econ.impl;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.AICoreAdminPlugin;
import com.fs.starfarer.api.characters.FullName;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.impl.campaign.AICoreAdminPluginImpl;
import com.fs.starfarer.api.impl.campaign.ids.Ranks;
import com.fs.starfarer.api.impl.campaign.ids.Skills;

import static kira.mkxthreat.ids.MkxthreatStrings.THREAT_CORE;

public class MkxCoreAdminPlugin extends AICoreAdminPluginImpl implements AICoreAdminPlugin {

    public PersonAPI createPerson(String aiCoreId, String factionId, long seed) {
        PersonAPI person = Global.getFactory().createPerson();
        person.setFaction(factionId);
        person.setAICoreId(THREAT_CORE);
        person.setName(new FullName("X", "MK", FullName.Gender.ANY));
        person.setPortraitSprite("graphics/portraits/volcore.png");
        person.setRankId((String)null);
        person.setPostId(Ranks.POST_ADMINISTRATOR);
        person.getStats().setSkillLevel(Skills.INDUSTRIAL_PLANNING, 1F);
        person.getStats().setSkillLevel(Skills.HYPERCOGNITION, 1F);
        return person;
    }
}
