package bot.listeners;

import bot.DiscordBot;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.io.IOException;

import static bot.listeners.test.GetLinkStuff.getVideoLength;

public class YoutubeLinkListener
{

    @EventSubscriber
    public void onMessageEvent(MessageReceivedEvent event) throws IOException, RateLimitException, DiscordException, MissingPermissionsException
    {
        MessageBuilder builder = new MessageBuilder(DiscordBot.instance.getClient());
        String length = "";
        String msg = event.getMessage().getContent();
        if (event.getMessage().getContent().contains("https://www.youtube.com/watch?v="))
        {

            String link = msg.replaceAll(".*(http[s]?://.*)[ ].*", "$1");
            String[] parts = link.split("=");

            length = getVideoLength(parts[1]);
        }
        else if (event.getMessage().getContent().contains("https://youtu.be/"))
        {
            String link = msg.replaceAll(".*(http[s]?://.*)[ ].*", "$1");
            String[] parts = link.split("be/");

            length = getVideoLength(parts[1]);
        }

        length = length.replace("PT", "");
        builder.withChannel(event.getMessage().getChannel()).appendContent(length).build();

    }

}
