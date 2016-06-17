package bot.game;

import bot.DiscordBot;
import sx.blah.discord.api.IDiscordClient;

public class GameBot extends DiscordBot{

    public GameBot(IDiscordClient client){
        super(client);
    }

    public void playGame(Game game){
        game.start();
    }

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
}
