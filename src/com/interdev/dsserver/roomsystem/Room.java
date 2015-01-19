package com.interdev.dsserver.roomsystem;


import com.esotericsoftware.kryonet.Connection;
import com.interdev.dsserver.Packet;

import java.util.Timer;
import java.util.TimerTask;

public class Room {
    public static final int battlefildWidth = 720;
    public static final int battlefildHeight = 1280;
    public static final int tickInterval = 100;//ms
    public static final int actDelay = 50;//ms
    public static final int spawnInterval = 20000;//ms


    public int ticks = 0;
    public Player player1, player2;

    public Timer actTimer;

    public Room(Connection connection1, Connection connection2) {
        player1 = new Player(connection1, this, false);
        player2 = new Player(connection2, this, true);

        actTimer = new Timer();

    }

    public void start() {
        actTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                act();
            }
        }, actDelay, tickInterval);
    }

    public void act() {
        ticks++;

        player1.act();
        player2.act();

        Packet.PacketBattlefieldUnitsUpdate battlefieldUpdatePacket = new Packet.PacketBattlefieldUnitsUpdate();
        battlefieldUpdatePacket.Player1PackedUnits = player1.getPackedUnits();
        battlefieldUpdatePacket.Player2PackedUnits = player2.getPackedUnits();
        player1.connection.sendTCP(battlefieldUpdatePacket);
        player2.connection.sendTCP(battlefieldUpdatePacket);

        if (ticks * tickInterval >= spawnInterval) {
            ticks = 0;
            player1.connection.sendTCP(new Packet.PacketWaveSpawned());
            player2.connection.sendTCP(new Packet.PacketWaveSpawned());
        }

    }

    public void sendDeadUnitsIDsPacket(int[] ids) {
        Packet.PacketDeadUnitsIDs deadUnitsIDsPacket = new Packet.PacketDeadUnitsIDs();
        deadUnitsIDsPacket.deadUnitsIDs = ids;
        player1.connection.sendTCP(deadUnitsIDsPacket);
        player2.connection.sendTCP(deadUnitsIDsPacket);
    }

    public Player getOppositePlayer(Player player) {
        if (player1.equals(player)) {
            return player2;
        }
        if (player2.equals(player)) {
            return player1;
        }
        return null;
    }

}
