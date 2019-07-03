package com.pavka;

public class Report extends Message {
    Force force;
    Hex hex;
    int turn;


    public Report(Force force, int turn) {
        this.force = force;
        this.hex = force.hex;
        this.turn = turn;
        this.hex = force.hex;
    }
}
