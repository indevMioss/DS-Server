package com.interdev.dsserver.roomsystem.gamelogics;


public class PlayerValues {
    public static final float INCOME_UPGRADE_PRICE = 100;

    //скорость добычи на X lvl (ресурса в сек.)
    public static final float INCOME_LVL1 = 10;
    public static final float INCOME_LVL2 = 11;
    public static final float INCOME_LVL3 = 12;
    public static final float INCOME_LVL4 = 13;
    public static final float INCOME_LVL5 = 14;

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
