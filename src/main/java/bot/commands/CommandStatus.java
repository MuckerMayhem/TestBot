package bot.commands;

import bot.DiscordBot;
import sx.blah.discord.handle.obj.IMessage;

public class CommandStatus extends Command
{

    @Override
    public void onExecute(DiscordBot bot, IMessage message, String[] args)
    {
        bot.respond("Connection status for " + message.getAuthor());
        bot.respond("Connected Voice Channels: " + message.getAuthor().getConnectedVoiceChannels().toString());
        bot.respond("isConnected() status: " + message.getAuthor().getConnectedVoiceChannels().get(0).isConnected());
    }
}
