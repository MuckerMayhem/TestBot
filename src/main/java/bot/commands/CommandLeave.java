package bot.commands;

import bot.DiscordBot;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IVoiceChannel;

public class CommandLeave extends Command
{
    @Override
    public void onCommand(DiscordBot bot, IMessage message, String[] args){
        IVoiceChannel channel = message.getAuthor().getVoiceChannel().get();
        channel.leave();
    }
}
