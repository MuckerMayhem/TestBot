package bot.feature.command;

import bot.DiscordBot;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IVoiceChannel;

import java.util.Optional;

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
        IUser user = message.getAuthor();

        Optional<IVoiceChannel> channel = DiscordBot.getClient().getOurUser().getVoiceChannel();
        if(channel.isPresent() && channel.get().getGuild().getID().equals(message.getGuild().getID()))
            channel.get().leave();
    }
}
