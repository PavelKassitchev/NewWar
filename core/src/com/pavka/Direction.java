package com.pavka;

public enum Direction {
    EAST,
    NORTHEAST,
    NORTHWEST,
    WEST,
    SOUTHWEST,
    SOUTHEAST;

    public Direction getOpposite() {
        switch(this) {
            case EAST:
                return WEST;
            case WEST:
                return EAST;
            case NORTHEAST:
                return SOUTHWEST;
            case NORTHWEST:
                return SOUTHEAST;
            case SOUTHWEST:
                return NORTHEAST;
            case SOUTHEAST:
                return NORTHWEST;
        }
        return null;
    }
}
