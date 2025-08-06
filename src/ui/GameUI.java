package ui;

import game.GameSetup;
import game.ItemLimitManager;
import models.Item;
import models.Unit;
import models.Weapon;
import storage.UnitStorage;
import storage.PdfExporter;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameUI extends Application {
    private static final int UNIT_AMOUNT = 10;

    ItemLimitManager limitManager = GameSetup.getItemLimitManager();
    
    private Label unitStatsLabel;
    private TextArea[] savedUnitStatsTextArea = new TextArea[UNIT_AMOUNT];
    private ComboBox<Unit>[] saveSlotComboBoxes = new ComboBox[UNIT_AMOUNT];
    private Spinner<Integer>[] quantitySpinner = new Spinner[UNIT_AMOUNT];

    private List<Unit> units = GameSetup.createUnits();
    private List<Item> items = GameSetup.createItems();
    private static Unit selectedUnit;
    private Label usedCapacityLabel;
    private ListView<Item> itemListView;
    private ListView<Item> equippedItemsView;
    private ListView<Weapon> weaponsView;
    private ListView<Unit> savedUnitsView;
    private VBox itemsBox;
    private UnitStorage unitStorage = new UnitStorage();

    boolean isUpdatingSpinner = false;

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


        // Initialisiere ComboBoxen und Labels in einer Schleife
        for (int i = 0; i < UNIT_AMOUNT; i++) {
            saveSlotComboBoxes[i] = new ComboBox<>();
            savedUnitStatsTextArea[i] = new TextArea("");
            
            int index = i; // Notwendig für Lambda-Ausdruck (final oder effectively final)
            saveSlotComboBoxes[i].setOnAction(e -> {
                savedUnitStatsTextArea[index].setText(unitStats(saveSlotComboBoxes[index].getValue()).getText() + "\nItems: " + saveSlotComboBoxes[index].getValue().getItems() + "\nArmor: " + saveSlotComboBoxes[index].getValue().getArmors());
            });
        }

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
                updateItemListView();
                equippedItemsView.getItems().setAll(selectedUnit.getEquipment());
                weaponsView.getItems().setAll(selectedUnit.getWeapons());
            }
        });
        
     // Button: Item-Limit anpassen
        Button exportButton = new Button("PDF Exportieren");
        PdfExporter exporter = new PdfExporter();
        exportButton.setOnAction(e -> exporter.exportUnitWithEquipment(selectedUnit, selectedUnit.getWeapons(),selectedUnit.getInventory(),selectedUnit.getName()+".pdf"));

        Button removeSavedUnitButton = new Button("Einheit entfernen");
        removeSavedUnitButton.setOnAction(e -> {
            Unit selectedSavedUnit = savedUnitsView.getSelectionModel().getSelectedItem();
            if (selectedSavedUnit != null) {
                removeSavedUnitAndReleaseItems(selectedSavedUnit);
            }
        });
        
        // Layouts
        VBox unitBox = new VBox(new Label("Einheit wählen:"), unitComboBox, unitStatsLabel, clearUnitButton);
        itemsBox = new VBox(new Label("Verfügbare Items:"), itemListView, equipButton);
        VBox equippedBox = new VBox(new Label("Ausgerüstete Items:"), equippedItemsView, removeButton, saveUnitButton, usedCapacityLabel);
        VBox weaponsBox = new VBox(new Label("Waffen der Einheit:"), weaponsView);
        VBox controlsBox = new VBox(editLimitsButton, exportButton);        
        VBox savedUnitsBox = new VBox(new Label("Gespeicherte Einheiten:"), savedUnitsView, loadUnitButton, removeSavedUnitButton);
        HBox controlsLayout = new HBox(20, unitBox, itemsBox, equippedBox, weaponsBox, controlsBox, savedUnitsBox);
        controlsLayout.setPadding(new javafx.geometry.Insets(15));
        
        // Layouts für Save Slots dynamisch generieren
        HBox overview = new HBox(20);
        overview.setPadding(new Insets(15));
        
        // Savelot-Inhalt (Unitstats und Waffen) initialisieren und hinzufügen
        for (int i = 0; i < UNIT_AMOUNT; i++) {
            int index = i;
            TextArea weaponInfoTextArea = new TextArea("Waffen: ");
            weaponInfoTextArea.setWrapText(true);
            weaponInfoTextArea.setMaxWidth(250); // Max. Breite!
            weaponInfoTextArea.setPrefWidth(250);
            VBox.setVgrow(weaponInfoTextArea, Priority.NEVER);
            
            savedUnitStatsTextArea[i].setWrapText(true);
            savedUnitStatsTextArea[i].setMaxWidth(250);
            savedUnitStatsTextArea[i].setPrefWidth(250);
            VBox.setVgrow(savedUnitStatsTextArea[i], Priority.NEVER);
            
            quantitySpinner[i] = new Spinner<>();
            quantitySpinner[i].setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));

            saveSlotComboBoxes[i].setOnAction(e -> {
                Unit selected = saveSlotComboBoxes[index].getValue();
                if (selected != null) {
                    savedUnitStatsTextArea[index].setText(unitStats(selected).getText() + "\nItems: " + selected.getItems() + "\nArmor: " + selected.getArmors());
                }

                
                quantitySpinner[index].valueProperty().addListener((obs, oldVal, newVal) -> {
                    if (isUpdatingSpinner) return; // Verhindert rekursive Aufrufe
                    if (selected != null && newVal > oldVal) {
                        if (limitManager.addUnitPossible(selected)) {
                            
                        } else {
                            isUpdatingSpinner = true; // Verhindert rekursive Aufrufe
                            quantitySpinner[index].getValueFactory().setValue(oldVal); // Setzt den alten Wert zurück
                            isUpdatingSpinner = false; // Rekursive Aufrufe wieder zulassen
                            return; // Abbruch, wenn Limit erreicht
                        }
                    } else if (selected != null && newVal < oldVal) {
                            for (Item item : selected.getEquipment()) {
                                for (int j = newVal; j < oldVal; j++) {
                                    limitManager.unequipItem(item.getName());
                                }
                            }
                        
                    }
                    updateItemListView();
                });

                    // Waffeninformationen aufbauen
                    StringBuilder weaponText = new StringBuilder("Waffen:\n");
                    for (Weapon weapon : selected.getWeapons()) {
                        weaponText.append("- ")
                                  .append(weapon.getName())
                                  .append(" (S: ").append(weapon.getEffectiveStrength(saveSlotComboBoxes[index].getValue()))
                                  .append(", AP: ").append(weapon.getAP())
                                  .append(", D: ").append(weapon.getDamage())
                                  .append(", Effects: ").append(weapon.getEffect())
                                  .append(")\n");
                    }
                    weaponInfoTextArea.setText(weaponText.toString());
	                    
            });
        	
            //Zeigt die gespeicherte Einheit und ihre Waffen an           
            VBox saveSlotBox = new VBox(
                new Label("SaveSlot " + (i + 1)), new Label("Anzahl:"), quantitySpinner[i],
                saveSlotComboBoxes[i],
                savedUnitStatsTextArea[i],
                weaponInfoTextArea
            );
            saveSlotBox.setSpacing(5);
            saveSlotBox.setMaxWidth(270); // Optional: begrenzt komplette VBox
            overview.getChildren().add(saveSlotBox);
            
        }
        
     // ScrollPane erstellen
        ScrollPane scrollPane = new ScrollPane(overview);
        scrollPane.setFitToHeight(false);
        scrollPane.setFitToWidth(false); // horizontales Scrollen erlauben
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // Root-Layout
        VBox root = new VBox(20, controlsLayout, scrollPane);

        primaryStage.setScene(new Scene(root, 1000, 800));
        primaryStage.show();
    }

    private void removeSavedUnitAndReleaseItems(Unit unitToRemove) {
        if (unitToRemove == null) return;

        // Items im LimitManager wieder freigeben
        for (Item item : unitToRemove.getEquipment()) {
            limitManager.unequipItem(item.getName());
        }

        // Einheit aus der Speicherung entfernen
        unitStorage.getSavedUnits().remove(unitToRemove);

        // UI aktualisieren
        updateSafeSlotComboBoxes();
        savedUnitsView.getItems().setAll(unitStorage.getSavedUnits());
        updateItemListView();
        weaponsView.getItems().clear();
        equippedItemsView.getItems().clear();
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
        List<Unit> savedUnits = unitStorage.getSavedUnits();
        for (ComboBox<Unit> comboBox : saveSlotComboBoxes) {
            comboBox.getItems().setAll(savedUnits);
        }
    }
    
    private void updateUsedCapacity() {
        if (selectedUnit != null) {
            usedCapacityLabel.setText("Used Capacity: " + selectedUnit.getUsedCapacity() + " / " + + selectedUnit.getCapacity());
        }
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
