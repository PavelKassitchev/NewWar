package com.pavka;

import com.badlogic.gdx.utils.Array;

import java.util.Arrays;
import java.util.Comparator;

public enum UnitType {
    SUPPLY(0, 0, 12, 0, 0, 40, 25, 0, 0, 0.4, 0),
    INFANTRY(1, 900, 16, 0.4, 0.4, 8, 1.5, 1, 1, 0.2, 1),
    CAVALRY(2, 180, 20, 0.4, 0.06, 6.5, 0.45, 0.1, 2.4, 0.16, 0.7),
    ARTILLERY(3, 104, 12, 0.12, 1.6, 3, 6.4, 4, 0, 0.1, 1.1),
    HEADQUARTERS(4, 0, 48, 0, 0, 0, 0, 0, 0, 0, 0);

    /*
    Ammo:
    Battalion - 500 Kgs per 4 hours; 400, stock 1500  0.5 and 4
    stock - 1600 Kgs
    Squadron - 80 Kgs per 4 hours; 60, stock 450   former 0.1 and 2.4
    stock - 400 Kgs
    Battery - 1600 Kgs per 4 hours; 1600, stock 6400 former 1 and 2.4
    stock - 6000 Kgs
    Division 16 hour stock - 55200 Kgs  4 hour consumption 11320

    Food:
    Battalion - 1800 Kgs per day - stock 5 days  1600 : 4, stock 8000   former 2 and 12
    Squadron - 1800 Kgs per day - stock 5 days   1600 : 4, stock 6500   former 1.1 and 7
    Battery - 600 Kgs per day - stock 5 days 500 : 4, stock 3000  former 0.6 and 8
    Division 5 days stock - 138000Kgs 1 day consumption 24400

    2 horse wagon - 1000 Kgs
    100 wagons length - 650 m

    or maybe 65 wagon train of 400 m length stock  25000 + 40000?  former 50 and 30
     */
    UnitType(int num, int STRENGTH, double SPEED, double FOOD_NEED,
             double AMMO_NEED, double FOOD_LIMIT, double AMMO_LIMIT, double FIRE, double CHARGE, double LENGTH, double imprisoned) {
        //this.num = num;
        this.STRENGTH = STRENGTH;
        this.SPEED = SPEED;
        this.FOOD_NEED = FOOD_NEED;
        this.AMMO_NEED = AMMO_NEED;
        this.FOOD_LIMIT = FOOD_LIMIT;
        this.AMMO_LIMIT = AMMO_LIMIT;
        this.FIRE = FIRE;
        this.CHARGE = CHARGE;
        this.LENGTH = LENGTH;
        this.imrisoned = imprisoned;
    }

    public int num;
    public int STRENGTH;
    public double SPEED;
    public double FOOD_NEED;
    public double AMMO_NEED;
    public double FOOD_LIMIT;
    public double AMMO_LIMIT;
    public double FIRE;
    public double CHARGE;
    public double LENGTH;
    public double imrisoned;

    public static UnitType[] combatByFood() {
        Array<UnitType> unitTypes = new Array<UnitType>();
        for(UnitType u: UnitType.values()) {
            if(u.FOOD_NEED > 0) {
                unitTypes.add(u);
            }
        }
        unitTypes.sort(new Comparator<UnitType>() {
            @Override
            public int compare(UnitType o1, UnitType o2) {
                if(o1.FOOD_LIMIT / o1.FOOD_NEED > o2.FOOD_LIMIT / o2.FOOD_NEED) return 1;
                return -1;
            }
        });
        return unitTypes.toArray(UnitType.class);
    }
    public static UnitType[] combatByAmmo() {
        Array<UnitType> unitTypes = new Array<UnitType>();
        for(UnitType u: UnitType.values()) {
            if(u.AMMO_NEED > 0) {
                unitTypes.add(u);
            }
        }
        unitTypes.sort(new Comparator<UnitType>() {
            @Override
            public int compare(UnitType o1, UnitType o2) {
                if(o1.AMMO_LIMIT / o1.AMMO_NEED  > o2.AMMO_LIMIT / o2.AMMO_NEED ) {
                    return 1;
                }
                else {
                    return -1;
                }
            }
        });
        return unitTypes.toArray(UnitType.class);
    }
    public static UnitType[] unitsBySpeed() {
        UnitType[] units = UnitType.values();
        Arrays.sort(units, new Comparator<UnitType>() {

            @Override
            public int compare(UnitType o1, UnitType o2) {
                if(o1.SPEED > o2.SPEED) return 1;
                return -1;
            }
        });
        return units;
    }

}
