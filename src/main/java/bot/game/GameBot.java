package bot.game;

import bot.InputBot;
import sx.blah.discord.api.IDiscordClient;

public class GameBot extends InputBot{

    public GameBot(IDiscordClient client){
        super(client);
    }

    public void playGame(Game game){
        game.start();
    }

    public void quitGame(){

    }
}
