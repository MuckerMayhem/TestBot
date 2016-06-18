package bot.listeners;

import bot.BotParameters;
import bot.DiscordBot;
import sx.blah.discord.handle.impl.events.UserVoiceChannelLeaveEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class OnLeaveListener
{
    //@EventSubscriber
    public void onUserLeaveEvent(UserVoiceChannelLeaveEvent event) throws MissingPermissionsException, RateLimitException, DiscordException
    {

        if (event.getUser().isBot())
            return;


        MessageBuilder builder = new MessageBuilder(DiscordBot.instance.getClient());
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_TIME;
        String text = date.format(formatter);




        for (IChannel c : DiscordBot.instance.getClient().getGuildByID(BotParameters.GUILD_ID).getChannels())
        {
            if (c.getName().equals(BotParameters.HOME))
            {
                builder.appendContent("User " + event.getUser().getName() + " has left the " + event.getChannel().getName() + " channel at "
                        + text).build();
            }
        }


    }
}
