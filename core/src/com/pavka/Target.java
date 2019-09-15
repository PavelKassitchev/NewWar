package com.pavka;

public class Target {

    public static final int FIGHT = -1;
    public static final int FOLLOW = 0;
    public static final int JOIN = 1;
    public static final int TAKE = 2;

    Force force;
    int action;

    public Target(Force target, int action) {
        this.force = target;
        this.action = action;
    }
}
