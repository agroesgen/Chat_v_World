package models;

import java.util.ArrayList;
import java.util.List;
import game.GameSetup;
import game.ItemLimitManager;

public class Unit implements Cloneable{
    private String name;
    private int movement, ws, bs, strength, baseToughness, baseWounds, attacks, baseLeadership, baseSave, capacity;
    private String special;
    private List<Item> equipment;

    public Unit(String name, int movement, int ws, int bs, int strength, int toughness, int wounds,
                int attacks, int leadership, int save, int capacity, String special, ArrayList<Item> equipment) {
        this.name = name;
        this.movement = movement;
        this.ws = ws;
        this.bs = bs;
        this.strength = strength;
        this.baseToughness = toughness;
        this.baseWounds = wounds;
        this.attacks = attacks;
        this.baseLeadership = leadership;
        this.baseSave = save;
        this.capacity = capacity;
        this.special = special;
        this.equipment = equipment;
    }
    
    /*    // Overriding the clone() method
    @Override
    public Unit clone() throws CloneNotSupportedException {
        // Returning a clone of the current object
        return (Unit) super.clone(); 
    }*/

    public boolean addItem(Item item) {
    	ItemLimitManager limitManager = GameSetup.getItemLimitManager();
    	
    	int usedCapacity = equipment.stream().mapToInt(Item::getCapacity).sum();
        if (usedCapacity + item.getCapacity() <= this.capacity && limitManager.equipItem(item.getName())) {
            equipment.add(item);
            return true;
        }
        else {
            System.out.println("Limit für " + item.getName() + " erreicht!");
            return false;
        }// Zu viel Gewicht
    }
    
    public void removeItem(Item item) {
        if (equipment.contains(item)) {
            equipment.remove(item);
        	GameSetup.getItemLimitManager().unequipItem(item.getName());
            System.out.println(item.getName() + " wurde entfernt.");
        } else {
            System.out.println("Das Item ist nicht in der Ausrüstungsliste.");
        }
    }
    
 // Getter and Setter methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMovement() {
        return movement;
    }

    public void setMovement(int movement) {
        this.movement = movement;
    }

    public int getWs() {
        return ws;
    }

    public void setWs(int ws) {
        this.ws = ws;
    }

    public int getBs() {
        return bs;
    }

    public void setBs(int bs) {
        this.bs = bs;
    }

    public int getStrength() {
        int modifiedStrength = strength;
     	for (Item item : equipment) {
         	if (item.getName()=="Pferd") {
             modifiedStrength = strength+1;
         	}
         }
     	return modifiedStrength;
     }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public List<Item> getInventory() {
    	List<Item> inventory = new ArrayList<>();
    	for (Item item : equipment) {
    		if (!(item instanceof Armor)) {
    			inventory.add(item);
    		}
    	}
    	return inventory;
    }
    
    public int getBaseToughness() {
        return baseToughness;
    }

    public void setBaseToughness(int baseToughness) {
        this.baseToughness = baseToughness;
    }

    public int getBaseWounds() {
        return baseWounds;
    }

    public void setBaseWounds(int baseWounds) {
        this.baseWounds = baseWounds;
    }

    public int getAttacks() {
        return attacks;
    }

    public void setAttacks(int attacks) {
        this.attacks = attacks;
    }

    public int getBaseLeadership() {
        return baseLeadership;
    }

    public void setBaseLeadership(int baseLeadership) {
        this.baseLeadership = baseLeadership;
    }

    public int getBaseSave() {
        return baseSave;
    }

    public void setBaseSave(int baseSave) {
        this.baseSave = baseSave;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public List<Item> getEquipment() {
        return equipment;
    }

    public void setEquipment(List<Item> equipment) {
        this.equipment = new ArrayList<>(equipment);
    }

    // Clone method
    @Override
    public Unit clone() {
        try {
            Unit cloned = (Unit) super.clone();
            cloned.equipment = new ArrayList<>(this.equipment); // Deep copy of equipment list
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // Should never happen
        }
    }
   
    public int getUsedCapacity() {
    	int usedCapacity = equipment.stream().mapToInt(Item::getCapacity).sum();
    	return usedCapacity;
    }
    
    public int getEffectiveMovement() {
        int modifiedMovement = movement;
        for (Item item : equipment) {
            if (item.getName()=="Pferd") {
                modifiedMovement += 30;
            }
            if (item instanceof Armor) {
                modifiedMovement += ((Armor) item).getMovementModifier();
            }

        }
        return modifiedMovement;
    }


    public int getEffectiveToughness() {
        int modifiedToughness = baseToughness;
        for (Item item : equipment) {
            if (item.getName()=="Pferd") {
                modifiedToughness += 1;
            }
        	if (item instanceof Armor) {
                modifiedToughness += ((Armor) item).getToughnessBonus();
            }
        }
        return modifiedToughness;
    }

    public int getEffectiveWounds() {
        int modifiedWounds = baseWounds;
        for (Item item : equipment) {
            if (item instanceof Armor) {
                modifiedWounds += ((Armor) item).getWoundsBonus();
            }
        }
        return modifiedWounds;
    }

    public int getEffectiveSave() {
        int modifiedSave = baseSave;
        for (Item item : equipment) {
            if (item instanceof Armor) {
                modifiedSave -= ((Armor) item).getSaveBonus();
            }
        }
        return modifiedSave;
    }

    public int getEffectiveLeadership() {
        int modifiedLeadership = baseLeadership;
        for (Item item : equipment) {
            if (item instanceof Armor) {
                modifiedLeadership += ((Armor) item).getLeadershipModifier();
            }
        }
        return modifiedLeadership;
    }
    
    public void clearEquipment() {
    	this.equipment.clear();
    }
    
    public List<Weapon> getWeapons() {
        List<Weapon> weapons = new ArrayList<>();
        for (Item item : equipment) {
            if (item instanceof Weapon) {
                Weapon weapon = (Weapon) item;
                // Erstelle eine neue Weapon mit berechneten Werten
                Weapon modifiedWeapon = new Weapon(
                    weapon.getName(),
                    weapon.getCost(),
                    weapon.getCapacity(),
                    weapon.getTwoHanded(),
                    weapon.getAttacks(),
                    weapon.getEffectiveStrength(this), // Stärke hängt von der Einheit ab
                    weapon.getIndieStrength(),
                    weapon.getDamage(),
                    weapon.getAP(),
                    weapon.getRange(),
                    weapon.getEffect()
                );
                weapons.add(modifiedWeapon);
            }
        }
        return weapons;
    }

    @Override
    public String toString() {
        return name + " (Kapazität: " + capacity + ")";
    }
}

