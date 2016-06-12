package bot;

import bot.commands.AnnotationListener;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.HTTP429Exception;


public class NewBot
{
    private static IDiscordClient client;

    public static void main(String[] args) throws DiscordException, HTTP429Exception{
        client = new ClientBuilder().withToken("MTkxMzk3NDkzNDE2Nzg3OTY4.Cj5sTg.-gE8kl3iH_c7NSUp17zwzJ9-K-w").login();

        client.getDispatcher().registerListener(new AnnotationListener());
    }

    public static IDiscordClient getClient(){
        return client;
    }
}


