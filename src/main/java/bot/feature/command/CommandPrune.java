package bot.feature.command;

import bot.DiscordBot;
import bot.locale.Message;
import bot.locale.MessageBuilder;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import util.DiscordUtil;

import java.util.List;
import java.util.stream.Collectors;

public class CommandPrune extends BotCommand{

    public CommandPrune(){
        super("prune", Permissions.MANAGE_MESSAGES);
    }

    @Override
    public void onRegister() {}

    @Override
    public void onEnable(DiscordBot bot) {}

    @Override
    public void onDisable(DiscordBot bot) {}
    
    @Override
    public void onExecute(DiscordBot bot, IMessage message, String[] args) throws Exception{
        if(args.length == 0) return;
        
        final IUser target = args.length >= 2 ? DiscordUtil.getUserByMention(message.getGuild(), args[1]) : null;
        
        MessageBuilder builder = new MessageBuilder(bot.getLocale());
        
        message.getChannel().getMessages().load(100);
        List<IMessage> affectedMessages = message.getChannel().getMessages().stream()
                .filter(m -> m.getContent().toLowerCase().contains(args[0].toLowerCase()))
                .filter(m -> target == null || target == m.getAuthor())
                .collect(Collectors.toList());
        
        message.getChannel().getMessages().bulkDelete(affectedMessages);
        
        bot.say(message.getChannel(), message.getAuthor().mention() + " " + builder.buildMessage(Message.CMD_PRUNE_DELETED, affectedMessages.size()), 3000L);
    }
}
