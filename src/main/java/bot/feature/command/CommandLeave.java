package bot.feature.command;

import bot.DiscordBot;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IVoiceChannel;
import util.DiscordUtil;

public class CommandLeave extends BotCommand{

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
        IVoiceChannel channel = DiscordUtil.getVoiceChannel(bot.getGuild(), DiscordBot.getClient().getOurUser());
        if(channel == null) return;
        
        channel.leave();
    }
}
