package com.interdev.dsserver.roomsystem;


import com.esotericsoftware.kryonet.Connection;

public class Room {

    public Connection connection1, connection2;

    public Room(Connection conn1, Connection conn2) {
        connection1 = conn1;
        connection2 = conn2;
    }

    public void act() {

    }


    public Connection getOppositeConnection(Connection connection) {
            if(connection1.equals(connection)) {
                return connection2;
            }
            if(connection2.equals(connection)) {
                return connection1;
            }
        return null;
    }

}
