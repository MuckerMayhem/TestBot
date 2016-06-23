package bot.listeners;

import bot.DiscordBot;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;

import java.util.HashMap;

public class MessageEventListener{

    public static final String BREAKUP_MESSAGE = "No walls! :yum:";
    public static final int MAX_MESSAGES = 8;

    private static MessageReceivedEvent lastEvent = null;
    private static HashMap<IChannel, String> messages = new HashMap<>();
    private static HashMap<IChannel, Integer> counts = new HashMap<>();

    @EventSubscriber
    public void onMessageEvent(MessageReceivedEvent event){
        IChannel channel = event.getMessage().getChannel();

        if(messages.containsKey(channel)){
            if(messages.get(channel).equals(event.getMessage().getAuthor().getID())){
                counts.put(channel, counts.get(channel) + 1);
            }
            else counts.put(channel, 0);

            if(counts.get(channel) >= MAX_MESSAGES){
                DiscordBot.instance.say(event.getMessage().getChannel(), BREAKUP_MESSAGE);
                counts.put(channel, 0);
            }
        }
        else{
            messages.put(channel, event.getMessage().getAuthor().getID());
            counts.put(channel, 1);
        }

        messages.put(channel, event.getMessage().getAuthor().getID());
    }
}
