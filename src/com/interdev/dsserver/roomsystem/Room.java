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

    public static final int gridSizeX = 16;
    public static final int gridSizeY = 64;


    public int unitsTicks = 0;
    public int incomeTicks = 0;

    public Player player1, player2;

    public Grid grid;

    private PackedCell[][] packedCells; // DEBUG

    public Timer actTimer;

    public Room(Connection connection1, Connection connection2) {
        player1 = new Player(connection1, this, false);
        player2 = new Player(connection2, this, true);

        grid = new Grid(gridSizeX, gridSizeY);
        packedCells = new PackedCell[gridSizeY][gridSizeX]; // DEBUG

        actTimer = new Timer();

    }

    public void start() {
        Log.info("start()");
        player1.connection.sendTCP(new Packet.PacketWaveSpawned()); //для синхронизации таймеров
        player2.connection.sendTCP(new Packet.PacketWaveSpawned());

        actTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                act();
            }
        }, actDelay, tickInterval);
    }


    public void act() {
        unitsTicks++;
        incomeTicks++;

        player1.act(tickInterval);
        player2.act(tickInterval);

        if (incomeTicks * tickInterval >= 1000) {
            System.out.println("tick income " + incomeTicks);
            player1.money += player1.income;
            player2.money += player2.income;
            incomeTicks = 0;
        }


        for (int i = 0; i < gridSizeY; i++) {
            for (int j = 0; j < gridSizeX; j++) {
                boolean free = (grid.grid[i][j].owner == null);
                packedCells[i][j] = new PackedCell((short) grid.grid[i][j].x, (short) grid.grid[i][j].y, free);
            }
        }

        Packet.PacketCellsDebug packetCellsDebug = new Packet.PacketCellsDebug();
        packetCellsDebug.cells = packedCells;
        player1.connection.sendTCP(packetCellsDebug);

        Packet.PacketGameUpdate gameUpdatePacket1 = new Packet.PacketGameUpdate();
        gameUpdatePacket1.money = player1.money;
        gameUpdatePacket1.Player1PackedUnits = player1.getPackedUnits(false);
        gameUpdatePacket1.Player2PackedUnits = player2.getPackedUnits(false);
        player1.connection.sendTCP(gameUpdatePacket1);

        Packet.PacketGameUpdate gameUpdatePacket2 = new Packet.PacketGameUpdate();
        gameUpdatePacket2.money = player2.money;
        gameUpdatePacket2.Player1PackedUnits = player2.getPackedUnits(true);
        gameUpdatePacket2.Player2PackedUnits = player1.getPackedUnits(true);
        player2.connection.sendTCP(gameUpdatePacket2);

        player1.handleDeadUnits();
        player2.handleDeadUnits();



        if (unitsTicks * tickInterval >= spawnInterval) {
            Log.info("wave spawned");
            unitsTicks = 0;
            player1.spawnWave();
            player2.spawnWave();
            player1.connection.sendTCP(new Packet.PacketWaveSpawned());
            player2.connection.sendTCP(new Packet.PacketWaveSpawned());
        }

    }

    public void baseIsDead(Player player) {
        if (player == player1) {
            //player 2 win
        } else if(player == player2) {
            //player 1 win
        } else {
            System.err.println("ERROR public void baseIsDead(Player player) - player is unknown");
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

    public void destroy() {
        actTimer.cancel();
        player1.connection.sendTCP(new Packet.PacketRoomDestroyed());
        player2.connection.sendTCP(new Packet.PacketRoomDestroyed());
    }

}
