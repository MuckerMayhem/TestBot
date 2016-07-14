package bot.feature.commands;

import bot.DiscordBot;
import bot.locale.Message;
import bot.locale.MessageBuilder;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import util.DiscordUtil;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class CommandHelp extends BotCommand{

    @Override
    public void onRegister(){

    }

    @Override
    public void onEnable(DiscordBot bot){

    }

    @Override
    public void onDisable(DiscordBot bot){

    }

    @Override
    protected void onExecute(DiscordBot bot, IMessage message, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException{
        MessageBuilder builder = new MessageBuilder(bot.getLocale());

        if(args.length == 0){
            bot.respond("\n" + builder.buildMessage(Message.CMD_HELP_DETAILS, getHandle(bot.getLocale())) + "\n" +
                    String.join("\n", CommandHandler.getAllRegisteredCommands()
                            .stream()
                            .filter(c -> DiscordUtil.userHasPermission(message.getAuthor(), message.getGuild(), c.getRequiredPermissions()))
                            .map(c -> c.getHandle(bot.getLocale()) + " - " + c.getDescription(bot.getLocale()))
                            .sorted()
                            .collect(Collectors.toList())));
        }
        else{
            Optional<BotCommand> optional = CommandHandler.getAllRegisteredCommands().stream()
                    .filter(c -> c.getName(bot.getLocale()).equalsIgnoreCase(args[0]) || Arrays.asList(c.getAliases()).contains(args[0]))
                    .filter(c -> DiscordUtil.userHasPermission(message.getAuthor(), message.getGuild(), c.getRequiredPermissions()))
                    .findFirst();

            if(optional.isPresent()){
                BotCommand command = optional.get();
                bot.respond(command.getDetailedDescription());
            }
            else bot.info(builder.buildMessage(Message.CMD_INVALID_CMD, args[0]));
        }
    }
}
