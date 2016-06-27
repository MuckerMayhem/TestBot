package bot.commands;

import bot.DiscordBot;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.util.List;
import java.util.stream.Collectors;

public class CommandPrune extends Command{
    @Override
    public void onExecute(DiscordBot bot, IMessage message, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException{
        if(args.length == 0) return;
        List<IMessage> affectedMessages = message.getChannel().getMessages().stream()
                .filter(m -> m.getContent().toLowerCase().contains(String.join(" ", args).toLowerCase()))
                .collect(Collectors.toList());
        message.getChannel().getMessages().bulkDelete(affectedMessages);
        bot.say(message.getChannel(), affectedMessages.size() + " messages deleted.");
    }
}
