package com.pavka;

public enum Direction {
    EAST,
    NORTHEAST,
    NORTHWEST,
    WEST,
    SOUTHWEST,
    SOUTHEAST;

    public Direction getOpposite() {
        switch (this) {
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

    public Direction getLeftBack() {
        switch (this) {
            case EAST:
                return NORTHWEST;
            case WEST:
                return SOUTHEAST;
            case NORTHEAST:
                return WEST;
            case NORTHWEST:
                return SOUTHWEST;
            case SOUTHWEST:
                return EAST;
            case SOUTHEAST:
                return NORTHEAST;
        }
        return null;
    }

    public Direction getRightBack() {
        switch (this) {
            case EAST:
                return SOUTHWEST;
            case WEST:
                return NORTHEAST;
            case NORTHEAST:
                return SOUTHEAST;
            case NORTHWEST:
                return EAST;
            case SOUTHWEST:
                return NORTHWEST;
            case SOUTHEAST:
                return WEST;
        }
        return null;
    }

    public Direction getRightForward() {
        switch (this) {
            case EAST:
                return SOUTHEAST;
            case WEST:
                return NORTHWEST;
            case NORTHEAST:
                return EAST;
            case NORTHWEST:
                return NORTHEAST;
            case SOUTHWEST:
                return WEST;
            case SOUTHEAST:
                return SOUTHWEST;
        }
        return null;
    }

    public Direction getLeftForward() {
        switch (this) {
            case EAST:
                return NORTHEAST;
            case WEST:
                return SOUTHWEST;
            case NORTHEAST:
                return NORTHWEST;
            case NORTHWEST:
                return WEST;
            case SOUTHWEST:
                return SOUTHEAST;
            case SOUTHEAST:
                return EAST;
        }
        return null;
    }
}
