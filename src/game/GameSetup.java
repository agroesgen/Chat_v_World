package game;

import models.Unit;
import models.Item;
import models.Weapon;
import models.Armor;
import java.util.ArrayList;
import java.util.List;

public class GameSetup {
    public static List<Unit> createUnits() {
        List<Unit> units = new ArrayList<>();
        units.add(new Unit("Viewer", 30, 6, 6, 2, 2, 1, 1, 9, 6, 5, "-"));
        units.add(new Unit("Follower", 30, 5, 5, 2, 3, 1, 1, 8, 6, 7, "-"));
        units.add(new Unit("T1 Sub", 30, 4, 4, 3, 3, 2, 1, 7, 5, 9, "Leader"));
        units.add(new Unit("T2 Sub", 30, 3, 3, 4, 3, 3, 2, 6, 5, 10, "Leader"));
        units.add(new Unit("T3 Sub", 30, 3, 3, 5, 3, 4, 3, 5, 5, 12, "Leader"));
        return units;
    }

    public static List<Item> createItems() {
        List<Item> items = new ArrayList<>();
        // Item Eigenschaften (name, movementModifier, cost, capacity, twoHanded?, attacks, strength, damage, ap, range, effct)
        items.add(new Weapon("Schwert", 2000, 2, false, 1, 1, 1, 0, "Nah", "Keine"));
        items.add(new Weapon("Bogen", 2000, 2, true, 1, 0, 1, 0, "100m", "BS+1 bei >60m"));
        items.add(new Armor("Schild", 0, 2500, 2, 1, 0, 0));
        items.add(new Armor("Lederrüstung", 0, 2500, 1, 0, 1, 0));
        return items;
    }
}

