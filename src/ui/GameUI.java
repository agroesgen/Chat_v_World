package ui;

import game.GameSetup;
import models.Item;
import models.Unit;
import models.Weapon;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class GameUI extends Application {
    private List<Unit> units = GameSetup.createUnits();
    private List<Item> items = GameSetup.createItems();
    private Unit selectedUnit;
    private Label unitStatsLabel;
    private Label usedCapacityLabel;
    private ListView<Item> itemListView;
    private ListView<Item> equippedItemsView;
    private ListView<Weapon> weaponsView;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Einheiten-Ausrüstung");

        // Dropdown zur Auswahl der Einheit
        ComboBox<Unit> unitComboBox = new ComboBox<>();
        unitComboBox.getItems().addAll(units);
        unitComboBox.setOnAction(e -> {
            selectedUnit = unitComboBox.getValue();
            updateUnitStats();
            updateUsedCapacity();
            equippedItemsView.getItems().setAll(selectedUnit.getEquipment());
            weaponsView.getItems().setAll(selectedUnit.getWeapons());
        });

        // Label zur Anzeige der aktuellen Einheit-Attribute
        unitStatsLabel = new Label("Wähle eine Einheit aus");
        usedCapacityLabel = new Label("Used Capacity: 0");
        

        // Liste der verfügbaren Gegenstände
        itemListView = new ListView<>();
        itemListView.getItems().addAll(items);

        // Liste der aktuell ausgerüsteten Gegenstände
        equippedItemsView = new ListView<>();

        // Liste der Waffen der Einheit
        weaponsView = new ListView<>();

        // Button: Gegenstand ausrüsten
        Button equipButton = new Button("Ausrüsten");
        equipButton.setOnAction(e -> {
            Item selectedItem = itemListView.getSelectionModel().getSelectedItem();
            if (selectedUnit != null && selectedItem != null) {
                boolean success = selectedUnit.addItem(selectedItem);
                if (success) {
                    updateUnitStats();
                    updateUsedCapacity();
                    equippedItemsView.getItems().setAll(selectedUnit.getEquipment());
                    weaponsView.getItems().setAll(selectedUnit.getWeapons());
                } else {
                    showAlert("Kapazitätsgrenze erreicht!", "Diese Einheit kann keinen weiteren Gegenstand tragen.");
                }
            }
        });
        
     // Button: Gegenstand entfernen
        Button removeButton = new Button("Entfernen");
        removeButton.setOnAction(e -> {
            Item selectedItem = equippedItemsView.getSelectionModel().getSelectedItem();
            if (selectedUnit != null && selectedItem != null) {
                selectedUnit.removeItem(selectedItem);
                updateUnitStats();
                updateUsedCapacity();
                equippedItemsView.getItems().setAll(selectedUnit.getEquipment());
                weaponsView.getItems().setAll(selectedUnit.getWeapons());
            }
        });

        // Layouts
        VBox unitBox = new VBox(new Label("Einheit wählen:"), unitComboBox, unitStatsLabel);
        VBox itemsBox = new VBox(new Label("Verfügbare Items:"), itemListView, equipButton);
        VBox equippedBox = new VBox(new Label("Ausgerüstete Items:"), equippedItemsView, removeButton, usedCapacityLabel);
        VBox weaponsBox = new VBox(new Label("Waffen der Einheit:"), weaponsView);

        HBox root = new HBox(20, unitBox, itemsBox, equippedBox, weaponsBox);
        root.setPadding(new javafx.geometry.Insets(15));

        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.show();
    }

    private void updateUnitStats() {
        if (selectedUnit != null) {
            unitStatsLabel.setText("Name: " + selectedUnit.getName() +
                    "\nMovement: " + selectedUnit.getEffectiveMovement() +
                    "\nStrength: " + selectedUnit.getStrength()+
                    "\nToughness: " + selectedUnit.getEffectiveToughness() +
                    "\nWounds: " + selectedUnit.getEffectiveWounds() +
                    "\nSave: " + selectedUnit.getEffectiveSave() +
                    "\nLeadership: " + selectedUnit.getEffectiveLeadership());
        }
    }
    
    private void updateUsedCapacity() {
        if (selectedUnit != null) {
            usedCapacityLabel.setText("Used Capacity: " + selectedUnit.getUsedCapacity() + " / " + + selectedUnit.getCapacity());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
