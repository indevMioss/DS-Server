package com.interdev.dsserver.roomsystem.gamelogics;

public abstract class Unit {
    public short x, y;
    public short type;
    public short lives;
    public short damage;
    public short atk_range;
    public float atk_interval;
    public short walk_speed;
    public short texture_width;

    protected Unit(short x, short y, short type) {
        this.x = x;
        this.y = y;
        this.type = type;
        initUnitTypeValues(type);
    }

    private void initUnitTypeValues(short type) {
        switch (type) {
            case 0:
                lives = UnitValues.Unit0.lives;
                damage = UnitValues.Unit0.damage;
                atk_range = UnitValues.Unit0.atk_range;
                atk_interval = UnitValues.Unit0.atk_speed;
                walk_speed = UnitValues.Unit0.walk_speed;
                texture_width = UnitValues.Unit0.texture_width;
                break;

        }
    }
}
