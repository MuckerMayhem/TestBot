package bot.commands;

import bot.DiscordBot;
import com.github.axet.vget.VGet;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.io.File;
import java.net.URL;

/**
 * Created by Owner on 2016-06-22.
 */
public class CommandPlayVideo extends Command
{

    @Override
    void onExecute(DiscordBot bot, IMessage message, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException
    {
        try
        {
            // ex: http://www.youtube.com/watch?v=Nj6PFaDmp6c
            String url = args[0];
            // ex: "/Users/axet/Downloads"
            String path = args[1];
            VGet v = new VGet(new URL(url), new File(path));
            v.download();
        } catch (
                Exception e
                )
        {
            throw new RuntimeException(e);
        }
    }
}
