package bot.feature.command;

import bot.DiscordBot;
import sx.blah.discord.handle.obj.IMessage;
import util.DiscordUtil;

public class CommandType extends BotCommand{

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

        DiscordUtil.deleteMessage(message);
        bot.type(message.getChannel(), String.join(" ", args));
    }
}
