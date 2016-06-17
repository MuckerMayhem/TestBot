package bot.chatter;

import bot.DiscordBot;
import sx.blah.discord.api.events.EventSubscriber;
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
    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event) throws RateLimitException, DiscordException, MissingPermissionsException
    {
        MessageBuilder builder = new MessageBuilder(DiscordBot.instance.getClient());
        if(event.getMessage().getChannel().getName().equals("bot"))
        {
            builder.withChannel(event.getMessage().getChannel());
            String s = event.getMessage().getContent();
            try
            {
//                s = DiscordBot.session.think(s);
                builder.appendContent(s).build();

            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }
}
