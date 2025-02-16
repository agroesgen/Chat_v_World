package storage;

import models.Unit;
import java.util.ArrayList;
import java.util.List;
import ui.GameUI;

public class UnitStorage {
    private final List<Unit> savedUnits = new ArrayList<>();
    private Unit currentUnit;

    public void setCurrentUnit(Unit unit) {
        this.currentUnit = unit;
    }

    public void saveCurrentUnit(String name) {
        if (currentUnit != null) {
            // Kopiere die aktuelle Einheit und speichere sie
            Unit clonedUnit = currentUnit.clone();   //!!!! Die gleiche Instanz wird überspielt und keine neue/eigenständige Abgespeichert
            clonedUnit.setName(name);
            savedUnits.add(clonedUnit);
        } else {
            System.out.println("Keine Einheit ausgewählt!");
        }
    }

    public void resetCurrentUnit() {
        this.currentUnit = null;
        System.out.println("Einheit zurückgesetzt! Jetzt kann eine neue Einheit ausgerüstet werden.");
    }

    public List<Unit> getSavedUnits() {
        return savedUnits;
    }
}
