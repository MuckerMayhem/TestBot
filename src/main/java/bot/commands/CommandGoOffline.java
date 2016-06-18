package bot.commands;

import bot.DiscordBot;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.util.EnumSet;
import java.util.List;

/**
 * Created by Owner on 2016-06-17.
 */
public class CommandGoOffline extends Command
{
    @Override
    public void
    onExecute(DiscordBot bot, IMessage message, String[] args) throws MissingPermissionsException, RateLimitException, DiscordException
    {
        MessageBuilder builder = new MessageBuilder(DiscordBot.instance.getClient());
        builder.withChannel(message.getChannel());
        IGuild guild = bot.getClient().getGuildByID("189563086418608130");
      List<IRole> roles = message.getAuthor().getRolesForGuild(guild);
        for(IRole r : roles)
        {
            EnumSet<Permissions> perms = r.getPermissions();
            if(perms.contains(Permissions.MANAGE_SERVER))
            {
                bot.getClient().logout();
                break;
            }
            else
            {
                builder.appendContent("Invalid permissions").build();
                break;
            }
        }
    }
}

