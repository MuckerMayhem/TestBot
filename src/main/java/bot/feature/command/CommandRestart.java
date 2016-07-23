package bot.feature.command;

import bot.DiscordBot;
import bot.locale.Message;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.Permissions;

public class CommandRestart extends BotCommand{

    public CommandRestart(){
        super("restart", Permissions.MANAGE_SERVER);
    }

    @Override
    public void onRegister() {}

    @Override
    public void onEnable(DiscordBot bot) {}

    @Override
    public void onDisable(DiscordBot bot) {}
    
    @Override
    protected void onExecute(DiscordBot bot, IMessage message, String[] args){
        bot.respond(buildMessage(Message.CMD_RESTART_RESTARTING));
        bot.init();
        bot.respond(messageBuilder().withLocale(bot.getLocale()).buildMessage(Message.CMD_RESTART_FINISHED));
    }
}
