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

        this.id = id;
        this.enemyPlayer = enemyPlayer;

    }

    public void act(float deltaTime) {
        if (!haveTargetToAttack && !haveTargetToMove) {
            if (findTargetToMove()) {
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
            targetUnit.getDamage(damage);
            if (targetUnit.lives <= 0) {
                haveTargetToAttack = false;
                haveTargetToMove = false;
            }
        }
    }

    public void getDamage(int damage) {
        this.lives -= damage;
        if (this.lives <= 0) {
            enemyPlayer.myRoom.grid.clearCells(this);
        }
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
        float y_destination = 0;
        if (enemyPlayer.baseAtTheTop) {
            y_destination = y + this.walk_speed * secondDivider;
        } else {
            y_destination = y - this.walk_speed * secondDivider;
        }
        if (enemyPlayer.myRoom.grid.occupy(this, this.x, (short) y_destination))
            this.y = (short) y_destination;
    }


    private void moveToTarget(float deltaTime) {
        if (targetUnit != null && targetUnit.lives > 0) {
            float x_destination = 0;
            float y_destination = 0;

            float secondDivider = deltaTime / 1000f;

            float walk_component = walk_speed * secondDivider;
            float walk_diag_xy_component = diag_walk_speed_xy_component * secondDivider;

            if (x < targetUnit.x && y < targetUnit.y) {
                x_destination = x + walk_diag_xy_component;
                y_destination = y + walk_diag_xy_component;
                if (x > targetUnit.x) x_destination = targetUnit.x;
                if (y > targetUnit.y) y_destination = targetUnit.y;
            } else if (x > targetUnit.x && y > targetUnit.y) {
                x_destination = x - walk_diag_xy_component;
                y_destination = y - walk_diag_xy_component;
                if (x < targetUnit.x) x_destination = targetUnit.x;
                if (y < targetUnit.y) y_destination = targetUnit.y;
            } else if (x < targetUnit.x && y > targetUnit.y) {
                x_destination = x + walk_diag_xy_component;
                y_destination = y - walk_diag_xy_component;
                if (x > targetUnit.x) x_destination = targetUnit.x;
                if (y < targetUnit.y) y_destination = targetUnit.y;
            } else if (x > targetUnit.x && y < targetUnit.y) {
                x_destination = x - walk_diag_xy_component;
                y_destination = y + walk_diag_xy_component;
                if (x < targetUnit.x) x_destination = targetUnit.x;
                if (y > targetUnit.y) y_destination = targetUnit.y;
            } else if (x < targetUnit.x) {
                x_destination = x + walk_component;
                if (x > targetUnit.x) x_destination = targetUnit.x;
            } else if (x > targetUnit.x) {
                x_destination = x - walk_component;
                if (x < targetUnit.x) x_destination = targetUnit.x;
            } else if (y < targetUnit.y) {
                y_destination = y + walk_component;
                if (y > targetUnit.y) y_destination = targetUnit.y;
            } else if (y > targetUnit.y) {
                y_destination = y - walk_component;
                if (y < targetUnit.y) y_destination = targetUnit.y;
            } else return;
            if (enemyPlayer.myRoom.grid.occupy(this, (short) x_destination, (short) y_destination)) {
                x = (short) x_destination;
                y = (short) y_destination;
            }

            if (isReachable(targetUnit, atk_range)) {
                haveTargetToAttack = true;
            }
        } else {
            haveTargetToMove = false;
        }

    }


    private boolean isUnitAlive(ActiveUnit unit) {
        return (unit.lives > 0);
    }

    public int getTargetId() {
        if (!haveTargetToAttack || targetUnit == null) return 0;
        return targetUnit.id;
    }

}
