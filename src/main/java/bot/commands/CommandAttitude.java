package bot.commands;

import bot.behavior.BotAttitude;
import bot.behavior.Mood;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.HTTP429Exception;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;

public class CommandAttitude extends BotCommand{

    @Override
    String getName(){
        return "attitude";
    }

    @Override
    String getDescription(){
        return "Bot attitude values";
    }

    @Override
    void onCommand(IDiscordClient client, IMessage message, String[] args){
        BotAttitude attitude = BotAttitude.getBotFor(message.getAuthor().getID());
        try{
            MessageBuilder builder = new MessageBuilder(client);
            if(attitude == null){
                builder.withChannel(message.getChannel()).withContent("I don't know you yet!").build();
            }
            else{
                builder.withChannel(message.getChannel()).withContent("Attitude towards " + message.getAuthor().getName() + ": \n" +
                        "Happiness: " + attitude.getHappiness() + "\n" +
                        "Annoyance: " + attitude.getAnnoyance() + "\n" +
                        "Boredom: " + attitude.getBoredom() + "\n" +
                        "Mood: " + Mood.findClosestMood(attitude).name() + "\n" +
                        "Default mood: " + attitude.getDefaultMood().name()).build();
            }
        }
        catch(DiscordException | HTTP429Exception | MissingPermissionsException e){
            e.printStackTrace();
        }
    }
}
