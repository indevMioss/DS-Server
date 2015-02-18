package com.interdev.dsserver.roomsystem.gamelogics;

/**
 * Created by Evg256 on 14.02.2015.
 */
public class Grid {
    private Cell grid[][];
    public static final int cell_size = 64;

    public Grid(int x_size, int y_size) {
         grid = new Cell[x_size][y_size];
        int tx = 0, ty = 0;
        for(int i = 0; i < x_size; i ++) {
            for(int j = 0; j < y_size; j ++) {
                grid[i][j] = new Cell(tx, ty);
                tx += cell_size;
            }
            ty += cell_size;
            tx = 0;
        }
    }

    public boolean occupy(ActiveUnit unit, short x_destination, short y_destination) {
        short y_index = (short) (unit.y/ cell_size);
        short x_index = (short) (unit.x / cell_size);
        short y_index_dest = (short) (y_destination/ cell_size);
        short x_index_dest = (short) (x_destination / cell_size);
        //
        for(int i = 0; i < 16; i ++) {
            for (int j = 0; j < 64; j++) {
                if(grid[i][j].owner != null)
                    System.out.print('1');
                else
                    System.out.print('0');
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
        /*
        проверка, свободны ли клетки в конечной точке
        */
        for(int i = 0; i < unit.size_in_cells; i ++) {
            for(int j = 0; j < unit.size_in_cells; j ++) {
                if (grid[x_index_dest - i][y_index_dest + j].owner != null && grid[x_index_dest - i][y_index_dest + j].owner != unit) {
                    return false;
                }
            }
        }
        clearCells(x_index, y_index, unit);
        //заполнение сетки юнитом
        for(int i = 0; i < unit.size_in_cells; i ++) {
            for(int j = 0; j < unit.size_in_cells; j ++) {
                grid[x_index - i][y_index + j].owner = unit;
            }
        }
        unit.x_index = x_index_dest;
        unit.y_index = y_index_dest;
        return true;
    }

    public void clearCells(short x_ind, short y_ind, ActiveUnit unit) {
        //очистка клеток, занятых юнитом
        for(int i = 0; i < unit.size_in_cells; i ++) {
            for(int j = 0; j < unit.size_in_cells; j ++) {
                grid[x_ind - i][y_ind + j].owner = null;
            }
        }
    }


    private final class Cell {
        public ActiveUnit owner = null;
        public int x;
        public int y;

        public Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
