package bot.listeners;

import bot.BotParameters;
import bot.DiscordBot;
import sx.blah.discord.handle.impl.events.UserVoiceChannelJoinEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class OnJoinListener
{
//    @EventSubscriber
    public void onJoinEvent(UserVoiceChannelJoinEvent event) throws MissingPermissionsException, RateLimitException, DiscordException
    {
        if(event.getUser().isBot())
            return;


        MessageBuilder builder = new MessageBuilder(DiscordBot.instance.getClient());
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_TIME;
        String text = date.format(formatter);

//not today
//        System.out.println(event.getUser().getName());
//        if (event.getUser().getName().equals("Mucker"))
//        {
//            for (IChannel c : DiscordBot.instance.getClient().getGuildByID(BotParameters.GUILD_ID).getChannels())
//            {
//                if (c.getName().equals("general"))
//                    builder.withChannel(c).appendContent("Mucker-chan-senpai has arrived.").build();
//            }
//
//
//        }
       // else
        //{
            for (IChannel c : DiscordBot.instance.getClient().getGuildByID(BotParameters.GUILD_ID).getChannels())
            {
                if (c.getName().equals(BotParameters.HOME))
                {
                    builder.withChannel(c);
                    builder.appendContent("User " + event.getUser().getName() + " has connected to the " + event.getChannel().getName() + " channel at "
                           + text).build();

                }
            }
       // }
    }
}
