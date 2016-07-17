package bot.feature.function;

import bot.DiscordBot;
import bot.feature.BotFeature;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.UserVoiceChannelMoveEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class BotFunction extends BotFeature{

    private static ArrayList<BotFunction> global_functions = new ArrayList<>();

    private Class<? extends BotFunction> mainClass;

    public static List<BotFunction> getAllRegisteredFunctions(){
        return global_functions;
    }

    public static BotFunction registerFunction(String name, Class<? extends BotFunction> mainClass){
        BotFunction instance;
        try{
            instance = mainClass.newInstance();
            instance.name = name;
            instance.mainClass = mainClass;

            instance.onRegister();

            global_functions.add(instance);
        }
        catch(InstantiationException | IllegalAccessException e){
            System.err.print("Failed to register function '" + name + "': " + e.getClass().getSimpleName());
            return null;
        }

        return instance;
    }

    public abstract void onMessageReceived(DiscordBot bot, MessageReceivedEvent event) throws Exception;

    public abstract void onVoiceChannelMove(DiscordBot bot, UserVoiceChannelMoveEvent event) throws Exception;

    public String getName(){
        return this.name;
    }

    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event){
        DiscordBot bot = DiscordBot.getInstance(event.getMessage().getGuild());
        if(bot != null && bot.featureEnabled(this)){
            try{
                this.onMessageReceived(bot, event);
            }
            catch(Exception e){
                bot.log(e, "Exception occurred while handling function " + this.name);
            }
        }
    }

    @EventSubscriber
    public void onVoiceChannelMove(UserVoiceChannelMoveEvent event){
        DiscordBot bot = DiscordBot.getInstance(event.getNewChannel().getGuild());
        if(bot != null && bot.featureEnabled(this)){
            try{
                this.onVoiceChannelMove(bot, event);
            }
            catch(Exception e){
                bot.log(e, "Exception occurred while handling function " + this.name);
            }
        }
    }
}
