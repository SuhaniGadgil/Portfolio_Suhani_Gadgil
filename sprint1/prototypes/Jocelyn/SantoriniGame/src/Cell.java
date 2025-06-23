public class Cell
{
    int level;
    boolean hasDome;
    Worker worker;

    public Cell()
    {
        this.level = 0;
        this.hasDome = false;
        this.worker = null;
    }

    public boolean isOccupied()
    {
        return worker != null;
    }
}
