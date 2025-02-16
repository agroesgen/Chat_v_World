package ui;

import game.GameSetup;
import models.Item;
import models.Unit;
import models.Weapon;
import storage.UnitStorage;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameUI extends Application {
    private List<Unit> units = GameSetup.createUnits();
    private List<Item> items = GameSetup.createItems();
    private static Unit selectedUnit;
    private Label unitStatsLabel;
    private Label [] savedUnitStatsLabel = new Label [5];
    private Label usedCapacityLabel;
    private ListView<Item> itemListView;
    private ListView<Item> equippedItemsView;
    private ListView<Weapon> weaponsView;
    private ListView<Unit> savedUnitsView;
    private VBox itemsBox;
    private UnitStorage unitStorage = new UnitStorage();
    
   // private ComboBox<Unit> [] saveSlotComboBoxes = new ComboBox<>()[5];
        
    private ComboBox<Unit> saveSlotComboBox1= new ComboBox<>();
    private ComboBox<Unit> saveSlotComboBox2= new ComboBox<>();
    private ComboBox<Unit> saveSlotComboBox3= new ComboBox<>();
    private ComboBox<Unit> saveSlotComboBox4= new ComboBox<>();
    private ComboBox<Unit> saveSlotComboBox5= new ComboBox<>();

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
        
        for (int i=0; i<5; i++){
        	savedUnitStatsLabel[i] = new Label("");
        }
        
		// Dropdown Auswahl der gespeicherten Einheiten
    	saveSlotComboBox1.setOnAction(e -> {
    		savedUnitStatsLabel[0].setText(unitStats(saveSlotComboBox1.getValue()).getText());
    		System.out.println(savedUnitStatsLabel[0].getText());
    	});
    	
    	saveSlotComboBox2.setOnAction(e -> {
    		savedUnitStatsLabel[1].setText(unitStats(saveSlotComboBox2.getValue()).getText());
    		System.out.println(savedUnitStatsLabel[0].getText());
    	});
    	
    	saveSlotComboBox3.setOnAction(e -> {
    		savedUnitStatsLabel[2].setText(unitStats(saveSlotComboBox3.getValue()).getText());
    		System.out.println(savedUnitStatsLabel[0].getText());
    	});
    	
    	saveSlotComboBox4.setOnAction(e -> {
    		savedUnitStatsLabel[3].setText(unitStats(saveSlotComboBox4.getValue()).getText());
    		System.out.println(savedUnitStatsLabel[0].getText());
    	});
    	
    	saveSlotComboBox5.setOnAction(e -> {
    		savedUnitStatsLabel[4].setText(unitStats(saveSlotComboBox5.getValue()).getText());
    		System.out.println(savedUnitStatsLabel[0].getText());
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
        
        // Button: Einheit speichern
        Button saveUnitButton = new Button("Einheit speichern");
        saveUnitButton.setOnAction(e -> {
            if (selectedUnit != null) {
                // Einheit speichern
            	
            	TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Einheit speichern");
                dialog.setHeaderText("Einheitenname eingeben:");
                dialog.setContentText("Name:");

                dialog.showAndWait().ifPresent(name -> {
                    if (!name.trim().isEmpty()) {
                    	unitStorage.setCurrentUnit(selectedUnit);
                    	unitStorage.saveCurrentUnit(name);

	                // Liste der gespeicherten Einheiten aktualisieren
	                savedUnitsView.getItems().setAll(unitStorage.getSavedUnits());
	
	                // Einheit zurücksetzen
	                selectedUnit.getEquipment().clear();
	                selectedUnit = unitComboBox.getValue();
	                updateUnitStats();
	                updateSafeSlotComboBoxes();
	                weaponsView.getItems().setAll(selectedUnit.getWeapons());
	                equippedItemsView.getItems().setAll(selectedUnit.getEquipment());
	                
                    }	else {
                    		showAlert("Fehler", "Bitte einen gültigen Namen eingeben.");
                    	}
                });
            }
        });

        // Button: Gespeicherte Einheit laden
        Button loadUnitButton = new Button("Einheit laden");
        loadUnitButton.setOnAction(e -> {
            Unit selectedSavedUnit = savedUnitsView.getSelectionModel().getSelectedItem();
            if (selectedSavedUnit != null) {
            	selectedUnit = selectedSavedUnit;
            	
                updateUnitStats();
                updateUsedCapacity();
                equippedItemsView.getItems().setAll(selectedSavedUnit.getEquipment());
                weaponsView.getItems().setAll(selectedUnit.getWeapons());
                //unitComboBox.setValue(selectedUnit);
            }
        });
        
     // Button: Einheit zurücksetzen
        Button clearUnitButton = new Button("Einheit zurücksetzen");
        clearUnitButton.setOnAction(e -> {
            if (selectedUnit != null) {
                selectedUnit.clearEquipment();
                equippedItemsView.getItems().clear();
                updateUnitStats();
                updateUsedCapacity();
            }
        });
        
     // Liste der gespeicherten Einheiten
        savedUnitsView = new ListView<>();

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
                    showAlert("Kapazitätsgrenze oder Gegenstandslimit erreicht!", "Diese Einheit kann keinen weiteren Gegenstand tragen.");
                }
            }
            updateItemListView();
        });
        
     // Button: Item-Limit anpassen
        Button editLimitsButton = new Button("Item-Limits ändern");
        editLimitsButton.setOnAction(e -> openItemLimitEditor());
        
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
        
        VBox unitBox = new VBox(new Label("Einheit wählen:"), unitComboBox, unitStatsLabel, clearUnitButton);
        itemsBox = new VBox(new Label("Verfügbare Items:"), itemListView, equipButton);
        VBox equippedBox = new VBox(new Label("Ausgerüstete Items:"), equippedItemsView, removeButton, saveUnitButton, usedCapacityLabel);
        VBox weaponsBox = new VBox(new Label("Waffen der Einheit:"), weaponsView);
        VBox controlsBox = new VBox(editLimitsButton);        
        VBox savedUnitsBox = new VBox(new Label("Gespeicherte Einheiten:"), savedUnitsView, loadUnitButton);

        VBox saveSlot1 = new VBox(new Label("SaveSlot 1"), saveSlotComboBox1, savedUnitStatsLabel[0]);
        VBox saveSlot2 = new VBox(new Label("SaveSlot 2"), saveSlotComboBox2, savedUnitStatsLabel[1]);
        VBox saveSlot3 = new VBox(new Label("SaveSlot 3"), saveSlotComboBox3, savedUnitStatsLabel[2]);
        VBox saveSlot4 = new VBox(new Label("SaveSlot 4"), saveSlotComboBox4, savedUnitStatsLabel[3]);
        VBox saveSlot5 = new VBox(new Label("SaveSlot 5"), saveSlotComboBox5, savedUnitStatsLabel[4]);
        
        HBox controlsLayout = new HBox(20, unitBox, itemsBox, equippedBox, weaponsBox, controlsBox, savedUnitsBox);
        controlsLayout.setPadding(new javafx.geometry.Insets(15));
        HBox overview = new HBox (20, saveSlot1, saveSlot2, saveSlot3, saveSlot4, saveSlot5);
        overview.setPadding(new javafx.geometry.Insets(15));
        
        VBox root = new VBox (20, controlsLayout, overview);

        primaryStage.setScene(new Scene(root, 1000, 800));
        primaryStage.show();
    }

    private void updateUnitStats() {
        if (selectedUnit != null) {
            unitStatsLabel.setText(unitStats(selectedUnit).getText());
        }
    }
    
    private Label unitStats(Unit unit) {
    	Label unitStats = new Label("");
    	
    	if (unit != null) {
    		unitStats.setText("Name: " + unit.getName() + 
    				"\nMovement: " + unit.getEffectiveMovement() +
                    "\nWS: " + unit.getWs() +
                    "\nBS: " + unit.getBs() +
                    "\nStrength: " + unit.getStrength()+
                    "\nToughness: " + unit.getEffectiveToughness() +
                    "\nWounds: " + unit.getEffectiveWounds() +
                    "\nSave: " + unit.getEffectiveSave() +
                    "\nLeadership: " + unit.getEffectiveLeadership());
    	}     
    	return unitStats;
    }
    
    private void updateSafeSlotComboBoxes() {
    	
    	saveSlotComboBox1.getItems().setAll(unitStorage.getSavedUnits());    
    	saveSlotComboBox2.getItems().setAll(unitStorage.getSavedUnits());    
    	saveSlotComboBox3.getItems().setAll(unitStorage.getSavedUnits());    
    	saveSlotComboBox4.getItems().setAll(unitStorage.getSavedUnits());    
    	saveSlotComboBox5.getItems().setAll(unitStorage.getSavedUnits());    
    	
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
    
    private void openItemLimitEditor() {
        Stage limitStage = new Stage();
        limitStage.setTitle("Item-Limits bearbeiten");

        VBox layout = new VBox(10);
        layout.getChildren().add(new Label("Item Limits bearbeiten:"));
        layout.setPadding(new javafx.geometry.Insets(15));
        
        List<TextField> textFields = new ArrayList<>();
        
        //Label itemLabel = new Label("");
        //TextField limitField = new TextField("");
        
        for (Item item : items) {
            HBox row = new HBox(10);
            Label itemNameLabel = new Label(item.getName());
            
            TextField limitField = new TextField();
            limitField.setText(String.valueOf(GameSetup.getItemLimitManager().getMaxLimits().get(item.getName()))); // Aktuelles Limit setzen
            textFields.add(limitField); // Textfeld zur Liste hinzufügen
            
            row.getChildren().addAll(itemNameLabel, limitField);
            layout.getChildren().add(row);
        }

        Button saveButton = new Button("Speichern");
        layout.getChildren().add(saveButton);
        
        saveButton.setOnAction(e -> {
            for (int i = 0; i < items.size(); i++) {
                String itemName = items.get(i).getName();
                try {
                    int newLimit = Integer.parseInt(textFields.get(i).getText());
                    GameSetup.getItemLimitManager().setMaxLimit(itemName, newLimit);
                } catch (NumberFormatException ex) {
                    showAlert("Fehler", "Ungültige Eingabe für " + itemName);
                }
            }           
         // Neues Label erstellen
            updateItemListView();

            
            showAlert("Erfolg", "Alle Limits wurden gespeichert!");
        });

        Scene scene = new Scene(layout, 400, 800);
        limitStage.setScene(scene);
        limitStage.show();
    }
    
    private void updateItemListView() {
        itemListView.getItems().clear();
        itemListView.getItems().addAll(items);
    	
    	ListView<Item> neueItemListView = new ListView<>();
    
	    neueItemListView = itemListView;
	    // Altes Element ersetzen
	    int index = itemsBox.getChildren().indexOf(itemListView);
	    if (index != -1) {
	        itemsBox.getChildren().set(index, neueItemListView);
	    }
    }
    
    public static Unit getSelectedUnit() {
    	return selectedUnit;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
