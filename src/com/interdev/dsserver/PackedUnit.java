package com.interdev.dsserver;

import com.interdev.dsserver.roomsystem.gamelogics.ActiveUnit;
import com.interdev.dsserver.roomsystem.gamelogics.PlayerValues;

public class PackedUnit {
    public short type;
    public short x;
    public short y;
    public short lives;
    public int id;
    public int targetId;

    public PackedUnit(ActiveUnit unit, boolean inversed) {
        type = unit.type;
        if (inversed) {
            x = (short) (PlayerValues.BATTLEFIELD_WIDTH - unit.getX());
            y = (short) (PlayerValues.TOTAL_FIELD_HEIGHT - unit.getY());
        } else {
            x = unit.getX();
            y = unit.getY();
        }
        lives = unit.lives;
        id = unit.getID();
        targetId = unit.getTargetId();
    }
}
