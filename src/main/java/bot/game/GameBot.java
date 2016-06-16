package bot.game;

import bot.DiscordBot;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;

public class GameBot extends DiscordBot implements IListener<MessageReceivedEvent>{

    public GameBot(IDiscordClient client){
        super(client);
        client.getDispatcher().registerListener(this);
    }

    public void playGame(Game game){
        game.start();
    }

    public String nextLine(){
        String last = getHome().getMessages().get(0).getContent();
        String next = last;
        while(next.equals(last)){
            next = getHome().getMessages().get(0).getContent();
        }
        return next;
    }

    @Override
    public void handle(MessageReceivedEvent event){

    }
}
