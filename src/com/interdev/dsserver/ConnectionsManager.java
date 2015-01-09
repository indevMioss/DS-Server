package com.interdev.dsserver;

import com.esotericsoftware.kryonet.Connection;

import java.util.ArrayList;

/*
    Прототип системы румов.
    По сути просто хранит записи о соединениях, которые должны находится в парах.
 */
public class ConnectionsManager {

    private ArrayList<ConnectionPair> connectionPairsList;

    public ConnectionsManager() {
        connectionPairsList = new ArrayList<ConnectionPair>();
    }

    public Connection getOppositeConnection(Connection connection) {
        for (ConnectionPair connectionPair : connectionPairsList) {
            if(connectionPair.connection1.equals(connection)) {
                return connectionPair.connection2;
            }
            if(connectionPair.connection2.equals(connection)) {
                return connectionPair.connection1;
            }
        }
        return null;
    }


    public boolean addPair(Connection connection1, Connection connection2) {
        if(containsConnection(connection1) || containsConnection(connection2)) return false;

        connectionPairsList.add(new ConnectionPair(connection1, connection2));
        return true;
    }

    public boolean removePair(Connection connection) {
        for (ConnectionPair connectionPair : connectionPairsList) {
            if(connectionPair.connection1.equals(connection)) {
                connectionPair.connection2.close();  //если отключился 1й игрок, то дисконнектим и второго
                connectionPairsList.remove(connectionPair);
                return true;
            }
            if(connectionPair.connection2.equals(connection)) {
                connectionPair.connection1.close(); //если отключился 2й игрок, то дисконнектим и первого
                connectionPairsList.remove(connectionPair);
                return true;
            }
        }
        return false;
    }

    private boolean containsConnection(Connection connection) {
        for (ConnectionPair connectionPair : connectionPairsList) {
            if(connectionPair.connection1.equals(connection) || connectionPair.connection2.equals(connection)) {
                return true;
            }
        }
        return false;
    }

}
