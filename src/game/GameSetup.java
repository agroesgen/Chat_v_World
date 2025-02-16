package game;

import java.util.ArrayList;
import java.util.List;
import models.Armor;
import models.Item;
import models.Unit;
import models.Weapon;



public class GameSetup {
	
	private static final ItemLimitManager itemLimitManager = new ItemLimitManager();

    public static List<Unit> createUnits() {
        List<Unit> units = new ArrayList<>();
        units.add(new Unit("Viewer", 30, 6, 6, 2, 2, 1, 1, 9, 6, 5, "-", new ArrayList<>()));
        units.add(new Unit("Follower", 30, 5, 5, 2, 3, 1, 1, 8, 6, 7, "-", new ArrayList<>()));
        units.add(new Unit("T1 Sub", 30, 4, 4, 3, 3, 2, 1, 7, 5, 9, "Leader", new ArrayList<>()));
        units.add(new Unit("T2 Sub", 30, 3, 3, 4, 3, 3, 2, 6, 5, 10, "Leader", new ArrayList<>()));
        units.add(new Unit("T3 Sub", 30, 3, 3, 5, 3, 4, 3, 5, 5, 12, "Leader", new ArrayList<>()));
        return units;
    }

    public static List<Item> createItems() {
    	List<Item> items = new ArrayList<>();
        // Itemeigenschaften (name, cost, capacity, effect)
        // Waffeneigenschaften (name, cost, capacity, twoHanded, attacks, strength, damage, ap, range, effect)
        // Armor Eigenschaften (name, movementModifier, cost, capacity, saveBonus, toughnessBonus, woundsBonus)
        items.add(new Weapon("Schwert", 2000, 2, false, 1, 1, false, "1", 0, "Nah", "Keine"));
        items.add(new Weapon("Dolch", 2000, 1, false, 1, 0, false, "1", 1, "Nah", "Keine"));
        items.add(new Weapon("Hellebarde", 4500, 3, true, 1, 2, false, "d3", 1, "Nah", "Keine"));
        items.add(new Weapon("Bogen", 2000, 2, true, 1, 0, false, "1", 0, "100m", "BS+1 bei >60m"));
        items.add(new Weapon("Bolzenwerfer", 5000, 3, true, 1, 1, false, "1", 0, "100m", "Rapidfire 1"));
        items.add(new Weapon("Langbogen", 5000, 3, true, 1, 1, false, "1", 0, "180m", "Heavy, Indirect Fire"));
        items.add(new Weapon("Bombe", 3500, 3, false, 1, 4, true, "1", 0, "30m", "BLAST, 1x pro Einheit"));
        
        items.add(new Armor("Schild", 0, 2500, 2, 1, 0, 0,""));
        items.add(new Armor("Lederrüstung", 0, 2500, 1, 0, 1, 0, ""));
        items.add(new Armor("Kettenrüstung", 0, 5000, 2, 1, 1, 0, ""));
        items.add(new Armor("Plattenrüstung", -10, 9000, 3, 2, 2, 0, ""));
        
        items.add(new Item("Heiltrank",2500,2,"Heilt 1 Wunde"));
        items.add(new Item("Pferd",10000, -2,"Movement 60m, Charge 3d6, Gegen Fußsoldaten: S+2 & T+1 "));
        
        // Setzt für alle Items in der Liste ein Standardlimit von z. B. 5
        
        for (Item item : items) {
            itemLimitManager.setMaxLimit(item.getName(), 5);
        }
        return items;
    }
    
    public static ItemLimitManager getItemLimitManager() {
        return itemLimitManager;
    }
    


    // Hier können auch Methoden für das Erstellen von Einheiten und Items stehen

}

