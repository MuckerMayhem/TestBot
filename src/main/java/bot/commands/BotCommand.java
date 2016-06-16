package bot.commands;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.Arrays;

public abstract class BotCommand{

    public static final String COMMAND_PREFIX = "!";

    abstract String getName();

    abstract String getDescription();

    abstract void onCommand(IDiscordClient client, IMessage message, String[] args) throws IOException, UnsupportedAudioFileException, RateLimitException, DiscordException, MissingPermissionsException;

    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event) throws IOException, UnsupportedAudioFileException, RateLimitException, DiscordException, MissingPermissionsException
    {
        String message = event.getMessage().getContent();
        if(message.startsWith(COMMAND_PREFIX + getName())){
            String[] split = message.split(" ");
            String[] args = Arrays.copyOfRange(split, 1, split.length);
//            event.getMessage().delete();
            this.onCommand(event.getClient(), event.getMessage(), args);
            System.out.println("Command '" + getName() + "' run with arguments " + String.join(", ", Arrays.asList(args)));//TODO: This is for debugging
        }
    }
}
