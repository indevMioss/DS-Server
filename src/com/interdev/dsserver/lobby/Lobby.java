package com.interdev.dsserver.lobby;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.minlog.Log;
import com.interdev.dsserver.Network;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/*
   Лобби. Хранит список игроков ожидающих попарного соединения для совместной игры
   Так же осуществляет периодическую проверку при помощи таймера на наличие как
   минимум двух игроков чтобы ассоциировать их соединения. После - удаляет из
   списка игроков в лобби.
 */
public class Lobby {
    private static final int timerCheckInterval = 2000; //ms
    private static final int timerStartDelay = 3000;
    public ArrayList<WaitingPlayer> waitingPlayersList;

    public Lobby() {
        waitingPlayersList = new ArrayList<WaitingPlayer>();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                tryToFormNewRoom();
            }
        }, timerStartDelay, timerCheckInterval);
    }

    public void tryToFormNewRoom() {
        Log.info("Количество игроков в лобби: " + waitingPlayersList.size());
        if (waitingPlayersList.size() < 2) return;

        WaitingPlayer waitingPlayer0 = waitingPlayersList.get(0);
        WaitingPlayer waitingPlayer1 = waitingPlayersList.get(1);

        if (Network.roomManager.makeNewRoom(waitingPlayer0.connection, waitingPlayer1.connection)) {
            removeWaitingPlayer(waitingPlayer0);
            removeWaitingPlayer(waitingPlayer1);
            Log.info("2 игрока объеденены и удалены из списка ожидания");
        } else {
            Log.info("Ошибка в логике? Один или несколько игроков уже в игре.");
        }
    }

    public boolean addWaitingPlayer(WaitingPlayer player) {
        if(waitingPlayersList.contains(player)) {
            return false;
        }

        waitingPlayersList.add(player);
        return true;
    }


    private boolean removeWaitingPlayer(WaitingPlayer player) {
        if(!waitingPlayersList.contains(player)) {
            return false;
        }

        waitingPlayersList.remove(player);
        return true;
    }

    public boolean removeWaitingPlayerByConnection(Connection connection) {
        for (WaitingPlayer player : waitingPlayersList) {
            if(player.connection.equals(connection)) {
                waitingPlayersList.remove(player);
                return true;
            }
        }

        return false;
    }
}
