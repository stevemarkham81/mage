package mage.sets;

import mage.cards.ExpansionSet;
import mage.constants.Rarity;
import mage.constants.SetType;

/**
 *
 * @author spjspj
 */
public class HappyHolidays extends ExpansionSet {

    private static final HappyHolidays instance = new HappyHolidays();

    public static HappyHolidays getInstance() {
        return instance;
    }

    private HappyHolidays() {
        super("Happy Holidays", "HHO", ExpansionSet.buildDate(2006, 12, 31), SetType.JOKESET);

        cards.add(new SetCardInfo("Snow Mercy", 10, Rarity.RARE, mage.cards.s.SnowMercy.class));
    }
}
