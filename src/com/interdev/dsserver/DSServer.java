package com.interdev.dsserver;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import java.io.IOException;

/*
    Создание и запуск самого сервера
 */
public class DSServer {
    static public final int port = 54555;

    private Server server;

    public DSServer() throws IOException {
        server = new Server();
        registerPackets();
        server.addListener(new Network());
        server.bind(port);
        server.start();

    }

    private void registerPackets() {
        Kryo kryo = server.getKryo();
        kryo.register(Packet.PacketLoginRequest.class);
        kryo.register(Packet.PacketLoginAnswer.class);
        kryo.register(Packet.PacketMessage.class);

        kryo.register(Packet.PacketBattlefieldUnitsUpdate.class);
        kryo.register(Packet.PacketDeadUnitsIDs.class);
        kryo.register(Packet.PacketReadyToPlay.class);
        kryo.register(Packet.PacketWaveSpawned.class);

    }


    public static void main(String[] args) {
        try {
            new DSServer();
            Log.set(Log.LEVEL_DEBUG);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
