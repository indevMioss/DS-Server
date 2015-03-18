package com.interdev.dsserver.roomsystem.gamelogics;

/**
 * Created by Evg256 on 14.02.2015.
 */
public class Grid {
    public Cell grid[][];
    public static final int cell_size = 64;
    public int x_size;
    public int y_size;

    public Grid(int x_size, int y_size) {
        this.x_size = x_size;
        this.y_size = y_size;
        grid = new Cell[y_size][x_size];
        int tx = 0, ty = 0;
        for (int i = 0; i < y_size; i++) {
            for (int j = 0; j < x_size; j++) {
                grid[i][j] = new Cell(tx, ty);
                tx += cell_size;
            }
            ty += cell_size;
            tx = 0;
        }
    }

    public boolean occupy(ActiveUnit unit, short x_destination, short y_destination) {
        if((x_destination - unit.texture_width / 2) < 0 || (x_destination + unit.texture_width / 2) > PlayerValues.BATTLEFIELD_WIDTH) {
            unit.way_blocked = false;
            return  false;
        }

        if((y_destination - unit.texture_width / 2) < 0 || (y_destination + unit.texture_width / 2) > PlayerValues.TOTAL_FIELD_HEIGHT) {
            unit.way_blocked = false;
            return false;
        }

        short start_y = (short) (y_destination + unit.texture_width / 2);
        short start_x = (short) (x_destination - unit.texture_width / 2);

        short end_y = (short) (y_destination - unit.texture_width / 2);
        short end_x = (short) (x_destination + unit.texture_width / 2);


        short y_start_index = (short) (start_y / cell_size);
        short x_start_index = (short) (start_x / cell_size);


        short y_end_index = (short) (end_y / cell_size);
        short x_end_index = (short) (end_x / cell_size);




        /*
        проверка, свободны ли клетки в конечной точке
        */
        for (int i = 0; i <= Math.abs(y_start_index - y_end_index); i++) {
            for (int j = 0; j <= Math.abs(x_start_index - x_end_index); j++) {
                try {
                    if (grid[y_start_index - i][x_start_index + j].owner != null && grid[y_start_index - i][x_start_index + j].owner != unit) {
                        unit.way_blocked = false;
                        return false;
                    }
                } catch (IndexOutOfBoundsException e) {
                    System.err.println("IndexOutOfBoundsException caught");
                    return false;
                }
            }
        }

        //очистка занятых ячеек
        clearCells(unit);

        //заполнение сетки юнитом
        short occupy_cells_val = 0;
        for (int i = 0; i <= Math.abs(y_start_index - y_end_index); i++) {
            for (int j = 0; j <= Math.abs(x_start_index - x_end_index); j++) {
                grid[y_start_index - i][x_start_index + j].owner = unit;
                unit.occupy_cells_list[occupy_cells_val] = grid[y_start_index - i][x_start_index + j];
                occupy_cells_val++;
            }
        }
        unit.way_blocked = true;
        return true;
    }


    public void clearCells(ActiveUnit unit) {
        for (int i = 0; i < unit.occupy_cells_list.length; i++) {
            if (unit.occupy_cells_list[i] != null) {
                unit.occupy_cells_list[i].owner = null;
            }
        }
    }


    public class Cell {
        public ActiveUnit owner = null;
        public int x;
        public int y;

        public Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

}
