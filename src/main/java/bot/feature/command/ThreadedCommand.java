package bot.feature.command;

import bot.DiscordBot;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.io.IOException;

public abstract class ThreadedCommand extends BotCommand{

    @Override
    public void execute(DiscordBot bot, IMessage message, String[] args){
        new Thread(() -> {
            try{
                this.onExecute(bot, message, args);
            }
            catch(RateLimitException | DiscordException | MissingPermissionsException | IOException e){
                bot.reportException(e, "Exception in ThreadedCommand '" + this.name + "'");
            }
        }).run();
    }

    /**
     * Prompts the bot to wait for user input in the specified channel.<br><
     * Any input sent by the specified user is counted.
     * @return The next input sent by the specified user
     */
    public String nextLine(IChannel channel, String userId){
        String last = channel.getMessages().get(0).getContent();
        String next = last;
        while(next.equals(last)){
            IMessage first = channel.getMessages().get(0);
            if(first.getAuthor().getID().equals(userId)){
                next = channel.getMessages().get(0).getContent();
            }
        }
        return next;
    }

    /**
     * Prompts the bot to wait for user input the specified channel.<br><
     * Any sent by a real user (rather than a bot) will be counted as<br>
     * the next input.
     * @return The next input sent by a non-bot user
     */
    public String nextLine(IChannel channel){
        String last = channel.getMessages().get(0).getContent();
        String next = last;
        while(next.equals(last)){
            if(!channel.getMessages().get(0).getAuthor().isBot()){
                next = channel.getMessages().get(0).getContent();
            }
        }
        return next;
    }
}
