package bot.feature.command;

import bot.DiscordBot;
import bot.locale.Message;
import sx.blah.discord.handle.obj.IMessage;
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
    protected void onExecute(DiscordBot bot, IMessage message, String[] args){
        if(args.length == 0){
            bot.respond("\n" + buildMessage(Message.CMD_HELP_DETAILS, getHandle(bot.getLocale())) + "\n" +
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
            else bot.info(buildMessage(Message.CMD_INVALID_CMD, args[0]));
        }
    }
}
