package com.interdev.dsserver.roomsystem;

import com.esotericsoftware.kryonet.Connection;
import com.interdev.dsserver.Packet;

import java.util.ArrayList;
import java.util.Iterator;

/*
    Менеджер румов

 */
public class RoomManager {

    private ArrayList<Room> rooms;

    public RoomManager() {
        rooms = new ArrayList<Room>();
    }

    public boolean makeNewRoom(Connection connection1, Connection connection2) {
        if(containsConnection(connection1) || containsConnection(connection2)) return false;

        rooms.add(new Room(connection1, connection2));

        Packet.PacketRoomReady packet = new Packet.PacketRoomReady();
        packet.tickInterval = Room.tickInterval;
        connection1.sendTCP(packet);
        connection2.sendTCP(packet);
        return true;
    }

    public boolean destroyRoom(Connection connection) {
        Iterator<Room> it = rooms.iterator();
        while (it.hasNext()) {
            Room room = it.next();
            if (room.player1.connection.equals(connection) || room.player2.connection.equals(connection)) {
                room.destroy();
                it.remove();
                return true;
            }
        }
        return false;
    }

    public Player findPlayer(Connection connection) {
        for (Room room : rooms) {
            if(room.player1.connection.equals(connection)) {
                return room.player1;
            } else if(room.player2.connection.equals(connection)) {
                return room.player2;
            }
        }
        return null;
    }

    private boolean containsConnection(Connection connection) {
        for (Room room : rooms) {
            if(room.player1.connection.equals(connection) || room.player2.connection.equals(connection)) {
                return true;
            }
        }
        return false;
    }

}






