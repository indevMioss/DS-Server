package com.interdev.dsserver;

import com.interdev.dsserver.roomsystem.PackedCell;

public class Packet {

    public static class PacketCellsDebug {
        public PackedCell cells[][];
    }

    // Server ->> Client // ----------------------------------------------

    public static class PacketLoginAnswer {
        public boolean accepted = false;
    }

    public static class PacketRoomReady {
        public int tickInterval;
    }

    public static class PacketRoomDestroyed {
    }

    public static class PacketGameUpdate {
        public int money;
        public PackedUnit[] Player1PackedUnits;
        public PackedUnit[] Player2PackedUnits;
    }

    public static class PacketWaveSpawned {
    }

    public static class PacketAnswerUnitPurchase {
        public int id; // 0 - покупка невозможна
    }

    public static class PacketAnswerUnitSell {
        public boolean answer;
    }

    public static class PacketAnswerUpgrade {
        public boolean answer;
    }
    // Client ->> Server // ------------------------------------------------

    public static class PacketLoginRequest {
    }
    public static class PacketReadyToPlay {
    }

    public static class PacketRequestUnitPurchase {
        public short type;
        public short x;
        public short y;
    }


    public static class PacketRequestUnitSell {
        public int id;
    }

    public static class PacketRequestUpgrade {
        public short type;
    }

}
