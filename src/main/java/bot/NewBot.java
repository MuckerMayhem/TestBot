package bot;

import bot.commands.AnnotationListener;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.HTTP429Exception;

import java.util.ArrayList;
import java.util.List;


public class NewBot
{
    private static IDiscordClient client;

    public static void main(String[] args) throws DiscordException, HTTP429Exception, Exception
    {
        bot.chatterbotapi.ChatterBotFactory factory = new bot.chatterbotapi.ChatterBotFactory();

        bot.chatterbotapi.ChatterBot bot1 = factory.create(bot.chatterbotapi.ChatterBotType.PANDORABOTS, "f326d0be8e345a13");

        bot.chatterbotapi.ChatterBotSession bot1session = bot1.createSession();
        List<bot.chatterbotapi.ChatterBotSession> list = new ArrayList<>();

        client = new ClientBuilder().withToken("MTkyMTc3OTU5MjYyNDg2NTI4.CkFDKw.uDdFbxl8fxx5vY6QOUBR-CJwGn0").login();

        client.getDispatcher().registerListener(new AnnotationListener(bot1session));
    }

    public static IDiscordClient getClient()
    {
        return client;
    }
}


