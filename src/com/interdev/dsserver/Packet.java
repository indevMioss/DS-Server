package com.interdev.dsserver;

/*
    Список пакетов которые можно пересылать. Пока еще не знаю полностью что тут будет,
    и особенно как LoginRequest юзать и нужен ли он вообще нам.
 */
public class Packet {
    public static class PacketLoginRequest {
    }

    public static class PacketLoginAnswer {
        public boolean accepted = false;
    }

    public static class PacketMessage {
        public String message;
    }

    public static class PacketReadyToPlay {
    }

    public static class PacketBattlefieldUnitsUpdate {
        public PackedUnit[] Player1PackedUnits;
        public PackedUnit[] Player2PackedUnits;
    }

    public static class PacketDeadUnitsIDs {
        public int[] deadUnitsIDs;
    }

    public static class PacketWaveSpawned {
    }

    public static class PacketRequestUnitPurchase {
        public short type;
        public short x;
        public short y;
    }

    public static class PacketAnswerUnitPurchase {
        public int id; // 0 - покупка невозможна
    }

    public static class PacketRequestUnitSell {
        public int id;
    }

    public static class PacketAnswerUnitSell {
        public boolean answer;
    }
}
