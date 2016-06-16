package bot.commands;

import bot.DiscordBot;
import sx.blah.discord.handle.obj.IMessage;

import java.util.Arrays;

public class CommandTest extends Command{

    @Override
    public void onCommand(DiscordBot bot, IMessage message, String[] args){
        bot.respond("Hello, " + message.getAuthor().getName() + " (" + String.join(", ", Arrays.asList(args)) + ")");
    }
}
