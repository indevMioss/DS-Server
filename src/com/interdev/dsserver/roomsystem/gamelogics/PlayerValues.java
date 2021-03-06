package com.interdev.dsserver.roomsystem.gamelogics;


public final class PlayerValues {
    public static final int BATTLEFIELD_WIDTH = 1024;
    public static final int BATTLEFIELD_HEIGHT = 2048;

    public static final int PERSONALFIELD_WIDTH = 1024;
    public static final int PERSONALFIELD_HEIGHT = 1024;
    public static final int TOTAL_FIELD_HEIGHT = BATTLEFIELD_HEIGHT + PERSONALFIELD_HEIGHT * 2;

    public static final float WAVE_SPAWN_INTERVAL = 20000;
    public static final float INCOME_UPGRADE_PRICE = 100;
    public static final int START_MONEY = 4000;

    public static final int TOP_BASE_ID = 1;
    public static final int BOTTOM_BASE_ID = -1;
    public static int MY_BASE_ID; //initiates on client after a packet baseAtTheTop from server
    public static int ENEMY_BASE_ID;

    public static final int BASE_START_LIVES = 5000;
    public static final int BASE_DAMAGE = 80;
    public static final int BASE_ATTACK_INTERVAL = 1000;
    public static final int BASE_ATTACK_DISTANCE = BATTLEFIELD_WIDTH/4;

    public static final float PERSONAL_FIELD_BORDER = 0.01f;

    //скорость добычи на X lvl (ресурса в сек.)
    public static final int INCOME_LVL1 = 10;
    public static final int INCOME_LVL2 = 11;
    public static final int INCOME_LVL3 = 12;
    public static final int INCOME_LVL4 = 13;
    public static final int INCOME_LVL5 = 14;

    //минимальное время между апрейдами
    public static final int INCOME_LVL2_BEFORE_UPGRADE_DELAY = 60000;
    public static final int INCOME_LVL3_BEFORE_UPGRADE_DELAY = 80000;
    public static final int INCOME_LVL4_BEFORE_UPGRADE_DELAY = 100000;
    public static final int INCOME_LVL5_BEFORE_UPGRADE_DELAY = 120000;

    //время приостановки добычи после апгрейда
    public static final int INCOME_BEFORE_LVL2_UPGRADE_SUSPENSE = 20000;
    public static final int INCOME_BEFORE_LVL3_UPGRADE_SUSPENSE = 30000;
    public static final int INCOME_BEFORE_LVL4_UPGRADE_SUSPENSE = 40000;
    public static final int INCOME_BEFORE_LVL5_UPGRADE_SUSPENSE = 60000;



}
