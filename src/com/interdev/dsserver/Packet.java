package com.interdev.dsserver;

/*
    Список пакетов которые можно пересылать. Пока еще не знаю полностью что тут будет,
    и особенно как LoginRequest юзать и нужен ли он вообще нам.
 */
public class Packet {
    public static class Packet0LoginRequest { }
    public static class Packet1LoginAnswer { boolean accepted = false; }
    public static class Packet2Message {String message; }
}
