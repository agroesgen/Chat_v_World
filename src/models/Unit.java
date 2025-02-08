package models;

import java.util.ArrayList;
import java.util.List;

public class Unit {
    private String name;
    private int movement, ws, bs, strength, baseToughness, baseWounds, attacks, baseLeadership, baseSave, capacity;
    private String special;
    private List<Item> equipment;

    public Unit(String name, int movement, int ws, int bs, int strength, int toughness, int wounds,
                int attacks, int leadership, int save, int capacity, String special) {
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
        this.equipment = new ArrayList<>();
    }

    public boolean addItem(Item item) {
        int usedCapacity = equipment.stream().mapToInt(Item::getCapacity).sum();
        if (usedCapacity + item.getCapacity() <= this.capacity) {
            equipment.add(item);
            return true;
        }
        return false; // Zu viel Gewicht
    }
    
    public void removeItem(Item item) {
        if (equipment.contains(item)) {
            equipment.remove(item);
            System.out.println(item.getName() + " wurde entfernt.");
        } else {
            System.out.println("Das Item ist nicht in der Ausrüstungsliste.");
        }
    }
    
    public int getUsedCapacity() {
    	int usedCapacity = equipment.stream().mapToInt(Item::getCapacity).sum();
    	return usedCapacity;
    }
    
    public int getStrength() {
        return strength;
    }
    
    public int getCapacity() {
        return capacity;
    }
    
    public int getEffectiveMovement() {
        int modifiedMovement = movement;
        for (Item item : equipment) {
            if (item instanceof Armor) {
                modifiedMovement += ((Armor) item).getMovementModifier();
            }
        }
        return modifiedMovement;
    }


    public int getEffectiveToughness() {
        int modifiedToughness = baseToughness;
        for (Item item : equipment) {
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

    public List<Item> getEquipment() {
        return equipment;
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

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + " (Kapazität: " + capacity + ")";
    }
}

