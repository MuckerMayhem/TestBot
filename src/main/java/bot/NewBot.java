package bot;

import bot.chatter.ConversationListener;
import bot.commands.CommandAttitude;
import bot.commands.CommandTest;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.HTTP429Exception;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;


public class NewBot
{
    public static final String BOT_HOME = "bot"; //Channel the bot will speak in

    private static IDiscordClient client;

    public static void main(String[] args) throws DiscordException, HTTP429Exception{
        client = new ClientBuilder().withToken("MTkxMzk3NDkzNDE2Nzg3OTY4.Cj5sTg.-gE8kl3iH_c7NSUp17zwzJ9-K-w").login();

        client.getDispatcher().registerListener(new ConversationListener()); //Conversation listener
        client.getDispatcher().registerListener(new CommandTest()); //Test command
        client.getDispatcher().registerListener(new CommandAttitude());//Display bot attitude towards user
    }

    public static IDiscordClient getClient(){
        return client;
    }

    public static void say(String message){
        try{
            new MessageBuilder(client).withChannel("191670114121285632").withContent(message).build();
        }
        catch(DiscordException | HTTP429Exception | MissingPermissionsException e){
            e.printStackTrace();
        }
    }
}


