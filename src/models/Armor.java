package models;

public class Armor extends Item {
    private int leadership, saveBonus, toughnessBonus, movementModifier, woundsBonus;

    public Armor(String name, int movementModifier, int cost, int capacity, int saveBonus, int toughnessBonus, int woundsBonus) {
        super(name, cost, capacity);
        this.saveBonus = saveBonus;
        this.toughnessBonus = toughnessBonus;
        this.movementModifier = movementModifier;
        this.woundsBonus = woundsBonus;
    }

    @Override
    public int getSaveBonus() {
        return saveBonus;
    }
    
    @Override
    public int getToughnessBonus() {
        return toughnessBonus;
    }
    
    public int getWoundsBonus() {
        return woundsBonus;
    }
    
    @Override
    public int getMovementModifier() {
        return movementModifier;
    }

    @Override
    public String toString() {
        return name + " (+" + saveBonus + " Save, +" + toughnessBonus + " Toughness " + ", Capacity: " + super.capacity +")";
    }
}
