package bot.game;

public abstract class Game{

    protected GameBot bot;

    public Game(GameBot bot){
        this.bot = bot;
    }

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

    public abstract void play();

    public abstract void quit();

    public abstract boolean isReplayable();
}
