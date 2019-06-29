package com.pavka;

public class Report extends Message {
    Force force;
    int turn;

    public Report(Force force, int turn) {
        this.force = force;
        this.turn = turn;
    }
}
