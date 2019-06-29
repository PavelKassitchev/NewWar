package com.pavka;

public abstract class Message {
    public int turn;
    public int issued;
    public Message() {
        issued = Play.turn;
    }


}
