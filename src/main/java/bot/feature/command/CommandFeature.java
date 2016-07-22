package bot.feature.command;

import bot.DiscordBot;
import bot.feature.BotFeature;
import bot.feature.ToggleableBotFeature;
import bot.locale.Message;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.Permissions;

import java.util.List;
import java.util.stream.Collectors;

public class CommandFeature extends BotCommand{
    
    public CommandFeature(){
        super("feature", Permissions.MANAGE_SERVER);
    }
    
    @Override
    protected void onExecute(DiscordBot bot, IMessage message, String[] args) throws Exception{
        if(args.length == 0) return;

        List<BotFeature> features = BotFeature.getAllRegisteredFeatures().stream().sorted().collect(Collectors.toList());
        
        if(args[0].equalsIgnoreCase(getLocalArgs()[0])){
            StringBuilder builder = new StringBuilder(buildMessage(Message.CMD_FEATURE_LIST)).append("\n");
            int index = 1;
            for(BotFeature f : features){
                builder.append(f instanceof ToggleableBotFeature ? index : "X")
                        .append(". ")
                        .append(f instanceof BotCommand ? ((BotCommand) f).getPrettyName(getLocale()) : f.getName(getLocale()))
                        .append(" - ")
                        .append(f.getDescription(getLocale()))
                        .append(" (")
                        .append(bot.featureEnabled(f) ? 'O' : 'X')
                        .append(")")
                        .append("\n");
                index++;
            }

            bot.respond(builder.toString(), features.size() * 2000L);
            return;
        }
        
        if(args.length < 2){
            bot.respond(getDetailedDescription());
            return;
        }
        
        if(args[0].equalsIgnoreCase(getLocalArgs()[1])){
            BotFeature feature;
            try{
                feature = features.get(Integer.parseInt(args[1]) - 1);
            }
            catch(ArrayIndexOutOfBoundsException | NumberFormatException e){
                bot.respond(buildMessage(Message.CMD_SETTING_INVALID_NUMBER, args[1]));
                return;
            }

            if(!(feature instanceof ToggleableBotFeature)){
                bot.respond(buildMessage(Message.CMD_FEATURE_NO_DISABLE, feature.getName(getLocale())));
                return;
            }
            
            boolean toggledOn = bot.toggleFeature(feature, true, true);
            
            if(toggledOn){
                bot.respond(buildMessage(Message.CMD_FEATURE_ENABLED, feature.getName(getLocale())));
            }
            else{
                bot.respond(buildMessage(Message.CMD_FEATURE_DISABLED, feature.getName(getLocale())));
            }
        }
        else bot.respond(getDetailedDescription());
    }

    @Override
    public void onRegister() {}

    @Override
    public void onEnable(DiscordBot bot) {}

    @Override
    public void onDisable(DiscordBot bot) {}
}
