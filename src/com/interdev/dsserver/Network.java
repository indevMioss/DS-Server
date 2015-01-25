package com.interdev.dsserver;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import com.interdev.dsserver.lobby.Lobby;
import com.interdev.dsserver.lobby.WaitingPlayer;
import com.interdev.dsserver.roomsystem.Player;
import com.interdev.dsserver.roomsystem.RoomManager;

/*
    Все операции с пакетами. NetworkListener немного разросся по функционалу(Добавил поля Lobby и ConnectionsManager)
    так что переименовал в просто Network. Не знаю насколько правильно хранить тут поля с лобби и менеджером соединений.
 */
public class Network extends Listener {
    public static RoomManager roomManager;
    public static Lobby lobby;


    public Network() {
        roomManager = new RoomManager();
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
            if (roomManager.destroyRoom(connection)) {
                Log.info("Кто-то отключился от игры");
            } else {
                Log.info("Кто-то отключился хер пойми откуда");
            }
        }
    }

    public void received(Connection connection, Object obj) {
        if (obj instanceof Packet.PacketLoginRequest) {
            Packet.PacketLoginAnswer loginAnswer = new Packet.PacketLoginAnswer();
            loginAnswer.accepted = true;
            connection.sendTCP(loginAnswer);
        } else if (obj instanceof Packet.PacketReadyToPlay) {
            Player player = roomManager.findPlayer(connection);
            if (player != null) {
                player.iAmReady();
            }
        } else if (obj instanceof Packet.PacketRequestUnitPurchase) {
            roomManager.findPlayer(connection).onUnitPurchaseRequest((Packet.PacketRequestUnitPurchase) obj);
        } else if (obj instanceof Packet.PacketRequestUnitSell) {
            roomManager.findPlayer(connection).onUnitSellRequest((Packet.PacketRequestUnitSell) obj);
        } else if (obj instanceof Packet.PacketRequestUpgrade) {
            roomManager.findPlayer(connection).onUpgradeRequest((Packet.PacketRequestUpgrade) obj);
        }
    }

}
