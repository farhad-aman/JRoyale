public class ElixirBar 
{
    /**
     * the elixir limit for elixir bar 
     */
    private final int elixirLimit = 10;

    /**
     * the amount of elixir adds every frame
     */
    private final double slowStep = 0.0125;

    /**
     * the amount of elixir adds every frame in last minute of the battle
     */
    private final double fastStep = 0.025;
    
    /**
     * the current amount of elixir
     */
    private double elixir;
    
    /**
     * if any elixir should be taken from elixir
     */
    private int nextFrameTakeElixir;

    /**
     * creates a new elixir bar
     */
    public ElixirBar()
    {
        this.elixir = 6;
        this.nextFrameTakeElixir = 0;
    }

    /**
     * the main step of elixir bar done every frame
     */
    public void step()
    {
        if(nextFrameTakeElixir > 0)
        {
            elixir -= nextFrameTakeElixir;
            nextFrameTakeElixir = 0;
        }
        if(elixir >= elixirLimit)
        {
            elixir = elixirLimit;
        }
        else
        {
            if(GameManager.getInstance().getBattle().getBattleTimer().getTime() <= 60)
            {
                elixir += fastStep;
            }
            else 
            {
                elixir += slowStep;
            }
        }
    }

    /**
     * sets next frame take elixir
     * @param cost
     * @return true if work done else false
     */
    public boolean takeExir(int cost)
    {
        if(elixir < cost)
        {
            return false;
        }
        else
        {
            this.nextFrameTakeElixir = cost;
            return true;
        }
    }

    /**
     * @return current amount of elixir
     */
    public double getElixir()
    {
        return elixir;
    }
}
