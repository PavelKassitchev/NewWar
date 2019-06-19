package com.pavka;

import com.badlogic.gdx.utils.Array;

public class Order {
    public boolean seekBattle;
    public double retreatLevel;
    public Array<Path> pathsOrder = new Array<Path>();
    MileStone mileStone;


    public Order() {
        pathsOrder = new Array<Path>();
        mileStone = new MileStone();
    }

    public Order(boolean seekBattle, double retreatLevel) {
        pathsOrder = new Array<Path>();
        mileStone = new MileStone();
        this.seekBattle = seekBattle;
        this.retreatLevel = retreatLevel;
    }

    public void setPathsOrder(Array<Path> pathsOrder) {
        this.pathsOrder = pathsOrder;
        //mileStone = new MileStone(pathsOrder.peek().getToNode());
    }
}
