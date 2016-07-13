package bot;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;

public class InputBot extends DiscordBot{

    public InputBot(IGuild guild){
        super(guild);
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

    /**
     * Prompts the bot to wait for user input in its home channel.<br><
     * Any input sent by the specified user is counted.
     * @return The next input sent by the specified user
     */
    public String nextLine(String userId){
        return nextLine(getHome(), userId);
    }

    /**
     * Prompts the bot to wait for user input in its home channel.<br><
     * Any sent by a real user (rather than a bot) will be counted as<br>
     * the next input.
     * @return The next input sent by a non-bot user
     */
    public String nextLine(){
        return nextLine(getHome());
    }
}
