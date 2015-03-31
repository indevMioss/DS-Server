package com.interdev.dsserver.roomsystem.gamelogics;


import com.interdev.dsserver.roomsystem.Player;

import java.util.Random;

public class ActiveUnit extends Unit implements Targetable {

    public Player enemyPlayer; //просто ссылка на второго игрока, полезно

    private Targetable targetUnit = null; //ссылка на юнита, которого будем брать в цель для движения/атаки
    private boolean haveTargetToMove = false; //метка того, что найдена цель для движения
    private boolean haveTargetToAttack = false; //метка того, что найдена цель для атаки

    private int attackDeltaTime = 0; //счётчик времени чтобы знать когда ударять/стрелять вражеского юнита (а то каждый такт бил бы)

    private Random random = new Random();
    private int yMovementDirection;

    public ActiveUnit(short x, short y, short type, Player enemyPlayer, int id) {
        super(x, y, type);

        this.id = id;
        this.enemyPlayer = enemyPlayer;

        if (enemyPlayer.baseAtTheTop) {
            yMovementDirection = 1;
        } else {
            yMovementDirection = -1;
        }

    }

    //метод вызывается каждый такт, входная точка логики юнита
    public void act(float deltaTime) {
        if (isAlive()) {
            if (!haveTargetToAttack && !haveTargetToMove) { // если у тебя нет ни цели для атаки, ни цели для движения к ней..
                if (findTargetToMove()) { // ..то попробуй найти цель для движения!
                    moveToTarget(deltaTime); // если нашел - иди к ней.
                } else { // если же её нет
                    moveForward(deltaTime); // то просто иди вперёд
                }
            } else if (haveTargetToAttack) { //если же цель для атаки всё-таки была..
                attack(deltaTime); // ..то бей её!
            } else if (haveTargetToMove) { //если цели для атаки не было, но была цель для движения..  [warning у IDE тут походу т.к. просто можно else-if убрать и будет то же самое, оставим как есть для наглядности]
                moveToTarget(deltaTime); // ..то иди же к ней!
            }
        }
    }

    //метод для раздачи подзатыльников
    private void attack(float deltaTime) {
        attackDeltaTime += deltaTime;
        if (attackDeltaTime >= atk_interval) {
            attackDeltaTime -= atk_interval;
            targetUnit.getDamage(damage);
            if (!targetUnit.isAlive()) {
                haveTargetToAttack = false;
                haveTargetToMove = false;
            }
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

    public void getDamage(int damage) {
        this.lives -= damage;
        if (!this.isAlive()) {
            enemyPlayer.myRoom.grid.clearCells(this);
        }
    }

    //метод нахождения ближайшей цели для движения
    private boolean findTargetToMove() {
        boolean foundTarget = false;
        float minDistanceSquare = Float.MAX_VALUE;
        float sqrDist;

        for (int i = 0; i < enemyPlayer.fightingUnitsList.size() + 1; i++) {
            Targetable unit;
            if (i < enemyPlayer.fightingUnitsList.size()) {
                unit = enemyPlayer.fightingUnitsList.get(i);
            } else {
                unit = enemyPlayer.base;
            }

            if (unit.isAlive() && isReachable(unit.getX(), unit.getY(), sight_distance)) {
                sqrDist = (float) (Math.pow(x - unit.getX(), 2) * 2 + Math.pow(y - unit.getY(), 2));
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

    //метод для проверки в зоне ли досягаемости юнит [для экономии, зона поиска квадратная, а не круглая]
    private boolean isReachable(short targX, short targY, short distance) {
        return (Math.abs(x - targX) <= distance && (Math.abs(y - targY) <= distance));
    }

    //метод для движения тупо вперед, если нет цели для атаки/движения
    private void moveForward(float deltaTime) {
        float secondDivider = deltaTime / 1000f;
        short walk_component = (short) (walk_speed * secondDivider);

        short y_destination = (short) (y + yMovementDirection * walk_component);
        if (enemyPlayer.myRoom.grid.occupy(this, this.x, y_destination)) {
            this.y = y_destination;
            if (way_blocked) {
                if (random.nextBoolean()) {
                    randomSidemoveDirection *= -1;
                }
            }
        } else {
            goAround(deltaTime);
        }
    }

    //метод для движения к цели, работает до тех пор, пока цель не окажется в зоне досягаемости для атаки
    private void moveToTarget(float deltaTime) {
        if (targetUnit != null && targetUnit.isAlive()) {
            float x_destination = x; //будущие новые координаты
            float y_destination = y; //будущие новые координаты

            float secondDivider = deltaTime / 1000f; //множитель для постоянной скорости чтобы не зависеть от тикрейта сервера

            float walk_component = walk_speed * secondDivider; //на сколько меняем координаты за один шаг если идём строго прямо/назад/влево/направо
            float walk_diag_xy_component = diag_walk_speed_xy_component * secondDivider; //на сколько меняем координаты за один шаг если идём наискосок

            if (x < targetUnit.getX() && y < targetUnit.getY()) {
                x_destination = x + walk_diag_xy_component;
                y_destination = y + walk_diag_xy_component;
                if (x > targetUnit.getX()) x_destination = targetUnit.getX();
                if (y > targetUnit.getY()) y_destination = targetUnit.getY();
            } else if (x > targetUnit.getX() && y > targetUnit.getY()) {
                x_destination = x - walk_diag_xy_component;
                y_destination = y - walk_diag_xy_component;
                if (x < targetUnit.getX()) x_destination = targetUnit.getX();
                if (y < targetUnit.getY()) y_destination = targetUnit.getY();
            } else if (x < targetUnit.getX() && y > targetUnit.getY()) {
                x_destination = x + walk_diag_xy_component;
                y_destination = y - walk_diag_xy_component;
                if (x > targetUnit.getX()) x_destination = targetUnit.getX();
                if (y < targetUnit.getY()) y_destination = targetUnit.getY();
            } else if (x > targetUnit.getX() && y < targetUnit.getY()) {
                x_destination = x - walk_diag_xy_component;
                y_destination = y + walk_diag_xy_component;
                if (x < targetUnit.getX()) x_destination = targetUnit.getX();
                if (y > targetUnit.getY()) y_destination = targetUnit.getY();
            } else if (x < targetUnit.getX()) {
                x_destination = x + walk_component;
                if (x > targetUnit.getX()) x_destination = targetUnit.getX();
            } else if (x > targetUnit.getX()) {
                x_destination = x - walk_component;
                if (x < targetUnit.getX()) x_destination = targetUnit.getX();
            } else if (y < targetUnit.getY()) {
                y_destination = y + walk_component;
                if (y > targetUnit.getY()) y_destination = targetUnit.getY();
            } else if (y > targetUnit.getY()) {
                y_destination = y - walk_component;
                if (y < targetUnit.getY()) y_destination = targetUnit.getY();
            } else {
                System.out.println("else { else { else { ... ");
                return;
            }

            if (enemyPlayer.myRoom.grid.occupy(this, (short) x_destination, (short) y_destination)) {
                x = (short) x_destination;
                y = (short) y_destination;
                if (way_blocked) {
                    if (random.nextBoolean()) {
                        randomSidemoveDirection *= -1;
                    }
                }
            } else {
                // движение в бок поиск пути
                goAround(deltaTime);
            }

            if (isReachable(targetUnit.getX(), targetUnit.getY(), atk_range)) {
                haveTargetToAttack = true;
            }
        } else {
            haveTargetToMove = false;
        }
    }

    private void goAround(float deltaTime) {
        float secondDivider = deltaTime / 1000f;
        float walk_component = walk_speed * secondDivider;

        short y_destination = (short) (y + yMovementDirection * walk_component);
        if (enemyPlayer.myRoom.grid.occupy(this, x, y_destination)) {
            y = y_destination;
        } else {
            short x_destination = (short) (x + walk_component * randomSidemoveDirection);
            if (enemyPlayer.myRoom.grid.occupy(this, x_destination, y)) {
                this.x = x_destination;
            } else {
                randomSidemoveDirection *= -1;
            }
        }

    }

    public boolean isAlive() {
        return (lives > 0);
    }

    //метод для отправки пакетов
    public int getTargetId() {
        if (!haveTargetToAttack || targetUnit == null) return 0;
        return targetUnit.getID();
    }

}
