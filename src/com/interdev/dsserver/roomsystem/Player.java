package com.interdev.dsserver.roomsystem;

import com.esotericsoftware.kryonet.Connection;
import com.google.common.primitives.Ints;
import com.interdev.dsserver.PackedUnit;
import com.interdev.dsserver.roomsystem.gamelogics.ActiveUnit;
import com.interdev.dsserver.roomsystem.gamelogics.PassiveUnit;

import java.util.ArrayList;
import java.util.Iterator;


public class Player {
    private Room myRoom;
    public boolean ready = false;
    public Connection connection;

    public boolean baseAtTheTop;
    public int money;
    public int income;
    public float lives;

    public int unitsIDsCounter = 0;
    public ArrayList<ActiveUnit> activeUnitsList;
    public ArrayList<PassiveUnit> passiveUnitsList;

    private ArrayList<Integer> deadUnitsIDsList;

    public Player(Connection connection, Room room, boolean baseAtTheTop) {
        this.connection = connection;
        myRoom = room;

        this.baseAtTheTop = baseAtTheTop;
        activeUnitsList = new ArrayList<ActiveUnit>();
        passiveUnitsList = new ArrayList<PassiveUnit>();
        deadUnitsIDsList = new ArrayList<Integer>();

        lives = 100f;
        money = 0;
        income = 10;
    }

    public void iAmReady() {
        if(!ready) {
            ready = true;
            if (myRoom.getOppositePlayer(this).ready) {
                myRoom.start();
            }
        }
    }

    public void spawnWave() {
        for (PassiveUnit unit : passiveUnitsList) {
            spawnUnit(unit.x, unit.y, unit.type);
        }
    }

    public void spawnUnit(short x, short y, short type) {
        if(baseAtTheTop) {
            unitsIDsCounter --;
        } else {
            unitsIDsCounter ++;
        }
        ActiveUnit unit = new ActiveUnit(x, y, type, myRoom.getOppositePlayer(this), unitsIDsCounter);
        activeUnitsList.add(unit);
    }

    public void act() {
        handleDeadUnits();

        for (ActiveUnit unit : activeUnitsList) {
            unit.act();
        }
    }

    public PackedUnit[] getPackedUnits() {
        PackedUnit[] packedUnitsArray = new PackedUnit[activeUnitsList.size()];
        int i = 0;
        for (ActiveUnit unit : activeUnitsList) {
            PackedUnit packedUnit = new PackedUnit();
            packedUnit.type = unit.type;
            packedUnit.x = unit.x;
            packedUnit.y = unit.y;
            packedUnit.lives = unit.lives;
            packedUnit.id = unit.id;
            packedUnit.targetId = unit.targetId;
            packedUnitsArray[i] = packedUnit;
            i++;
        }
        return packedUnitsArray;
    }

    private void handleDeadUnits() {
        deadUnitsIDsList.clear();
        Iterator<ActiveUnit> deadUnitsIterator = activeUnitsList.iterator();
        while (deadUnitsIterator.hasNext()) {
            ActiveUnit unit = deadUnitsIterator.next();
            if (unit.lives <= 0) {
                unitDied(unit);
                deadUnitsIterator.remove();
            }
        }

        myRoom.sendDeadUnitsIDsPacket(Ints.toArray(deadUnitsIDsList));
    }

    public void unitDied(ActiveUnit unit) {
        deadUnitsIDsList.add(unit.id);
    }


}
