package bot.feature.command;

import bot.DiscordBot;
import bot.locale.Message;
import sx.blah.discord.handle.obj.IMessage;

public class CommandRestart extends BotCommand{

    @Override
    protected void onExecute(DiscordBot bot, IMessage message, String[] args){
        bot.respond(buildMessage(Message.CMD_RESTART_RESTARTING));
        bot.init();
        bot.respond(messageBuilder().withLocale(bot.getLocale()).buildMessage(Message.CMD_RESTART_FINISHED));
    }

    @Override
    public void onRegister(){

    }

    @Override
    public void onEnable(DiscordBot bot){

    }

    @Override
    public void onDisable(DiscordBot bot){

    }
}
