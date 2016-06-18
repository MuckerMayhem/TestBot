package bot.commands;

import bot.DiscordBot;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class CommandHelp extends Command{

    @Override
    void onExecute(DiscordBot bot, IMessage message, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException{
//        StringBuilder builder = new StringBuilder("\n");

        if(args.length == 0){
            bot.respond("\n" + "Enter " + this.commandHandler.getCommandPrefix() + this.name + " <command> for details on a specific command\n" +
                    String.join("\n", CommandHandler.getAllRegisteredCommands()
                            .stream()
                            .map(c -> c.commandHandler.getCommandPrefix() + c.name + " - " + c.getDescription())
                            .collect(Collectors.toList())));
        }
        else{
            Optional<Command> optional = CommandHandler.getAllRegisteredCommands().stream()
                    .filter(c -> c.getName().equalsIgnoreCase(args[0]) || Arrays.asList(c.getAliases()).contains(args[0]))
                    .findFirst();

            if(optional.isPresent()){
                Command command = optional.get();
                bot.respond(command.getDetailedDescription());
            }
            else bot.respond("Invalid command '" + args[0] + "'");
        }
    }
}
