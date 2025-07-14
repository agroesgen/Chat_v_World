package models;

import game.GameSetup;

public class Weapon extends Item {
    private int attacks, strength, ap, leadership;
    private String range, effect, damage;
    private boolean twoHanded, indieStrength;

    public Weapon(String name, int cost, int capacity, boolean twoHanded, int attacks, int strength, boolean indieStrength, String damage, int ap, String range, String effect) {
        super(name, cost, capacity, effect);
        this.twoHanded = twoHanded;
        this.attacks = attacks;
        this.strength = strength;
        this.damage = damage;
        this.ap = ap;
        this.range = range;
        this.effect = effect;
        this.indieStrength = indieStrength;
    }

    @Override
    public int getStrengthBonus() {
        return strength;
    }

    @Override
    public String toString() {
        return "(" + GameSetup.getItemLimitManager().getRemaining(name) + ") " + name + " (+" + strength + " Stärke, " + attacks + " Attacken, " + (twoHanded ? "2H" : "1H") + ", AP: " + ap + ", Damage: " + damage + ", Capacity: " + super.capacity + ")";
    }
    
    public int getEffectiveStrength(Unit unit) {
    	if (getIndieStrength()) {
    		return strength;
    	}
    	else
    	return strength + unit.getStrength(); // Stärke basiert auf Toughness der Einheit
    }
    
    @Override
    public int getLeadershipModifier() {
        return leadership;
    }
    
    public boolean getTwoHanded() {
    	return twoHanded;
    }

    public boolean getIndieStrength() {
    	return indieStrength;
    }
    
    /*public void setEffectiveStrength(int strength) {
        this.strength = strength;
    }*/
    
    public int getAttacks() {
        return attacks;
    }

    public int getAP() {
        return ap;
    }

    public String getDamage() {
        return damage;
    }
    
    public String getRange() {
    	return range;
    }
    
    public String getEffect() {
    	return effect;
    }
}