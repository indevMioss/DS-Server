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
        for(int i = 0; i < y_size; i ++) {
            for(int j = 0; j < x_size; j ++) {
                grid[i][j] = new Cell(tx, ty);
                tx += cell_size;
            }
            ty += cell_size;
            tx = 0;
        }
        for(int i = 0; i < y_size; i ++) {
            for (int j = 0; j < x_size; j++) {
                if(grid[i][j].owner != null)
                    System.out.print('1');
                else
                    System.out.print('0');
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
    }

    public boolean occupy(ActiveUnit unit, short x_destination, short y_destination) {
        short y_index = (short) (unit.y/ cell_size);
        short x_index = (short) (unit.x / cell_size);
        short y_index_dest = (short) (y_destination/ cell_size);
        short x_index_dest = (short) (x_destination / cell_size);

        /*
        for(int i = 0; i < y_size; i ++) {
            for (int j = 0; j < x_size; j++) {
                if(grid[i][j].owner != null)
                    System.out.print('1');
                else
                    System.out.print('0');
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
        */
        /*
        проверка, свободны ли клетки в конечной точке
        */
        for(int i = 0; i < unit.size_in_cells; i ++) {
            for(int j = 0; j < unit.size_in_cells; j ++) {
                if (grid[y_index_dest - i][x_index_dest + j].owner != null && grid[y_index_dest - i][x_index_dest + j].owner != unit) {
                    return false;
                }
            }
        }
        //очистка занятых ячеек
        clearCells(unit);

        //заполнение сетки юнитом
        short occupy_cells_val = 0;
        for(int i = 0; i < unit.size_in_cells; i ++) {
            for(int j = 0; j < unit.size_in_cells; j ++) {
                grid[y_index - i][x_index + j].owner = unit;
                unit.occupy_cells_list[occupy_cells_val] = grid[y_index - i][x_index + j];
                occupy_cells_val ++;
            }
        }
        unit.x_index = x_index_dest;
        unit.y_index = y_index_dest;
        return true;
    }


    public void clearCells(ActiveUnit unit) {
        for(int i = 0; i < unit.occupy_cells_list.length; i ++) {
            if(unit.occupy_cells_list[i] != null) {
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
