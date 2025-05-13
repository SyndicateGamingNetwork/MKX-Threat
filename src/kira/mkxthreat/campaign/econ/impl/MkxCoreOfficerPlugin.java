package kira.mkxthreat.campaign.econ.impl;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.CommoditySpecAPI;
import com.fs.starfarer.api.characters.FullName;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.impl.campaign.BaseAICoreOfficerPluginImpl;
import com.fs.starfarer.api.impl.campaign.ids.Personalities;
import com.fs.starfarer.api.impl.campaign.ids.Ranks;
import com.fs.starfarer.api.impl.campaign.ids.Skills;
import kira.mkxthreat.ids.MkxthreatStrings;

import java.util.Random;

public class MkxCoreOfficerPlugin extends BaseAICoreOfficerPluginImpl {

    public static int MKX_CORE_POINTS = 0;
    public static float MKX_CORE_MULT = 4;

    public PersonAPI createPerson(String aiCoreId, String factionId, Random random) {
        if (random == null) random = new Random();

        PersonAPI person = Global.getFactory().createPerson();
        person.setFaction(factionId);
        person.setAICoreId(aiCoreId);

        CommoditySpecAPI spec = Global.getSettings().getCommoditySpec(aiCoreId);
        boolean jdp_omega = MkxthreatStrings.THREAT_CORE.equals(aiCoreId);

        person.getStats().setSkipRefresh(true);

        person.setName(new FullName("X", "MK", FullName.Gender.ANY));
        int points = 0;
        float mult = 1f;
        if (jdp_omega) {
            person.setPortraitSprite("graphics/portraits/characters/X_MK.png");
            person.getStats().setLevel(9);
            person.getStats().setSkillLevel(Skills.HELMSMANSHIP, 2);
            person.getStats().setSkillLevel(Skills.TARGET_ANALYSIS, 2);
            person.getStats().setSkillLevel(Skills.IMPACT_MITIGATION, 2);
            person.getStats().setSkillLevel(Skills.FIELD_MODULATION, 2);
            person.getStats().setSkillLevel(Skills.GUNNERY_IMPLANTS, 2);
            person.getStats().setSkillLevel(Skills.COMBAT_ENDURANCE, 2);
            person.getStats().setSkillLevel(Skills.DAMAGE_CONTROL, 2);
            person.getStats().setSkillLevel(Skills.POINT_DEFENSE, 2);
        }

        points = MKX_CORE_POINTS;
        mult = MKX_CORE_MULT;

        if (points != 0) {
            person.getMemoryWithoutUpdate().set(AUTOMATED_POINTS_VALUE, points);
        }
        person.getMemoryWithoutUpdate().set(AUTOMATED_POINTS_MULT, mult);

        person.setPersonality(Personalities.RECKLESS);
        person.setRankId(Ranks.SPACE_CAPTAIN);

        person.setPostId(null);

        person.getStats().setSkipRefresh(false);

        return person;
    }

}
