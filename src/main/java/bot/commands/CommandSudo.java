package bot.commands;

import bot.DiscordBot;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.util.Objects;
import java.util.Scanner;

public class CommandSudo extends Command
{
    Scanner r = new Scanner(System.in);

    @Override
    protected void onRegister(){

    }

    @Override
    protected void onExecute(DiscordBot bot, IMessage message, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException
    {
        System.out.println("test");
        IChannel channel = bot.getClient().getChannelByID("189563484839608320");
        String s = "";
        while (!Objects.equals(s, "exit"))
        {
            s = r.nextLine();

            bot.say(channel, s);
        }
        System.out.println("Sudo stopped.");
    }
}
