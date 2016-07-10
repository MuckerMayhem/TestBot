package bot.commands;

import bot.DiscordBot;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import util.DiscordUtil;

import java.util.List;
import java.util.stream.Collectors;

public class CommandPrune extends Command{
    @Override
    protected void onRegister(){

    }

    @Override
    public void onExecute(DiscordBot bot, IMessage message, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException{
        if(args.length == 0) return;

        final IUser target = args.length >= 2 ? DiscordUtil.getUserByMention(message.getGuild(), args[1]) : null;

        List<IMessage> affectedMessages = message.getChannel().getMessages().stream()
                .filter(m -> m.getContent().toLowerCase().contains(args[0].toLowerCase()))
                .filter(m -> target == null || target == m.getAuthor())
                .collect(Collectors.toList());
        message.getChannel().getMessages().bulkDelete(affectedMessages);
        bot.say(message.getChannel(), message.getAuthor().mention() + " " + affectedMessages.size() + " messages deleted.", 3000L);
    }
}
