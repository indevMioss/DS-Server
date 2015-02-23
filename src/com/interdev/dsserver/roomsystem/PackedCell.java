package com.interdev.dsserver.roomsystem;

public class PackedCell {
    public boolean free = true;
    public short x;
    public short y;

    public PackedCell() {
        x = 0;
        y = 0;
        free = true;
    }

    public PackedCell(short x, short y, boolean free) {
        this.free = free;
        this.x = x;
        this.y = y;
    }
}