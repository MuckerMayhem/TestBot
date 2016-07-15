package bot.feature.command;

import bot.DiscordBot;
import sx.blah.discord.handle.obj.IMessage;

public class CommandShowHelp extends BotCommand{

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
        if(args.length == 0) return;

        BotCommand command = CommandHandler.getCommandByName(args[0]);
        if(command == null) return;

        bot.respond(command.getDetailedDescription());
    }
}
