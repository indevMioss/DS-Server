package com.interdev.dsserver.roomsystem.gamelogics;


import com.interdev.dsserver.roomsystem.Player;

public class ActiveUnit extends Unit {

    public Player enemyPlayer;
    public int targetId = 0; //no target

    private boolean haveTarget = false;

    private ActiveUnit targetUnit = null;
    private int attackDeltaTime = 0;

    public ActiveUnit(short x, short y, short type, Player enemyPlayer, int id) {
        super(x, y, type);
        initUnitTypeValues();

        this.id = id;
        this.enemyPlayer = enemyPlayer;

    }

    public void act(float deltaTime) {
        attackDeltaTime += deltaTime;

        if(attackDeltaTime >= atk_interval) {
            attackDeltaTime = 0;
            if(!haveTarget) {
                if(findTarget()) attack(targetUnit);
            } else if (isUnitAlive(targetUnit) && isDistanceReachable(targetUnit)) {
                attack(targetUnit);
            } else {
                haveTarget = false;
            }
        }

        if (!haveTarget) move(deltaTime);
    }

    private void move(float deltaTime) {
        float secondDivider = deltaTime/1000f;
        if (enemyPlayer.baseAtTheTop) {
            this.y += this.walk_speed*secondDivider;
        } else {
            this.y -= this.walk_speed*secondDivider;
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
