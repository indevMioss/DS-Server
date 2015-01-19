package com.interdev.dsserver.roomsystem;

import com.esotericsoftware.kryonet.Connection;

import java.util.ArrayList;

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
        return true;
    }

    public boolean destroyRoom(Connection connection) {
        for (Room room : rooms) {
            if(room.player1.connection.equals(connection)) {
                room.player2.connection.close();  //если отключился 1й игрок, то дисконнектим и второго
                rooms.remove(room);
                return true;
            }
            if(room.player2.connection.equals(connection)) {
                room.player1.connection.close(); //если отключился 2й игрок, то дисконнектим и первого
                rooms.remove(room);
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






