package kira.mkxthreat.campaign.ids;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.PersonImportance;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.characters.FullName;
import com.fs.starfarer.api.characters.ImportantPeopleAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.impl.campaign.ids.*;
import kira.mkxthreat.ids.MkxthreatStrings;

public class Mkxthreat_People {

    // Hel Inhabitants
    public static final String X_MK = "mkxthreat_x_mk";

    public static PersonAPI getPerson(final String id) {
        return Global.getSector().getImportantPeople().getPerson(id);
    }

    public static void create() {
        createFactionLeaders();
    }

    public static void createFactionLeaders() {
        ImportantPeopleAPI ip = Global.getSector().getImportantPeople();
        MarketAPI market = null;

        market =  Global.getSector().getEconomy().getMarket("helmarket");
        if (market != null) {
            PersonAPI person = Global.getFactory().createPerson();
            person.setId(X_MK);
            person.setFaction(MkxthreatStrings.MKXTHREAT);
            person.setGender(FullName.Gender.ANY);
            person.setRankId(Ranks.FACTION_LEADER);
            person.setPostId(Ranks.POST_FACTION_LEADER);
            person.setImportance(PersonImportance.VERY_HIGH);
            person.getName().setFirst("X");
            person.getName().setLast("MK");
            person.setPortraitSprite(Global.getSettings().getSpriteName("characters", "mkxthreat_x_mk"));
            person.getStats().setSkillLevel(Skills.SPACE_OPERATIONS, 3);
            person.getStats().setSkillLevel(Skills.PLANETARY_OPERATIONS, 3);
            person.getStats().setSkillLevel(Skills.INDUSTRIAL_PLANNING, 3);
            person.getStats().setSkillLevel(Skills.HELMSMANSHIP, 3);
            person.getStats().setSkillLevel(Skills.TARGET_ANALYSIS, 3);
            person.getStats().setSkillLevel(Skills.SHIELD_MODULATION, 3);
            person.getStats().setSkillLevel(Skills.MISSILE_SPECIALIZATION, 3);
            person.getStats().setSkillLevel(Skills.SYSTEMS_EXPERTISE, 3);
            person.getStats().setSkillLevel(Skills.ENERGY_WEAPON_MASTERY, 2);
            person.getStats().setSkillLevel(Skills.GUNNERY_IMPLANTS, 3);
            person.getStats().setLevel(15);
            person.setPersonality(Personalities.RECKLESS);

            market.setAdmin(person);
            market.getCommDirectory().addPerson(person, 0);
            market.addPerson(person);
            ip.addPerson(person);
        }

    }
}
