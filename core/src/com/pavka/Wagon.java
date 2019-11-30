package com.pavka;



public class Wagon extends Unit {
    /*public static final int MAX_STRENGTH = 0;
    public final static double SPEED = 20;
    public final static double FOOD_NEED = 0;
    public final static double AMMO_NEED = 0;
    public final static double FOOD_LIMIT = 50;
    public final static double AMMO_LIMIT = 30;*/

    public Wagon(Nation nation, Hex hex) {
        super(nation, UnitType.SUPPLY, hex);
        /* isUnit = true;
        type = UnitType.SUPPLY;
        maxStrength = MAX_STRENGTH;
        strength = MAX_STRENGTH;
        speed = SPEED;
        foodNeed = FOOD_NEED;
        ammoNeed = AMMO_NEED;
        foodLimit = FOOD_LIMIT;
        ammoLimit = AMMO_LIMIT;
        foodStock = FOOD_LIMIT;
        ammoStock = AMMO_LIMIT;
        xp = 0;
        fatigue = 0;
        morale = nation.getNationalMorale();*/
    }
    public Wagon(Play play, Nation nation, Hex hex) {
        this(nation, hex);
        this.play = play;
        play.addActor(this);
    }

}
