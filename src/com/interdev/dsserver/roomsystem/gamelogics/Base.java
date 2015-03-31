package com.interdev.dsserver.roomsystem.gamelogics;

import com.interdev.dsserver.roomsystem.Player;

/**
 * Created by amaz on 20.03.15.
 */
public class Base implements Targetable{
    private Player enemyPlayer;
    private int id;
    private short x, y;
    private float timePassed = 0;

    public short lives = PlayerValues.BASE_START_LIVES;
    public short damage = PlayerValues.BASE_DAMAGE;
    public short attackInteval = PlayerValues.BASE_ATTACK_INTERVAL;
    public short attackDistance = PlayerValues.BASE_ATTACK_DISTANCE;

    private ActiveUnit targetUnit = null;
    private boolean haveTargetToAttack = false;

    public Base(Player enemyPlayer, boolean atTheTop) {
        if (atTheTop) {
            id = PlayerValues.TOP_BASE_ID;
        } else {
            id = PlayerValues.BOTTOM_BASE_ID;
        }

        this.enemyPlayer = enemyPlayer;
        x = PlayerValues.BATTLEFIELD_WIDTH / 2;

        if (atTheTop) {
            y = (short) (PlayerValues.TOTAL_FIELD_HEIGHT - PlayerValues.PERSONALFIELD_HEIGHT * 1.1f);
        } else {
            y = (short) (PlayerValues.PERSONALFIELD_HEIGHT * 1.1f);
        }
    }

    public void act(float deltaTime) {
        timePassed += deltaTime;

        if (haveTargetToAttack) {
            if (timePassed >= attackInteval && targetUnit != null) {
                targetUnit.getDamage(damage);
                if (!targetUnit.isAlive()) {
                    haveTargetToAttack = false;
                }
                timePassed = 0;
            }
        } else {
            findTargetToAttack();
        }
    }

    public boolean findTargetToAttack() {
        for (ActiveUnit unit : enemyPlayer.fightingUnitsList) {
            if (unit.isAlive() && isReachable(unit, attackDistance)) {
                targetUnit = unit;
                haveTargetToAttack = true;
                return true;
            }
        }
        return false;
    }

    private boolean isReachable(ActiveUnit unit, short distance) {
        return (Math.abs(x - unit.x) <= distance && (Math.abs(y - unit.y) <= distance));
    }

    public void getDamage(int damage) {
        lives -= damage;
        if (!isAlive()) {
            enemyPlayer.myRoom.baseIsDead(enemyPlayer.myRoom.getOppositePlayer(enemyPlayer));
        }
    }

    public short getX() {
        return x;
    }

    public short getY() {
        return y;
    }

    public int getID() {
        return id;
    }
    public boolean isAlive() {
        return lives > 0;
    }

    public int getTargetID() {
        if (haveTargetToAttack) {
            return targetUnit.getID(); //null?
        }
        return 0;
    }
}
