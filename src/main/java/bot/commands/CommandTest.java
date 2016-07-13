package bot.commands;

import bot.DiscordBot;
import sx.blah.discord.handle.obj.IMessage;

import java.util.Arrays;

public class CommandTest extends Command{

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
        bot.respond("Hello, " + message.getAuthor().getName() + " (" + String.join(", ", Arrays.asList(args)) + ")");
    }
}
