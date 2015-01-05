package com.interdev.dsserver;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

public class NetworkListener extends Listener {

    public void connected(Connection connection) {
        Log.info("[SERVER] Someone has connected");
    }

    public void disconnected(Connection connection) {
        Log.info("[SERVER] Someone has disconnected");
    }

    public void received(Connection connection, Object obj) {

        if(obj instanceof Packet.Packet0LoginRequest) {
            Packet.Packet1LoginAnswer loginAnswer = new Packet.Packet1LoginAnswer();
            loginAnswer.accepted = true;
            connection.sendTCP(loginAnswer);
        }

        if(obj instanceof Packet.Packet2Message) {
            String message = ((Packet.Packet2Message) obj).message;
            Log.info(message);
        }
    }

}
