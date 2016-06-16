package bot.commands;

import bot.DiscordBot;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.util.Arrays;

public class CommandBooty extends Command{

    private static String[] theBooty = {"͡°", "͜ʖ", "͡°)"};

    @Override
    void onCommand(DiscordBot bot, IMessage message, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException{
        if(args.length < 3) return;

        if(Arrays.equals(theBooty, args)) new CommandSound().onCommand(bot, message, new String[] {"booty"});
    }
}