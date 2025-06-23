public class Player
{
    String name;
    String symbol;
    Worker[] workers;

    public Player(String name, String symbol)
    {
        this.name = name;
        this.symbol = symbol;
        this.workers = new Worker[2];
    }
}

