package models;

import game.GameSetup;

public class Armor extends Item {
    private int leadership, saveBonus, toughnessBonus, movementModifier, woundsBonus;

    public Armor(String name, int movementModifier, int cost, int capacity, int saveBonus, int toughnessBonus, int woundsBonus, String effect) {
        super(name, cost, capacity, effect);
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
    public String getEffect() {
    	return effect;
    }

    @Override
    public String toString() {
        return "(" + GameSetup.getItemLimitManager().getRemaining(name) + ") " + name + " (+" + saveBonus + " Save, +" + toughnessBonus + " Toughness " + ", Capacity: " + super.capacity +")";
    }
}
