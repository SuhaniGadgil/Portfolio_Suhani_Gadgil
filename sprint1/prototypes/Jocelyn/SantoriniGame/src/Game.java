import java.util.Scanner;

public class Game
{
    Board board;
    Player[] players;
    int currentPlayerIndex;

    public Game(String player1, String player2) {
        board = new Board();
        players = new Player[]
                {
                new Player(player1, "A"),
                new Player(player2, "B")
                };
        currentPlayerIndex = 0;
    }

    public boolean move(Worker worker, int a, int b)
    {
        if (!board.isInBoard(a, b))
        {
            return false;
        }

        Cell target = board.grid[a][b];
        if (target.isOccupied() || target.hasDome)
        {
            return false;
        }

        int levelDiff = target.level - board.grid[worker.a][worker.b].level;
        if (levelDiff > 1)
        {
            return false;
        }

        board.grid[worker.a][worker.b].worker = null;
        worker.a = a;
        worker.b = b;
        target.worker = worker;
        if (target.level == 3)
        {
            System.out.println("Congratulations" + worker.player.name + ", you win!");
            System.exit(0);
        }

        return true;
    }

    public boolean build(Worker worker, int a, int b)
    {
        if (!board.isInBoard(a, b))
        {
            return false;
        }

        Cell target = board.grid[a][b];
        if (target.isOccupied() || target.hasDome || (a == worker.a && b == worker.b))
        {
            return false;
        }

        if (target.level < 3)
        {
            target.level++;
        }
        else
        {
            target.hasDome = true;
        }

        return true;
    }

    public void nextTurn()
    {
        currentPlayerIndex = 1 - currentPlayerIndex;
    }

    public Player getCurrentPlayer()
    {
        return players[currentPlayerIndex];
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        for (Player player : players)
        {
            System.out.println(player.name + ", place your 2 workers.");
            for (int i = 0; i < 2; i++)
            {
                while (true)
                {
                    System.out.print("Enter position for Worker " + (i + 1) + " (e.g., B2): ");
                    String coor = scanner.next();
                    int[] parsed = parseCoordinate(coor);

                    if (parsed == null)
                    {
                        System.out.println("Invalid position. Try again.");
                        continue;
                    }

                    int a = parsed[0];
                    int b = parsed[1];

                    if (!board.isInBoard(a, b))
                    {
                        System.out.println("Invalid position. Try again.");
                    }
                    else if (board.grid[a][b].isOccupied())
                    {
                        System.out.println("This position is occupied. Try again.");
                    }
                    else
                    {
                        Worker w = new Worker(player, a, b);
                        board.grid[a][b].worker = w;
                        player.workers[i] = w;
                        break;
                    }
                }
            }
        }

        while (true)
        {
            Player current = getCurrentPlayer();
            System.out.println("\n" + current.name + "'s turn.");
            printBoard();

            Worker chosen = null;
            while (true) {
                System.out.print("Select Worker 1 or 2: ");
                int index = scanner.nextInt();
                if (index == 1 || index == 2)
                {
                    chosen = current.workers[index - 1];
                    break;
                }
                else
                {
                    System.out.println("Invalid selection. Enter 1 or 2.");
                }
            }

            while (true)
            {
                System.out.print("Move to (e.g., A3): ");
                String coor = scanner.next();
                int[] parsed = parseCoordinate(coor);
                if (parsed != null)
                {
                    int a = parsed[0];
                    int b = parsed[1];
                    if (move(chosen, a, b)) break;
                }
                System.out.println("Invalid move. Try again.");
            }

            while (true)
            {
                System.out.print("Build at (e.g., C4): ");
                String coord = scanner.next();
                int[] parsed = parseCoordinate(coord);
                if (parsed != null)
                {
                    int ba = parsed[0];
                    int bb = parsed[1];
                    if (build(chosen, ba, bb)) break;
                }
                System.out.println("Invalid build. Try again.");
            }

            nextTurn();
        }
    }

    public static int[] parseCoordinate(String input)
    {
        input = input.trim().toUpperCase();
        if (input.length() < 2) return null;

        char colChar = input.charAt(0);
        int col = colChar - 'A';

        int row;
        try
        {
            row = Integer.parseInt(input.substring(1)) - 1;
        }
        catch (NumberFormatException e)
        {
            return null;
        }

        if (col < 0 || col >= Board.SIZE || row < 0 || row >= Board.SIZE)
        {
            return null;
        }

        return new int[] { row, col };
    }

    private String getPlayerSymbol(Player player)
    {
        return player.symbol;
    }

    public void printBoard()
    {
        String horizontalLine = "  +-----+-----+-----+-----+-----+";

        System.out.print("    ");
        for (char col = 'A'; col <= 'E'; col++)
        {
            System.out.print("  " + col + "  ");
        }
        System.out.println();

        for (int i = 0; i < Board.SIZE; i++)
        {
            System.out.println(horizontalLine);

            System.out.print(" " + (i + 1) + " ");

            for (int j = 0; j < Board.SIZE; j++)
            {
                Cell c = board.grid[i][j];
                String content = "";

                if (c.hasDome)
                {
                    content = "D";
                }
                else
                {
                    content = Integer.toString(c.level);
                }

                if (c.worker != null)
                {
                    Player p = c.worker.player;
                    int workerIndex = (p.workers[0] == c.worker) ? 1 : 2;
                    content += p.symbol + workerIndex;
                }
                else
                {
                    content += "  ";
                }

                while (content.length() < 3)
                {
                    content += " ";
                }
                System.out.print("| " + content);

            }

            System.out.println("|");
        }

        System.out.println(horizontalLine);
    }
}
