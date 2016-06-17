package bot.game;

import bot.DiscordBot;
import sx.blah.discord.api.IDiscordClient;

public class GameBot extends DiscordBot{

    public GameBot(IDiscordClient client){
        super(client);
    }

    /**
     * Prompts the bot to wait for user input in its home channel.<br><
     * Any sent by a real user (rather than a bot) will be counted as<br>
     * the next input.
     * @return The next input sent by a non-bot user
     */
    public String nextLine(){
        String last = getHome().getMessages().get(0).getContent();
        String next = last;
        while(next.equals(last)){
            if(!getHome().getMessages().get(0).getAuthor().isBot()){
                next = getHome().getMessages().get(0).getContent();
            }
        }
        return next;
    }

    public void playGame(Game game){
        game.start();
    }

    public void quitGame(){

    }
}
