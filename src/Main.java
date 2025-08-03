import java.util.List;

import game.GameSetup;
import models.Item;
import models.Unit;
import ui.GameUI;

public class Main {
    public static void main(String[] args) {
        List<Unit> units = GameSetup.createUnits();
        List<Item> items = GameSetup.createItems();
        GameUI.main(args);

        // Beispiel: Eine Einheit mit Items ausrüsten
        Unit chosenUnit = units.get(0); // Erste Einheit auswählen
        System.out.println("Ausgewählte Einheit: " + chosenUnit);

        // Items hinzufügen
        chosenUnit.addItem(items.get(0)); // Schwert
        chosenUnit.addItem(items.get(2)); // Schild

        System.out.println("Nach Ausrüstung:");
        //System.out.println("Effektive Stärke: " + chosenUnit.getEffectiveStrength());
        System.out.println("Effektiver Rüstungswurf: " + chosenUnit.getEffectiveSave());

    }
}

// Entfernen Button soll ohne Auswahl einfach den letzten Eintrag entfernen
// Entfernen der gespeicherten Einheit macht die Items wieder verfügbar
// Geladene Einheiten unter gleichem Namen speicher => überschreiben