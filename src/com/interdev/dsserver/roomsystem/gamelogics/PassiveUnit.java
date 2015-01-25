package com.interdev.dsserver.roomsystem.gamelogics;


public class PassiveUnit extends Unit {

    public PassiveUnit(short x, short y, short type, int id) {
        super(x, y, type);
        this.id = id;
    }
}
