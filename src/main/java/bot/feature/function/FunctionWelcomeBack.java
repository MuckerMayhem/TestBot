package bot.feature.function;

import bot.DiscordBot;
import bot.locale.Message;
import bot.locale.MessageBuilder;
import bot.settings.BooleanSetting;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.UserVoiceChannelMoveEvent;

public class FunctionWelcomeBack extends BotFunction{
    
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

    public void onVoiceChannelMove(DiscordBot bot, UserVoiceChannelMoveEvent event){
        if(bot == null) return;

        if(!bot.checkSetting(event.getUser().getID(), SEE_WELCOME_NOTIFICATIONS)) return;

        if(event.getOldChannel().getID().equals(event.getOldChannel().getGuild().getAFKChannel().getID())){
            bot.say(new MessageBuilder(bot.getLocale()).buildMessage(Message.FUNC_WELCOME_MESSAGE, event.getUser().mention()));
        }
    }
}
