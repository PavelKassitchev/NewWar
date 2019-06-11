package com.pavka;

import com.badlogic.gdx.utils.Array;

public class Order {
    public boolean seekBattle;
    public double retreatLevel;
    public Array<Path> pathsOrder = new Array<Path>();


    public Order() {

    }

    public Order(boolean seekBattle, double retreatLevel) {
        this.seekBattle = seekBattle;
        this.retreatLevel = retreatLevel;
    }
}
