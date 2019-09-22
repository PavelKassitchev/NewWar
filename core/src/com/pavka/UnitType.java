package com.pavka;

public enum UnitType {
    SUPPLY(0, 0, 20, 0, 0, 50, 30, 0, 0, 0.4),
    INFANTRY(1, 900, 28, 2, 0.5, 12, 4, 1, 1, 0.2),
    CAVALRY(2, 180, 44, 1.1, 0.1, 7, 2.4, 0.1, 2.4, 0.16),
    ARTILLERY(3, 104, 20, 0.6, 1, 8, 5, 4, 0, 0.1),
    HEADQUARTERS(4, 0, 100, 0, 0, 0, 0, 0, 0, 0);

    UnitType(int num, int STRENGTH, double SPEED, double FOOD_NEED, double AMMO_NEED, double FOOD_LIMIT, double AMMO_LIMIT, double FIRE, double CHARGE, double LENGTH) {
        this.num = num;
        this.STRENGTH = STRENGTH;
        this.SPEED = SPEED;
        this.FOOD_NEED = FOOD_NEED;
        this.AMMO_NEED = AMMO_NEED;
        this.FOOD_LIMIT = FOOD_LIMIT;
        this.AMMO_LIMIT = AMMO_LIMIT;
        this.FIRE = FIRE;
        this.CHARGE = CHARGE;
        this.LENGTH = LENGTH;
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

    /*public static UntitType[] getAllTypes() {
        return UnitType.values();
    }*/

}
