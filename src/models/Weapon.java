package models;

public class Weapon extends Item {
    private int attacks, strength, damage, ap, leadership;
    private String range, effect;
    private boolean twoHanded;

    public Weapon(String name, int cost, int capacity, boolean twoHanded, int attacks, int strength, int damage, int ap, String range, String effect) {
        super(name, cost, capacity);
        this.twoHanded = twoHanded;
        this.attacks = attacks;
        this.strength = strength;
        this.damage = damage;
        this.ap = ap;
        this.range = range;
        this.effect = effect;
    }

    @Override
    public int getStrengthBonus() {
        return strength;
    }

    @Override
    public String toString() {
        return name + " (+" + strength + " Stärke, " + attacks + " Attacken, " + (twoHanded ? "2H" : "1H") + ", AP: " + ap + ", Damage: " + damage + ", Capacity: " + super.capacity + ")";
    }
    
    public int getEffectiveStrength(Unit unit) {
    	return strength + unit.getStrength(); // Stärke basiert auf Toughness der Einheit
    }
    
    @Override
    public int getLeadershipModifier() {
        return leadership;
    }
    
    public boolean getTwoHanded() {
    	return twoHanded;
    }
    
    public void setEffectiveStrength(int strength) {
        this.strength = strength;
    }
    
    public int getAttacks() {
        return attacks;
    }

    public int getAP() {
        return ap;
    }

    public int getDamage() {
        return damage;
    }
    
    public String getRange() {
    	return range;
    }
    
    public String getEffect() {
    	return effect;
    }
}