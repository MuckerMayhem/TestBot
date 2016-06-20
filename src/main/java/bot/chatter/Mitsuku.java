package bot.chatter;

import bot.DiscordBot;
import bot.chatterbotapi.ChatterBot;
import bot.chatterbotapi.ChatterBotFactory;
import bot.chatterbotapi.ChatterBotSession;
import bot.chatterbotapi.ChatterBotType;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

/**
 * Created by Owner on 2016-06-16.
 */
public class Mitsuku
{
    ChatterBotFactory factory = new ChatterBotFactory();

    ChatterBot bot1 = factory.create(ChatterBotType.PANDORABOTS, "f326d0be8e345a13");
    ChatterBotSession bot1session = bot1.createSession();

    public Mitsuku() throws Exception
    {
    }


    //@EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event) throws RateLimitException, DiscordException, MissingPermissionsException
    {
        MessageBuilder builder = new MessageBuilder(DiscordBot.instance.getClient());
        if(event.getMessage().getChannel().getName().equals("bot"))
        {
            builder.withChannel(event.getMessage().getChannel());
            String s = event.getMessage().getContent();
            try
            {
                s = bot1session.think(s);
                builder.appendContent(s).build();

            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }
}
