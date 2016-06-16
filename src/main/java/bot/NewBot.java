package bot;

import bot.chatter.ConversationListener;
import bot.commands.CommandAttitude;
import bot.commands.CommandLeave;
import bot.commands.CommandSound;
import bot.commands.CommandTest;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;


public class NewBot
{
    public static final String BOT_HOME = "bot"; //Channel the bot will speak in

    private static IDiscordClient client;

    public static void main(String[] args) throws DiscordException, RateLimitException{
//        client = new ClientBuilder().withToken("MTkxNjgyMTU3ODUxMzEyMTM4.CkKaXQ.55yjkyEIqjp0R84WCJdN8lexMVI").login();
        client = new ClientBuilder().withToken("MTkxMzk3NDkzNDE2Nzg3OTY4.CkPSLA.sDU5W0NbZn32gl3eDtFzIhmYV6Q").login();
        client.getDispatcher().registerListener(new ConversationListener()); //Conversation listener
        client.getDispatcher().registerListener(new CommandTest()); //Test command
        client.getDispatcher().registerListener(new CommandAttitude());//Display bot attitude towards user
        client.getDispatcher().registerListener(new CommandSound());
        client.getDispatcher().registerListener(new CommandLeave());
    }

    public static IDiscordClient getClient(){
        return client;
    }

    public static void say(String message){
        try{
            new MessageBuilder(client).withChannel("191670114121285632").withContent(message).build();
        }
        catch(DiscordException | RateLimitException | MissingPermissionsException e){
            e.printStackTrace();
        }
    }
}


