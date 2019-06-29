package com.pavka;

public class General extends Unit {

    public final static double SPEED = 100;

    public General(Nation nation, Hex hex) {
        super(nation, hex);
        isUnit = true;
        type = HEADQUATERS;
    }
}
