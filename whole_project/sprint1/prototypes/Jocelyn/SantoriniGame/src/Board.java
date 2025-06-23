public class Board
{
    public static final int SIZE = 5;
    Cell[][] grid;

    public Board()
    {
        grid = new Cell[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++)
        {
            for (int j = 0; j < SIZE; j++)
            {
                grid[i][j] = new Cell();
            }
        }
    }

    public boolean isInBoard(int x, int y)
    {
        return x >= 0 && y >= 0 && x < SIZE && y < SIZE;
    }
}

