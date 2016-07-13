package bot.game;

import bot.InputBot;
import sx.blah.discord.handle.obj.IGuild;

public class GameBot extends InputBot{

    public GameBot(IGuild guild){
        super(guild);
    }

    public void playGame(Game game){
        game.start();
    }

    public void quitGame(){

    }
}
