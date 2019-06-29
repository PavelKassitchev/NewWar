package com.pavka;

import com.badlogic.gdx.utils.Array;

public class Trace {
    public static final int MAX_SIZE = 16;
    public Array<Hex> route;
    public Trace() {
        route = new Array<Hex>();
    }
    public void add(Hex hex) {
        if(route.size == MAX_SIZE) route.removeRange(0, 0);
        route.add(hex);
    }

}
