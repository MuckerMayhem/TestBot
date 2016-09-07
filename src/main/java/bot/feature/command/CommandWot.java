package bot.feature.command;

import bot.DiscordBot;
import bot.feature.command.sound.Sound;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IVoiceChannel;
import util.DiscordUtil;

import java.util.Timer;
import java.util.TimerTask;

public class CommandWot extends BotCommand{

    public CommandWot(){
        super("wot");
    }

    @Override
    protected void onExecute(DiscordBot bot, IMessage message, String[] args) throws Exception{
        IVoiceChannel channel = DiscordUtil.getVoiceChannel(message.getGuild(), message.getAuthor());
        if(channel == null) return;
        
        CommandSound.playSound(bot, channel, Sound.WOT, 0.2F);
        
        bot.respond("http://i.imgur.com/DlxKQ3C.jpg", 7500L);

        TimerTask task = new TimerTask(){
            @Override
            public void run(){
                CommandSound.playSound(bot, channel, Sound.QUACK);
                
            }
        };
        new Timer().schedule(task, 7500L);
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
