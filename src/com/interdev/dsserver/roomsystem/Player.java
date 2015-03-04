package com.interdev.dsserver.roomsystem;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.minlog.Log;
import com.interdev.dsserver.PackedUnit;
import com.interdev.dsserver.Packet;
import com.interdev.dsserver.roomsystem.gamelogics.ActiveUnit;
import com.interdev.dsserver.roomsystem.gamelogics.PassiveUnit;
import com.interdev.dsserver.roomsystem.gamelogics.PlayerValues;
import com.interdev.dsserver.roomsystem.gamelogics.UnitValues;

import java.util.ArrayList;
import java.util.Iterator;


public class Player {
    public Room myRoom;
    public boolean ready = false;
    public Connection connection;

    public boolean baseAtTheTop;
    public int money;
    public int income;
    public float lives;

    public int activeUnitsIDsCounter = 0;
    public int passiveUnitsIDsCounter = 0;

    public ArrayList<ActiveUnit> activeUnitsList;
    public ArrayList<PassiveUnit> passiveUnitsList;

    public Player(Connection connection, Room room, boolean baseAtTheTop) {
        this.connection = connection;
        myRoom = room;

        this.baseAtTheTop = baseAtTheTop;
        activeUnitsList = new ArrayList<ActiveUnit>();
        passiveUnitsList = new ArrayList<PassiveUnit>();

        lives = PlayerValues.BASE_START_LIVES;

        money = PlayerValues.START_MONEY;
        income = PlayerValues.INCOME_LVL1;
    }

    public void iAmReady() {
        Log.info("iAmReady()");
        if (!ready) {
            Log.info("if (!ready)");

            ready = true;
            if (myRoom.getOppositePlayer(this).ready) {
                Log.info(" if (myRoom.getOppositePlayer(this).ready)");

                myRoom.start();
            }
        }
    }

    public void spawnWave() {
        for (PassiveUnit unit : passiveUnitsList) {
            spawnUnit(unit.x, unit.y, unit.type);
        }
    }

    private void spawnUnit(short x, short y, short type) {
        if (baseAtTheTop) {
            activeUnitsIDsCounter--;
        } else {
            activeUnitsIDsCounter++;
        }
        ActiveUnit unit = new ActiveUnit(x, y, type, myRoom.getOppositePlayer(this), activeUnitsIDsCounter);
        activeUnitsList.add(unit);
    }

    public void act(float deltaTime) {
        for (ActiveUnit unit : activeUnitsList) {
            unit.act(deltaTime);
        }
    }

    public PackedUnit[] getPackedUnits(boolean inversed) {
        PackedUnit[] packedUnitsArray = new PackedUnit[activeUnitsList.size()];
        int i = 0;
        for (ActiveUnit unit : activeUnitsList) {
            PackedUnit packedUnit = new PackedUnit(unit, inversed);
            packedUnitsArray[i] = packedUnit;
            i++;
        }
        return packedUnitsArray;
    }

    public void handleDeadUnits() {
        Iterator<ActiveUnit> it = activeUnitsList.iterator();
        while (it.hasNext()) {
            ActiveUnit unit = it.next();
            if (!unit.isAlive()) {
                it.remove();
            }
        }
    }


    public void onUnitPurchaseRequest(Packet.PacketRequestUnitPurchase packet) {
        if (money >= UnitValues.getByType(packet.type).price) {
            Log.info("onUnitPurchaseRequest money >=");

            money -= UnitValues.getByType(packet.type).price;

            if (baseAtTheTop) {
                passiveUnitsList.add(new PassiveUnit((short) (PlayerValues.BATTLEFIELD_WIDTH - packet.x), (short) (PlayerValues.TOTAL_FIELD_HEIGHT - packet.y), packet.type, ++passiveUnitsIDsCounter));
            } else {
                passiveUnitsList.add(new PassiveUnit(packet.x, packet.y, packet.type, ++passiveUnitsIDsCounter));
            }

            Packet.PacketAnswerUnitPurchase packt = new Packet.PacketAnswerUnitPurchase();
            packt.id = passiveUnitsIDsCounter;
            connection.sendTCP(packt);

        } else {
            Log.info("onUnitPurchaseRequest money <");
            Packet.PacketAnswerUnitPurchase packt = new Packet.PacketAnswerUnitPurchase();
            packt.id = 0;
            connection.sendTCP(packt);

        }
    }


    public void onUnitSellRequest(Packet.PacketRequestUnitSell packet) {

    }

    public void onUpgradeRequest(Packet.PacketRequestUpgrade packet) {

    }
}
