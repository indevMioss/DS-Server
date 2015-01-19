package com.interdev.dsserver.roomsystem.gamelogics;


import com.interdev.dsserver.roomsystem.Player;
import com.interdev.dsserver.roomsystem.Room;

public class ActiveUnit extends Unit {

    public Player enemyPlayer;
    public int id;
    public int targetId = 0; //no target

    private boolean haveTarget = false;

    private ActiveUnit targetUnit = null;
    private int deltaTime = 0;

    public ActiveUnit(short x, short y, short type, Player enemyPlayer, int id) {
        super(x, y, type);
        this.id = id;

        this.enemyPlayer = enemyPlayer;

    }

    public void act() {
        deltaTime += Room.tickInterval;

        if(deltaTime >= atk_interval) {
            deltaTime = 0;
            if(!haveTarget) {
                if(findTarget()) attack(targetUnit);
            } else if (isUnitAlive(targetUnit) && isDistanceReachable(targetUnit)) {
                attack(targetUnit);
            } else {
                haveTarget = false;
            }
        }

        if (!haveTarget) move();
    }

    private void move() {
        if (enemyPlayer.baseAtTheTop) {
            this.x += this.walk_speed;
        } else {
            this.x -= this.walk_speed;
        }
    }

    private boolean isDistanceReachable(ActiveUnit unit) {
        return (Math.pow(unit.x*unit.x + unit.y*unit.y, 0.5f) <= atk_range);
    }

    private boolean isUnitAlive(ActiveUnit unit) {
        return (unit.lives > 0);
    }

    private boolean findTarget() {
        boolean foundTarget = false;
        for(ActiveUnit unit : enemyPlayer.activeUnitsList) {
            if(isUnitAlive(unit) && isDistanceReachable(unit)) {
                haveTarget = true;
                targetUnit = unit;
                targetId = unit.id;
                foundTarget = true;
            }
        }
        if (!foundTarget) targetId = 0;
        return foundTarget;
    }

    private void attack(ActiveUnit unit)  {
        if(unit == null) {
            haveTarget = false;
        } else {
            unit.lives -= damage;
        }
    }
}
