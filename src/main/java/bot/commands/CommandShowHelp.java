package bot.commands;

import bot.DiscordBot;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.io.IOException;

public class CommandShowHelp extends Command{

    @Override
    protected void onRegister(){

    }

    @Override
    protected void onExecute(DiscordBot bot, IMessage message, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException, IOException{
        if(args.length == 0) return;

        Command command = CommandHandler.getCommandByName(args[0]);
        if(command == null) return;

        bot.respond(command.getDetailedDescription());
    }
}
