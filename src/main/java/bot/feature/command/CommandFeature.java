package bot.feature.command;

import bot.DiscordBot;
import bot.feature.BotFeature;
import bot.feature.ToggleableBotFeature;
import bot.locale.Message;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.Permissions;

public class CommandFeature extends BotCommand{
    
    public CommandFeature(){
        super("feature", Permissions.MANAGE_SERVER);
    }

    @Override
    protected void onExecute(DiscordBot bot, IMessage message, String[] args) throws Exception{
        if(args.length == 0) return;
        
        StringBuilder builder = new StringBuilder(buildMessage(Message.CMD_FEATURE_LIST)).append("\n");
        BotFeature.getAllRegisteredFeatures().stream()
                .filter(f -> f instanceof ToggleableBotFeature)
                .forEach(f -> builder
                        .append(f instanceof BotCommand ? ((BotCommand) f).getPrettyName(getLocale()) : f.getName(getLocale()))
                        .append(" - ")
                        .append(f.getDescription(getLocale()))
                        .append("\n"));
        
        bot.respond(builder.toString(), 8000L);
    }

    @Override
    public void onRegister(){

    }

    @Override
    public void onEnable(DiscordBot bot){

    }

    @Override
    public void onDisable(DiscordBot bot){

    }
}
