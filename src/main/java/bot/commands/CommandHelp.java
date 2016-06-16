package bot.commands;

import bot.DiscordBot;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

public class CommandHelp extends Command{

    @Override
    void onCommand(DiscordBot bot, IMessage message, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException{
        StringBuilder builder = new StringBuilder("\n");

        for(Command c : CommandHandler.getAllRegisteredCommands()){
            builder.append(c.commandHandler.getCommandPrefix() + c.name + " - " + c.getDescription() + "\n");
        }
        bot.respond(builder.toString());
    }
}
