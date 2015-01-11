package com.interdev.dsserver.lobby;

import com.esotericsoftware.kryonet.Connection;

/*
    Ожидающий в лобби игрок. Используется только в лобби.
 */
public class WaitingPlayer {
    public Connection connection;

    public WaitingPlayer(Connection connection) {
        this.connection = connection;
    }
}
