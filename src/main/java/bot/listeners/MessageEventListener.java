package bot.listeners;

import bot.DiscordBot;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

/**
 * Created by Owner on 2016-06-22.
 */
public class MessageEventListener
{
    private static MessageBuilder builder = new MessageBuilder(DiscordBot.instance.getClient());
    private static int count = 0;
    private static IMessage message;
    private static MessageReceivedEvent lastEvent = new MessageReceivedEvent(message);

    @EventSubscriber
    public void onMessageEvent(MessageReceivedEvent event) throws MissingPermissionsException, RateLimitException, DiscordException
    {


        if(lastEvent.getMessage().getAuthor().equals(event.getMessage().getAuthor()))
        {
            lastEvent = event;
            System.out.println("test");
            count++;
        }
        else
        {
            System.out.println("test3");

            lastEvent = event;
            count = 0;
        }

        if (count == 3)
        {
            builder.appendContent("BREAKING UP THAT SHIT BRUH").build();
            count = 0;
        }
    }

}
