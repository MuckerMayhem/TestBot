package bot.listeners;

import bot.BotParameters;
import bot.DiscordBot;
import sx.blah.discord.handle.impl.events.UserVoiceChannelLeaveEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.time.ZonedDateTime;


public class OnLeaveListener
{
//    @EventSubscriber
    public void onUserLeaveEvent(UserVoiceChannelLeaveEvent event) throws MissingPermissionsException, RateLimitException, DiscordException
    {
        if(event.getUser().isBot())
            return;

        MessageBuilder builder = new MessageBuilder(DiscordBot.instance.getClient());
        ZonedDateTime zdt = ZonedDateTime.now();

        int hour = 0;
        String s = "";

        if (zdt.getHour() > 12) //cause I know someones gonna complain if it's military time.
        {
            hour = zdt.getHour() - 12;
            s = "PM";
        }
        else
        {
            hour = zdt.getHour();
            s = "AM";
        }


        for (IChannel c : DiscordBot.instance.getClient().getGuildByID(BotParameters.GUILD_ID).getChannels())
        {
            if (c.getName().equals(BotParameters.HOME))
            {
                builder.withChannel(c);
                builder.appendContent("User " + event.getUser().getName() + " has disconnected from the " + event.getChannel().getName() + " channel at "
                        + hour + ":" + zdt.getMinute() + " " + s + " PST").build();

            }
        }


    }
}
