package com.interdev.dsserver;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

/*
    Все операции с пакетами. NetworkListener немного разросся по функционалу(Добавил поля Lobby и ConnectionsManager)
    так что переименовал в просто Network. Не знаю насколько правильно хранить тут поля с лобби и менеджером соединений.
 */
public class Network extends Listener {
    public static ConnectionsManager connectionsManager;
    public static Lobby lobby;


    public Network() {
        connectionsManager = new ConnectionsManager();
        lobby = new Lobby();
    }

    public void connected(Connection connection) {
        Log.info("Кто-то вошел в лобби");
        lobby.addWaitingPlayer(new WaitingPlayer(connection));
    }

    public void disconnected(Connection connection) {
        if (lobby.removeWaitingPlayerByConnection(connection)) {
            Log.info("Кто-то отключился от лобби");
        } else {
            if(connectionsManager.removePair(connection)) {
                Log.info("Кто-то отключился от игры");
            } else {
                Log.info("Кто-то отключился хер пойми откуда");
            }
        }
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