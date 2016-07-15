package bot.feature.function;

import bot.DiscordBot;
import bot.settings.BooleanSetting;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.UserVoiceChannelMoveEvent;

public class FunctionWelcomeBack extends BotFunction{

    public static String[] welcomes = {"Welcome back, {USER}!", "Hello again, {USER}!", "Welcome back {USER}, we missed you!"};

    //Setting for this function
    private static final BooleanSetting SEE_WELCOME_NOTIFICATIONS = new BooleanSetting("notify_welcome", true);

    @Override
    public void onRegister(){
        DiscordBot.getUserSettingsHandler().registerNewSetting(SEE_WELCOME_NOTIFICATIONS);
    }

    @Override
    public void onEnable(DiscordBot bot) {}

    @Override
    public void onDisable(DiscordBot bot) {}


    @Override
    public void onMessageReceived(DiscordBot bot, MessageReceivedEvent event) throws Exception {}

    @EventSubscriber
    public void onVoiceChannelMove(DiscordBot bot, UserVoiceChannelMoveEvent event){
        if(bot == null) return;

        if(!bot.checkSetting(event.getUser().getID(), SEE_WELCOME_NOTIFICATIONS)) return;

        if(event.getOldChannel().getID().equals(event.getOldChannel().getGuild().getAFKChannel().getID())){
            bot.say("Welcome back, " + event.getUser().mention() + "!" + (event.getUser().getID().equals("188803847458652162") ? " :heart:" : ""));
        }
    }
}
