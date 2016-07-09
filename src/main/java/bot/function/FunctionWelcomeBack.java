package bot.function;

import bot.settings.BooleanSetting;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.UserVoiceChannelMoveEvent;

public class FunctionWelcomeBack extends BotFunction{

    public static String[] welcomes = {"Welcome back, {USER}!", "Hello again, {USER}!", "Welcome back {USER}, we missed you!"};

    //Setting for this function
    private static final BooleanSetting SEE_WELCOME_NOTIFICATIONS = new BooleanSetting("notify_welcome", "Change whether the bot welcomes you back", true);

    @Override
    public void init(){
        this.bot.getUserSettingsHandler().registerNewSetting(SEE_WELCOME_NOTIFICATIONS);
    }

    @Override
    protected void onActivate(){

    }

    @Override
    protected void onDeactivate(){

    }

    @EventSubscriber
    public void onVoiceChannelLeave(UserVoiceChannelMoveEvent event){
        if(!checkSetting(event.getUser().getID(), SEE_WELCOME_NOTIFICATIONS)) return;

        if(event.getOldChannel().getID().equals(event.getOldChannel().getGuild().getAFKChannel().getID())){
            this.bot.say("Welcome back, " + event.getUser().mention() + "!" + (event.getUser().getID().equals("188803847458652162") ? " :heart:" : ""));
        }
    }
}
