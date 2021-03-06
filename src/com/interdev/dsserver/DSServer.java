package com.interdev.dsserver;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;
import com.interdev.dsserver.roomsystem.PackedCell;

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

        kryo.register(PackedCell.class);
        kryo.register(PackedCell[].class);
        kryo.register(PackedCell[][].class);
        kryo.register(Packet.PacketCellsDebug.class);

        kryo.register(Packet.PacketLoginAnswer.class);
        kryo.register(Packet.PacketRoomReady.class);
        kryo.register(Packet.PacketRoomDestroyed.class);

        kryo.register(PackedBase.class);
        kryo.register(PackedUnit.class);
        kryo.register(PackedUnit[].class);
        kryo.register(Packet.PacketGameUpdate.class);
        kryo.register(Packet.PacketWaveSpawned.class);
        kryo.register(Packet.PacketAnswerUnitPurchase.class);
        kryo.register(Packet.PacketAnswerUnitSell.class);
        kryo.register(Packet.PacketAnswerUpgrade.class);

        kryo.register(Packet.PacketLoginRequest.class);
        kryo.register(Packet.PacketReadyToPlay.class);
        kryo.register(Packet.PacketRequestUnitPurchase.class);
        kryo.register(Packet.PacketRequestUnitSell.class);
        kryo.register(Packet.PacketRequestUpgrade.class);

    }


    public static void main(String[] args) {
        try {
            new DSServer();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
