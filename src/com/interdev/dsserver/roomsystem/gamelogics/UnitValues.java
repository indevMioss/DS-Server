package com.interdev.dsserver.roomsystem.gamelogics;

public class UnitValues {

    private static UnitVal unit1 = new Unit1Values();
    private static UnitVal unit2 = new Unit1Values();
    private static UnitVal unit3 = new Unit1Values();
    private static UnitVal unit4 = new Unit1Values();

    public static UnitVal getByType(short type) {
        switch (type) {
            case 1:
                return unit1;
            case 2:
                return unit2;
            case 3:
                return unit3;
            case 4:
                return unit4;
        }
        return unit1;
    }

    public static class UnitVal {
        public short price;
        public short lives;
        public short damage;
        public short atk_range; // 0 - ближний бой
        public float atk_speed; // раз в X ms
        public short walk_speed; // виртуальных px/s
        public short texture_width;
    }

    private static class Unit1Values extends UnitVal {
        private Unit1Values() {
            price = 100;
            lives = 60;
            damage = 5;
            atk_range = 0; // 0 - ближний бой
            atk_speed = 300; // раз в X ms
            walk_speed = 50; // виртуальных px/s
            texture_width = 80;
        }

    }
}
