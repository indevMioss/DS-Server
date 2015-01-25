package com.interdev.dsserver.roomsystem.gamelogics;

public abstract class Unit {
    public int id;
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

    }

    public void initUnitTypeValues() {
        UnitValues.UnitVal unitType = UnitValues.getByType(type);
        lives = unitType.lives;
        damage = unitType.damage;
        atk_range = unitType.atk_range;
        atk_interval = unitType.atk_speed;
        walk_speed = unitType.walk_speed;
        texture_width = unitType.texture_width;

    }
}
