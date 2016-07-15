package bot.feature.command;

import bot.DiscordBot;
import bot.locale.Message;
import sx.blah.discord.handle.obj.IMessage;

import java.util.Arrays;

public class CommandTest extends BotCommand{

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
        bot.respond(buildMessage(Message.CMD_TEST_MESSAGE, message.getAuthor().getName(), String.join(", ", Arrays.asList(args))));
    }
}
