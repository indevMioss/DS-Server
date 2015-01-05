package com.interdev.dsserver;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import java.io.IOException;

public class DSServer {
    private Server server;

    public DSServer() throws IOException {
        server = new Server();
        registerPackets();
        server.addListener(new NetworkListener());
        server.bind(54555);
        server.start();

    }

    private void registerPackets() {
        Kryo kryo = server.getKryo();
        kryo.register(Packet.Packet0LoginRequest.class);
        kryo.register(Packet.Packet1LoginAnswer.class);
        kryo.register(Packet.Packet2Message.class);
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
