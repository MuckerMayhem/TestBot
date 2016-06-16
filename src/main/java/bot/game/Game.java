package bot.game;

public abstract class Game{

    protected GameBot bot;

    public Game(GameBot bot){
        this.bot = bot;
    }

    public GameBot getBot(){
        return this.bot;
    }

    public abstract void start();
}
