package com.pavka;



public class Unit extends Force {

    public static final UnitType[] ALL_TYPES = UnitType.values();
    public static final UnitType[] COMBAT_TYPES_BY_FOOD = UnitType.combatByFood();
    public static final UnitType[] COMBAT_TYPES_BY_AMMO = UnitType.combatByAmmo();
    public static final UnitType[] UNITS_BY_SPEED = UnitType.unitsBySpeed();

    public static final double CHARGE_ON_ARTILLERY = 1.25;
    public static final double CHARGE_ON_CAVALRY = 0.75;
    public static final double LACK_OF_AMMO_PENALTY = - 0.1;
    public static final double OUT_OF_AMMO_PENALTY = - 0.3;
    public static final double OUT_OF_FOOD_PENALTY = - 0.2 / 4;
    public static final double MORALE_RESTORE = 0.1 / 4;
    public static final double FATIGUE_DROP = 0.05;
    public static final double FATIGUE_RECOVER = 0.03;


    UnitType type;
    int maxStrength;
    double maxFire;
    double maxCharge;
    double imprisoned;
    double isolatedUnitPenalty;
    boolean isDisordered;

    public Unit(Nation nation, Hex hex) {

        super(nation, hex);
        isUnit = true;
    }
    public Unit(Nation nation, UnitType type, Hex hex, int strength) {
        super(nation, hex);
        isUnit = true;
        this.type = type;
        maxStrength = type.STRENGTH;
        maxFire = type.FIRE;
        maxCharge = type.CHARGE;
        imprisoned = type.imrisoned;
        speed = type.SPEED;
        this.strength = strength;
        double k = maxStrength > 0? (double)strength / maxStrength : 1.0;
        foodNeed = type.FOOD_NEED * k;
        ammoNeed = type.AMMO_NEED * k;
        foodLimit = type.FOOD_LIMIT * k;
        ammoLimit = type.AMMO_LIMIT * k;
        foodStock = type.FOOD_LIMIT * k;
        ammoStock = type.AMMO_LIMIT * k;
        fire = type.FIRE  * k;
        charge = type.CHARGE * k;
        xp = 0;
        fatigue = 0;
        morale = nation.getNationalMorale();
    }
    public Unit(Nation nation, UnitType type, Hex hex) {
        this(nation, type, hex, type.STRENGTH);
    }
    public Unit(Play play, Nation nation, UnitType type, Hex hex) {
        this(nation, type, hex);
        this.play = play;
    }
    public Unit(Play play, Nation nation, Hex hex) {
        this(nation, hex);
        this.play = play;
    }

    public static double getFoodRatio(UnitType type) {
        return  type.FOOD_LIMIT /type.FOOD_NEED;
    }

    public static double getUnitFoodNeed(UnitType type) {
        return type.FOOD_NEED;
    }

    public static double getAmmoRatio(UnitType type) {
        return type.AMMO_LIMIT / type.AMMO_NEED;
    }

    public static double getUnitAmmoNeed(UnitType type) {
        return type.AMMO_NEED;
    }



    @Override
    public Unit getReplenished(Unit unit) {
        if (unit.type == type) {
            int replenish = maxStrength - strength;
            if (unit.strength < replenish) {
                replenish = unit.strength;
            }
            double ratio = (double) (replenish / unit.strength);
            getReinforced(replenish, unit.xp, unit.morale, unit.fatigue, unit.foodStock * ratio, unit.ammoStock * ratio,
                    unit.foodNeed * ratio, unit.ammoNeed * ratio, unit.foodLimit * ratio, unit.ammoLimit * ratio,
                    unit.fire * ratio, unit.charge * ratio);


            unit.foodStock *= (1 - ratio);
            unit.ammoStock *= (1 - ratio);
            unit.foodNeed *= (1 - ratio);
            unit.ammoNeed *= (1 - ratio);
            unit.foodLimit *= (1 - ratio);
            unit.ammoLimit *= (1 - ratio);
            unit.fire *= (1 - ratio);
            unit.charge *= (1 - ratio);

            unit.strength -= replenish;

        }
        if (unit.strength == 0) return null;
        return unit;
    }

    public boolean belongsToTypes(UnitType... types) {
        for (int i = 0; i < types.length; i++) {
            if (type == types[i]) return true;
        }
        return false;
    }

    public int bearLoss(double ratio) {
        if (ratio > 1) ratio = 1;
        int s = (int) (strength * ratio);
        double fS = foodStock * ratio;
        double aS = ammoStock * ratio;
        double fN = foodNeed * ratio;
        double aN = ammoNeed * ratio;
        double fL = foodLimit * ratio;
        double aL = ammoLimit * ratio;
        double f = fire * ratio;
        double c = charge * ratio;
        strength -= s;
        foodStock -= fS;
        ammoStock -= aS;
        foodNeed -= fN;
        ammoNeed -= aN;
        foodLimit -= fL;
        ammoLimit -= aL;
        fire -= f;
        charge -= c;

        if (isSub)
            superForce.getReinforced(-s, this.xp, this.morale, this.fatigue, -fS, -aS, -fN, -aN, -fL, -aL, -f, -c);

        return s;
    }

    public int bearLoss(int c) {
        int s = 0;
        if(strength > 0) {

        }
        return s;
    }

    public void changeFatigue(double t){
        if(fatigue + t < 0) {
            t = -fatigue;
        }
        fatigue += t;
        if(isSub) superForce.updateFatigue(strength, t);
    }

    public Unit changeMorale(double change) {
        if (type == UnitType.CAVALRY) change *= CHARGE_ON_CAVALRY;
        if (type == UnitType.ARTILLERY) change *= CHARGE_ON_ARTILLERY;
        morale += change;
        if (isSub) superForce.updateMorale(strength, change);
        return this;
    }

    public void changeMorale(double change, boolean nonCombat) {
        if(!nonCombat) changeMorale(change);
        else {
            morale += change;
            if (isSub) superForce.updateMorale(strength, change);
        }
    }

    public void levelUnitMorale() {
        double xpon = Math.abs((nation.getNationalMorale() - morale) / nation.getNationalMorale());
        double change = morale < nation.getNationalMorale()? MORALE_RESTORE * (Math.exp(xpon) - 1) : MORALE_RESTORE * (1 - Math.exp(xpon));
        //if(change < MORALE_RESTORE) change = nation.getNationalMorale() - morale;
        changeMorale(change, true);
    }

    public double fire(double ratio) {
        double fireAttack = fire;
        double initStock = ammoStock;
        if (ammoStock > ammoNeed * ratio) {
            ammoStock -= ammoNeed * ratio;
            if (ammoStock < ammoNeed * ratio) changeMorale(LACK_OF_AMMO_PENALTY);
        }
        else {
            ammoStock = 0;
            fire = 0;
            changeMorale(OUT_OF_AMMO_PENALTY);
        }
        if (isSub) superForce.doFire(ammoStock - initStock, fire - fireAttack);
        return fireAttack;
    }
    public void route() {
        if (isSub) superForce.detach(this);
        Hex hx = hex.getNeighbour(order.retreatDirection);
        if(hx == null) surrender();
        else moveTo(hx);

    }


}
