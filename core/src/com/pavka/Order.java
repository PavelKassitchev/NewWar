package com.pavka;

import com.badlogic.gdx.utils.Array;

public class Order {
    public boolean seekBattle;
    public double retreatLevel;
    public Array<Path> pathsOrder = new Array<Path>();


    public Order() {
        pathsOrder = new Array<Path>();
    }

    public Order(boolean seekBattle, double retreatLevel) {
        pathsOrder = new Array<Path>();
        this.seekBattle = seekBattle;
        this.retreatLevel = retreatLevel;
    }
}
