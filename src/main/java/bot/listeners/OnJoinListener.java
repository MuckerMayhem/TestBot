package bot.listeners;

import bot.BotParameters;
import bot.DiscordBot;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.UserVoiceChannelJoinEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.time.ZonedDateTime;


public class OnJoinListener
{
    @EventSubscriber
    public void onJoinEvent(UserVoiceChannelJoinEvent event) throws MissingPermissionsException, RateLimitException, DiscordException
    {
        if(event.getUser().isBot())
            return;


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

        MessageBuilder builder = new MessageBuilder(DiscordBot.instance.getClient());
        System.out.println(event.getUser().getName());
        if (event.getUser().getName().equals("Mucker"))
        {
            for (IChannel c : DiscordBot.instance.getClient().getGuildByID(BotParameters.GUILD_ID).getChannels())
            {
                if (c.getName().equals("general"))
                    builder.withChannel(c).appendContent("Mucker-chan-senpai has arrived.").build();
            }


        }
        else
        {
            for (IChannel c : DiscordBot.instance.getClient().getGuildByID(BotParameters.GUILD_ID).getChannels())
            {
                if (c.getName().equals(BotParameters.HOME))
                {
                    builder.withChannel(c);
                    builder.appendContent("User " + event.getUser().getName() + " has connected to the " + event.getChannel().getName() + " channel at "
                            + hour + ":" + zdt.getMinute() + " " + s + " PST").build();

                }
            }
        }
    }
}
