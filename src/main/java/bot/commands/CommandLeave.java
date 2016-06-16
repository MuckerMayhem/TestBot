package bot.commands;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IVoiceChannel;

public class CommandLeave extends BotCommand
{

    @Override
    public String getName()
    {
        return "leave";
    }

    @Override
    public String getDescription()
    {
        return "Leave command";
    }

    @Override
    public void onCommand(IDiscordClient client, IMessage message, String[] args)
    {

        IVoiceChannel channel = message.getAuthor().getVoiceChannel().get();
        channel.leave();

    }
}
