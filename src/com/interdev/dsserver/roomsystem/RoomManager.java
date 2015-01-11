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
            if(room.connection1.equals(connection)) {
                room.connection2.close();  //если отключился 1й игрок, то дисконнектим и второго
                rooms.remove(room);
                return true;
            }
            if(room.connection2.equals(connection)) {
                room.connection1.close(); //если отключился 2й игрок, то дисконнектим и первого
                rooms.remove(room);
                return true;
            }
        }
        return false;
    }


    private boolean containsConnection(Connection connection) {
        for (Room room : rooms) {
            if(room.connection1.equals(connection) || room.connection2.equals(connection)) {
                return true;
            }
        }
        return false;
    }

}






