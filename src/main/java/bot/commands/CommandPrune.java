package bot.commands;

import bot.DiscordBot;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageList;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.util.List;

/**
 * Created by Owner on 2016-06-26.
 */
public class CommandPrune extends Command
{
    @Override
    public void onExecute(DiscordBot bot, IMessage message, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException
    {

      MessageList msgs = message.getChannel().getMessages();
        List<IMessage> msgList = msgs.subList(msgs.size() - 1, msgs.size() - 11);
        System.out.println("Test");

        if(args.length == 0)
           bot.respond("No arguments entered!");
        else
        {
            System.out.println("test");
            for(IMessage m : msgList)
            {
              if(m.getContent().equals(args[0]))
              {
                  msgs.get(m.getID()).delete();
              }

            }
        }


    }
}
