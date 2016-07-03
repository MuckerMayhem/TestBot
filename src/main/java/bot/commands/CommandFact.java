package bot.commands;

import bot.DiscordBot;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.io.IOException;

/**
 * Created by Owner on 2016-07-02.
 */
public class CommandFact extends Command
{
    @Override
    void onExecute(DiscordBot bot, IMessage message, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException, IOException
    {
        double ID = Math.floor(Math.random() * 1000);



          Document  doc = Jsoup.connect("http://mentalfloss.com/api/1.0/views/amazing_facts.json?id=" + ID).ignoreContentType(true).get();


        bot.respond(doc.body().text());
    }
}
