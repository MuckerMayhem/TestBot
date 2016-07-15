package bot.feature.command;

import bot.DiscordBot;
import bot.locale.Message;
import bot.locale.MessageBuilder;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.io.IOException;

public class CommandRestart extends BotCommand{

    @Override
    protected void onExecute(DiscordBot bot, IMessage message, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException, IOException{
        MessageBuilder builder = new MessageBuilder(bot.getLocale());

        bot.respond(builder.buildMessage(Message.CMD_RESTART_RESTARTING));
        bot.init();
        bot.respond(builder.withLocale(bot.getLocale()).buildMessage(Message.CMD_RESTART_FINISHED));
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
