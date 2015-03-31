package com.interdev.dsserver.roomsystem.gamelogics;

import com.esotericsoftware.minlog.Log;

public abstract class Unit {
    protected int id;
    protected short x, y;
    public short type;

    public short lives;
    public short damage;
    public short atk_range;
    public float atk_interval;
    public short walk_speed;
    public short diag_walk_speed_xy_component;

    public short sight_distance;
    public short texture_width;
    public short size_in_cells;

    short randomSidemoveDirection = 1;
    boolean way_blocked = false;


    public Grid.Cell [] occupy_cells_list;


    protected Unit(short x, short y, short type) {
        this.x = x;
        this.y = y;
        this.type = type;
        initUnitTypeValues();
        size_in_cells = (short) (texture_width /  Grid.cell_size);

        switch(size_in_cells) {
            case 1:
                occupy_cells_list = new Grid.Cell[4];
                break;
            case 2:
                occupy_cells_list = new Grid.Cell[9];
                break;
            case 3:
                occupy_cells_list = new Grid.Cell[16];
                break;
            default:
                occupy_cells_list = new Grid.Cell[50];
                System.err.print("Unit cells array not specified for this size");
        }
    }

    public void initUnitTypeValues() {
        UnitValues.UnitVal unitType = UnitValues.getByType(type);
        lives = unitType.lives;
        damage = unitType.damage;
        atk_range = unitType.atk_range;
        atk_interval = unitType.atk_interval;
        walk_speed = unitType.walk_speed;
        texture_width = unitType.texture_width;
        sight_distance = unitType.sight_distance;

        diag_walk_speed_xy_component = (short) (walk_speed/Math.pow(2, 0.5f));
        Log.info(walk_speed + " --- " + diag_walk_speed_xy_component);
    }
}
