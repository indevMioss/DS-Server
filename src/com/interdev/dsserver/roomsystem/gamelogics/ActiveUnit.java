package com.interdev.dsserver.roomsystem.gamelogics;


import com.interdev.dsserver.roomsystem.Player;

import java.util.Random;

public class ActiveUnit extends Unit {

    public Player enemyPlayer; //просто ссылка на второго игрока, полезно

    private ActiveUnit targetUnit = null; //ссылка на юнита, которого будем брать в цель для движения/атаки
    private boolean haveTargetToMove = false; //метка того, что найдена цель для движения
    private boolean haveTargetToAttack = false; //метка того, что найдена цель для атаки

    private int attackDeltaTime = 0; //счётчик времени чтобы знать когда ударять/стрелять вражеского юнита (а то каждый такт бил бы)

    private Random random = new Random();


    public ActiveUnit(short x, short y, short type, Player enemyPlayer, int id) {
        super(x, y, type);

        this.id = id;
        this.enemyPlayer = enemyPlayer;

    }

    //метод вызывается каждый такт, входная точка логики юнита
    public void act(float deltaTime) {
        if (!haveTargetToAttack && !haveTargetToMove) { // если у тебя нет ни цели для атаки, ни цели для движения к ней..
            if (findTargetToMove()) { // ..то попробуй найти цель для движения!
                moveToTarget(deltaTime); // если нашел - ебашь к ней.
            } else { // если же её нет
                moveForward(deltaTime); // то просто пиздуй вперёд
            }
        } else if (haveTargetToAttack) { //если же цель для атаки всё-таки была..
            attack(deltaTime); // ..то ебашь её!
        } else if (haveTargetToMove) { //если цели для атаки не было, но была цель для движения..  [warning у IDE тут походу т.к. просто можно else-if убрать и будет то же самое, оставим как есть для наглядности]
            moveToTarget(deltaTime); // ..то пиздуй к ней!
        }
    }

    //метод для раздачи пиздюлей
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

    //метод для получения пиздюлей
    public void getDamage(int damage) {
        this.lives -= damage;
        if (this.lives <= 0) {
            enemyPlayer.myRoom.grid.clearCells(this);
        }
    }

    //метод нахождения ближайшей цели для движения
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

    //метод для проверки в зоне ли досягаемости юнит, который передаём 1м арументом, 2 аргумент - дистанция [для экономии, зона поиска квадратная, а не круглая]
    private boolean isReachable(ActiveUnit unit, short distance) {
        return (Math.abs(x - unit.x) <= distance && (Math.abs(y - unit.y) <= distance));
    }

    //метод для движения тупо вперед, если нет цели для атаки/движения
    private void moveForward(float deltaTime) {
        float secondDivider = deltaTime / 1000f;
        float y_destination;
        if (enemyPlayer.baseAtTheTop) {
            y_destination = y + this.walk_speed * secondDivider;
        } else {
            y_destination = y - this.walk_speed * secondDivider;
        }
        if (enemyPlayer.myRoom.grid.occupy(this, this.x, (short) y_destination)) {
            this.y = (short) y_destination;
            if(way_blocked) {
                if (random.nextBoolean()) {
                    side *= -1;
                }
            }
        } else {
            // движение в бок поиск пути
            moveSideWay(deltaTime);
        }
    }

    //метод для движения к цели, работает до тех пор, пока цель не окажется в зоне досягаемости для атаки
    private void moveToTarget(float deltaTime) {
        if (targetUnit != null && targetUnit.lives > 0) {
            float x_destination = x; //будущие новые координаты
            float y_destination = y; //будущие новые координаты

            float secondDivider = deltaTime / 1000f; //множитель для постоянной скорости чтобы не зависеть от тикрейта сервера

            float walk_component = walk_speed * secondDivider; //на сколько меняем координаты за один шаг если идём строго прямо/назад/влево/направо
            float walk_diag_xy_component = diag_walk_speed_xy_component * secondDivider; //на сколько меняем координаты за один шаг если идём наискосок

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
            } else {
                System.out.println("else { else { else { ... ");
                return;
            }

            if (enemyPlayer.myRoom.grid.occupy(this, (short) x_destination, (short) y_destination)) {
                x = (short) x_destination;
                y = (short) y_destination;
                if(way_blocked) {
                    if (random.nextBoolean()) {
                        side *= -1;
                    }
                }
            } else {
                // движение в бок поиск пути
                moveSideWay(deltaTime);
            }

            if (isReachable(targetUnit, atk_range)) {
                haveTargetToAttack = true;
            }
        } else {
            haveTargetToMove = false;
        }
    }

    void moveSideWay(float deltaTime) {
        float x_destination = x; //будущие новые координаты
        float secondDivider = deltaTime / 1000f; //множитель для постоянной скорости чтобы не зависеть от тикрейта сервера
        float walk_component = walk_speed * secondDivider; //на сколько меняем координаты за один шаг если идём строго прямо/назад/влево/направо
        x_destination += walk_component;
        if (enemyPlayer.baseAtTheTop) {
            x_destination = x + (this.walk_speed * secondDivider) * side;
        } else {
            x_destination = x - (this.walk_speed * secondDivider) * side;
        }
        if (enemyPlayer.myRoom.grid.occupy(this, (short) x_destination, this.y)) {
            this.x = (short) x_destination;
        } else {
            side *= -1;
        }

    }

    //проверка, живой ли юнит
    private boolean isUnitAlive(ActiveUnit unit) {
        return (unit.lives > 0);
    }

    //метод для отправки пакетов
    public int getTargetId() {
        if (!haveTargetToAttack || targetUnit == null) return 0;
        return targetUnit.id;
    }

}
