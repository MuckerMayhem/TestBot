package bot.commands;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.HTTP429Exception;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;

import java.util.Arrays;

public class CommandTest extends BotCommand{

    @Override
    public String getName(){
        return "test";
    }

    @Override
    public String getDescription(){
        return "Test command";
    }

    @Override
    public void onCommand(IDiscordClient client, IMessage message, String[] args){
        MessageBuilder builder = new MessageBuilder(client).withChannel(message.getChannel());
        try{
            builder.appendContent("Hello, " + message.getAuthor().getName() + " (" + String.join(", ", Arrays.asList(args)) + ")").build();
        }
        catch(DiscordException | HTTP429Exception | MissingPermissionsException e){
            e.printStackTrace();
        }
    }
}
