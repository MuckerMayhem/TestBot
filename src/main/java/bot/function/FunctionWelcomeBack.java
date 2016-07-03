package bot.function;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.UserVoiceChannelMoveEvent;

public class FunctionWelcomeBack extends BotFunction{

    public static String[] welcomes = {"Welcome back, {USER}!", "Hello again, {USER}!", "Welcome back {USER}, we missed you!"};

    @Override
    public void onActivate(){

    }

    @Override
    public void onDeactivate(){

    }

    @EventSubscriber
    public void onVoiceChannelLeave(UserVoiceChannelMoveEvent event){
        if(event.getOldChannel().getID().equals(event.getOldChannel().getGuild().getAFKChannel().getID())){
            this.bot.say("Welcome back, " + event.getUser().mention() + "!" + (event.getUser().getID().equals("188803847458652162") ? " :heart:" : ""));
        }
    }
}
