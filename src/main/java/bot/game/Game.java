package bot.game;

public abstract class Game{

    protected GameBot bot;

    public Game(GameBot bot){
        this.bot = bot;
    }

    public abstract boolean isMultiplayer();

    public abstract boolean isReplayable();

    public abstract void play();

    public abstract void quit();

    public GameBot getBot(){
        return this.bot;
    }

    public void start(){
        String input = "";
        do{
            play();
            if(!isReplayable()) return;
            this.bot.say("Want to play again?");
            input = this.bot.nextLine();
        }
        while(input.toLowerCase().startsWith("y"));
    }
}
