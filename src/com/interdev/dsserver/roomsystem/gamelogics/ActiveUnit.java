package com.interdev.dsserver.roomsystem.gamelogics;


import com.interdev.dsserver.roomsystem.Player;

public class ActiveUnit extends Unit {

    public Player enemyPlayer;

    private ActiveUnit targetUnit = null;
    private boolean haveTargetToMove = false;
    private boolean haveTargetToAttack = false;

    private int attackDeltaTime = 0;

    public ActiveUnit(short x, short y, short type, Player enemyPlayer, int id) {
        super(x, y, type);
        initUnitTypeValues();

        this.id = id;
        this.enemyPlayer = enemyPlayer;

    }

    public void act(float deltaTime) {
        if (!haveTargetToAttack && !haveTargetToMove) {
            if (findTargetToAttack()) {
                attack(deltaTime);
            } else if (findTargetToMove()) {
                moveToTarget(deltaTime);
            } else {
                moveForward(deltaTime);
            }
        } else if (haveTargetToAttack) {
            attack(deltaTime);
        } else if (haveTargetToMove) {
            moveToTarget(deltaTime);
        }
    }

    private void attack(float deltaTime) {
        attackDeltaTime += deltaTime;
        if (attackDeltaTime >= atk_interval) {
            attackDeltaTime -= atk_interval;
            targetUnit.lives -= damage;
            if (targetUnit.lives <= 0) {
                haveTargetToAttack = false;
                haveTargetToMove = false;
            }
        }

    }

    private boolean findTargetToAttack() {

        return false;
    }

    private boolean findTargetToMove() {
        boolean foundTarget = false;
        float minDistanceSquare = Float.MAX_VALUE;
        float sqrDist;

        for (ActiveUnit unit : enemyPlayer.activeUnitsList) {
            if (isUnitAlive(unit) && isReachable(unit, sight_distance)) {
                sqrDist = (float) (Math.pow(x - unit.x, 2) + Math.pow(y - unit.y, 2));
                if (sqrDist < minDistanceSquare) {
                    minDistanceSquare = sqrDist;
                    haveTargetToMove = true;
                    targetUnit = unit;
                    foundTarget = true;
                }
            }
        }
        if (!foundTarget) targetUnit = null;
        return foundTarget;
    }

    private boolean isReachable(ActiveUnit unit, short distance) {
        return (Math.abs(x - unit.x) <= distance && (Math.abs(y - unit.y) <= distance));
    }

    private void moveForward(float deltaTime) {
        float secondDivider = deltaTime / 1000f;
        if (enemyPlayer.baseAtTheTop) {
            this.y += this.walk_speed * secondDivider;
        } else {
            this.y -= this.walk_speed * secondDivider;
        }
    }

    private void moveToTarget(float deltaTime) {
        float secondDivider = deltaTime / 1000f;

        float walk_component = walk_speed*secondDivider;
        float walk_diag_xy_component = diag_walk_speed_xy_component*secondDivider;

        if (x < targetUnit.x && y < targetUnit.y) {
            x += walk_diag_xy_component;
            y += walk_diag_xy_component;
            if (x > targetUnit.x) x = targetUnit.x;
            if (y > targetUnit.y) y = targetUnit.y;
        } else if (x > targetUnit.x && y > targetUnit.y) {
            x -= walk_diag_xy_component;
            y -= walk_diag_xy_component;
            if (x < targetUnit.x) x = targetUnit.x;
            if (y < targetUnit.y) y = targetUnit.y;
        } else if (x < targetUnit.x && y > targetUnit.y) {
            x += walk_diag_xy_component;
            y -= walk_diag_xy_component;
            if (x > targetUnit.x) x = targetUnit.x;
            if (y < targetUnit.y) y = targetUnit.y;
        } else if (x > targetUnit.x && y < targetUnit.y) {
            x -= walk_diag_xy_component;
            y += walk_diag_xy_component;
            if (x < targetUnit.x) x = targetUnit.x;
            if (y > targetUnit.y) y = targetUnit.y;
        } else if(x < targetUnit.x) {
            x += walk_component;
            if ( x > targetUnit.x) x = targetUnit.x;
        } else if(x > targetUnit.x) {
            x -= walk_component;
            if ( x < targetUnit.x) x = targetUnit.x;
        } else if(y < targetUnit.y) {
            y += walk_component;
            if (y > targetUnit.y) y = targetUnit.y;
        } else if(y > targetUnit.y) {
            y -= walk_component;
            if (y < targetUnit.y) y = targetUnit.y;
        }

        if (isReachable(targetUnit, atk_range)) {
            haveTargetToAttack = true;
        }
    }

    private void step(short deltaX, short deltaY) {

    }


    private boolean isUnitAlive(ActiveUnit unit) {
        return (unit.lives > 0);
    }

    public int getTargetId() {
        if (!haveTargetToAttack || targetUnit == null) return 0;
        return targetUnit.id;
    }

}
