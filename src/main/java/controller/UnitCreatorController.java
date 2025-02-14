package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import models.Unit;
import models.Item;

import java.util.ArrayList;
import java.util.List;

public class UnitCreatorController {

    @FXML
    private Label storedUnitsLabel;
    @FXML
    private GridPane storedUnitsGrid;
    @FXML
    private Button storeUnitButton;
    @FXML
    private ComboBox<String> unitTypeComboBox;
    @FXML
    private TextField nameTextField;

    private List<Unit> storedUnits = new ArrayList<>();
    private static final int MAX_STORED_UNITS = 5;
    private Unit currentUnit; // Neue Klassenvariable für die aktuelle Einheit

    @FXML
    private void handleStoreUnit() {
        if (storedUnits.size() < MAX_STORED_UNITS) {
            // Aktuelle Einheit kopieren und speichern
            Unit unitToStore = getCurrentUnit(); // Methode zum Kopieren der aktuellen Einheit
            storedUnits.add(unitToStore);
            
            // UI aktualisieren
            updateStoredUnitsDisplay();
            
            // Formular zurücksetzen
            resetUnitForm();
            
            // Label aktualisieren
            storedUnitsLabel.setText(String.format("Gespeicherte Einheiten (%d/5)", storedUnits.size()));
            
            // Button deaktivieren wenn Maximum erreicht
            storeUnitButton.setDisable(storedUnits.size() >= MAX_STORED_UNITS);
        }
    }

    private Unit getCurrentUnit() {
        // Statt eine neue Unit zu erstellen, geben wir die aktuelle Unit zurück
        return currentUnit;
    }

    // Diese Methode sollte aufgerufen werden, wenn sich Werte im Formular ändern
    private void updateCurrentUnit() {
        // Statt Setter zu verwenden, erstellen wir eine neue Unit mit den aktuellen Werten
        currentUnit = new Unit(nameTextField.getText(), 5, 3, 3, 1, 7, 10);  // name, movement, strength, toughness, wounds, leadership, capacity
    }

    @FXML
    private void initialize() {
        // Initiale Unit erstellen
        currentUnit = new Unit("Neue Einheit", 5, 3, 3, 1, 7, 10);
    }

    private void updateStoredUnitsDisplay() {
        storedUnitsGrid.getChildren().clear();
        
        for (int i = 0; i < storedUnits.size(); i++) {
            Unit unit = storedUnits.get(i);
            VBox unitCard = createUnitCard(unit);
            
            // Position im Grid berechnen
            int row = i / 3;
            int col = i % 3;
            
            storedUnitsGrid.add(unitCard, col, row);
        }
    }

    private VBox createUnitCard(Unit unit) {
        VBox card = new VBox(5);
        card.getStyleClass().add("stored-unit-card");
        card.setPadding(new Insets(10));
        
        Label nameLabel = new Label(unit.getName());
        nameLabel.setStyle("-fx-font-weight: bold");
        
        VBox equipmentBox = new VBox(2);
        Label equipmentTitle = new Label("Ausrüstung:");
        equipmentTitle.setStyle("-fx-font-weight: bold");
        equipmentBox.getChildren().add(equipmentTitle);
        
        for (Item item : unit.getEquipment()) {
            Label eqLabel = new Label("- " + item.getName());
            equipmentBox.getChildren().add(eqLabel);
        }
        
        card.getChildren().addAll(nameLabel, equipmentBox);
        return card;
    }

    private void resetUnitForm() {
        // Formular auf Standardwerte zurücksetzen
        unitTypeComboBox.setValue(null);
        nameTextField.clear();
        // ... weitere Felder zurücksetzen
    }
} 