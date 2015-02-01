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
            x = (short) (PlayerValues.BATTLEFIELD_WIDTH - unit.x);
            y = (short) (PlayerValues.TOTAL_FIELD_HEIGHT - unit.y);
        } else {
            x = unit.x;
            y = unit.y;
        }
        lives = unit.lives;
        id = unit.id;
        targetId = unit.getTargetId();
    }
}
