package models;

public abstract class Item {
    protected String name;
    protected int cost, capacity, movementModifier;

    public Item(String name, int cost, int capacity) {
        this.name = name;
        this.cost = cost;
        this.capacity = capacity;
    }

    public String getName() {
        return name;
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
        return name + " (Kapazität: " + capacity + ")";
    }
    
    public int getMovementModifier() {
    	return 0;
    }
}

