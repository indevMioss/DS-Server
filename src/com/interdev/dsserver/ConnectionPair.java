package com.interdev.dsserver;

import com.esotericsoftware.kryonet.Connection;

/*
    Объект для хранения пары соединений двух игроков, которые в данный момент играют
 */
public class ConnectionPair {
    public Connection connection1;
    public Connection connection2;

    public ConnectionPair(Connection connection1, Connection connection2) {
        this.connection1 = connection1;
        this.connection2 = connection2;
    }
}
