package models;

import game.GameSetup;

public class Item {
    protected String name, effect;
    protected int cost, capacity, movementModifier;

    public Item(String name, int cost, int capacity, String effect) {
        this.name = name;
        this.cost = cost;
        this.capacity = capacity;
        this.effect = effect;
    }

    public String getName() {
        return name;
    }

    public String getEffect() {
    	return effect;
    }

    public int getCost() {
        return cost;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getStrengthBonus() {
        return 0;
    }

    public int getSaveBonus() {
        return 0;
    }
    
    public int getToughnessBonus() {
        return 0;
    }
    
    public int getLeadershipModifier() {
        return 0;
    }

    @Override
    public String toString() {
        return "(" + GameSetup.getItemLimitManager().getRemaining(name) + ") " + name + " (Kapazität: " + capacity + ")";
    }
    
    public int getMovementModifier() {
    	return 0;
    }
}

