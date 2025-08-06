package game;
import java.util.HashMap;
import java.util.Map;
import models.Item;
import models.Unit;

public class ItemLimitManager {
    private final Map<String, Integer> maxLimits;  // Speichert das maximale Limit pro Item-Typ
    private final Map<String, Integer> equippedCounts; // Speichert, wie viele Items bereits ausgerüstet wurden

    public ItemLimitManager() {
        this.maxLimits = new HashMap<>();
        this.equippedCounts = new HashMap<>();
    }

    // Methode zur Festlegung des Limits für ein bestimmtes Item
    public void setMaxLimit(String itemName, int maxAmount) {
        maxLimits.put(itemName, maxAmount);
    }
    
    // Holt alle aktuellen Limits als Map (Item-Name → Maximal erlaubte Anzahl)
    public Map<String, Integer> getMaxLimits() {
        return maxLimits;
    }

public boolean addUnitPossible(Unit unit) {
    // Zähle, wie oft jedes Item im Unit-Equipment vorkommt
    Map<String, Integer> tempCounts = new HashMap<>();
    for (Item item : unit.getEquipment()) {
        String itemName = item.getName();
        tempCounts.put(itemName, tempCounts.getOrDefault(itemName, 0) + 1);
    }

    // Prüfe für jedes Item, ob das Limit überschritten würde
    for (Map.Entry<String, Integer> entry : tempCounts.entrySet()) {
        String itemName = entry.getKey();
        int toEquip = entry.getValue();
        int currentCount = equippedCounts.getOrDefault(itemName, 0);
        int maxLimit = maxLimits.getOrDefault(itemName, Integer.MAX_VALUE);

        if (currentCount + toEquip > maxLimit) {
            return false; // Limit würde überschritten
        }
    }

    // Wenn alles passt, rüste die Items tatsächlich aus
    for (Map.Entry<String, Integer> entry : tempCounts.entrySet()) {
        String itemName = entry.getKey();
        int toEquip = entry.getValue();
        equippedCounts.put(itemName, equippedCounts.getOrDefault(itemName, 0) + toEquip);
    }

    return true;
}

    // Prüft, ob das Item ausgerüstet werden darf
    public boolean canEquip(String itemName) {
        int currentCount = equippedCounts.getOrDefault(itemName, 0);
        int maxLimit = maxLimits.getOrDefault(itemName, Integer.MAX_VALUE); // Falls kein Limit gesetzt ist, unendlich

        return currentCount < maxLimit;
    }

    // Fügt ein Item hinzu, falls es erlaubt ist
    public void equipItem(String itemName) {
        equippedCounts.put(itemName, equippedCounts.getOrDefault(itemName, 0) + 1);
    }

    // Entfernt ein Item aus der Zählung
    public void unequipItem(String itemName) {
        int currentCount = equippedCounts.getOrDefault(itemName, 0);
        if (currentCount > 0) {
            equippedCounts.put(itemName, currentCount - 1);
        }
    }
    
    // Gibt zurück, wie viele Items noch verfügbar sind
    public int getRemaining(String itemName) {
        return maxLimits.getOrDefault(itemName, Integer.MAX_VALUE) - equippedCounts.getOrDefault(itemName, 0);
    }
}

