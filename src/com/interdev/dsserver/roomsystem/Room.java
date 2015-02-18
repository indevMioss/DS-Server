package com.interdev.dsserver.roomsystem;


import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.minlog.Log;
import com.interdev.dsserver.Packet;
import com.interdev.dsserver.roomsystem.gamelogics.Grid;

import java.util.Timer;
import java.util.TimerTask;

public class Room {

    public static final int tickInterval = 250;//ms
    public static final int actDelay = 50;//ms
    public static final int spawnInterval = 20000;//ms


    public int ticks = 0;
    public Player player1, player2;
    public Grid grid;

    public Timer actTimer;

    public Room(Connection connection1, Connection connection2) {
        player1 = new Player(connection1, this, false);
        player2 = new Player(connection2, this, true);
        grid = new Grid(16, 64);

        actTimer = new Timer();

    }

    public void start() {
        Log.info("start()");

        actTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                act();
            }
        }, actDelay, tickInterval);
    }

    public void act() {
        ticks++;

        player1.act(tickInterval);
        player2.act(tickInterval);


        Packet.PacketBattlefieldUnitsUpdate battlefieldUpdatePacket1 = new Packet.PacketBattlefieldUnitsUpdate();
        battlefieldUpdatePacket1.Player1PackedUnits = player1.getPackedUnits(false);
        battlefieldUpdatePacket1.Player2PackedUnits = player2.getPackedUnits(false);
        player1.connection.sendTCP(battlefieldUpdatePacket1);

        Packet.PacketBattlefieldUnitsUpdate battlefieldUpdatePacket2 = new Packet.PacketBattlefieldUnitsUpdate();
        battlefieldUpdatePacket2.Player1PackedUnits = player2.getPackedUnits(true);
        battlefieldUpdatePacket2.Player2PackedUnits = player1.getPackedUnits(true);
        player2.connection.sendTCP(battlefieldUpdatePacket2);

        player1.handleDeadUnits();
        player2.handleDeadUnits();

        if (ticks * tickInterval >= spawnInterval) {
            Log.info("wave spawned");
            ticks = 0;
            player1.spawnWave();
            player2.spawnWave();
            player1.connection.sendTCP(new Packet.PacketWaveSpawned());
            player2.connection.sendTCP(new Packet.PacketWaveSpawned());
        }

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
