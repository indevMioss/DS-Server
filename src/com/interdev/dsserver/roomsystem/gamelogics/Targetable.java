package com.interdev.dsserver.roomsystem.gamelogics;

/**
 * Created by Amaz on 24.03.2015.
 */
public interface Targetable {
    public short getX();
    public short getY();
    public int getID();
    public boolean isAlive();
    public void getDamage(int damage);

}
