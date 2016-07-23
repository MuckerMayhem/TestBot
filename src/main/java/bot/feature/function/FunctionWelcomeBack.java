package bot.feature.function;

import bot.DiscordBot;
import bot.event.BotEventSubscriber;
import bot.event.BotVoiceChannelMoveEvent;
import bot.feature.ToggleableBotFeature;
import bot.locale.Message;
import bot.locale.MessageBuilder;
import bot.settings.BooleanSetting;

public class FunctionWelcomeBack extends BotFunction implements ToggleableBotFeature{
    
    //Setting for this function
    private static final BooleanSetting SEE_WELCOME_NOTIFICATIONS = new BooleanSetting("notify_welcome", true);

    public FunctionWelcomeBack(){
        super("welcome");
    }

    @Override
    public void onRegister(){
        DiscordBot.getUserSettingsHandler().addSetting(SEE_WELCOME_NOTIFICATIONS);
    }

    @Override
    public void onEnable(DiscordBot bot){
        bot.getEventDispatcher().registerListener(this);
    }

    @Override
    public void onDisable(DiscordBot bot){
        bot.getEventDispatcher().unregisterListener(this);
    }
    
    @BotEventSubscriber
    public void onVoiceChannelMove(BotVoiceChannelMoveEvent event){
        DiscordBot bot = event.getBot();

        if(!bot.checkSetting(event.getUser().getID(), SEE_WELCOME_NOTIFICATIONS)) return;

        if(event.getOldChannel().getID().equals(event.getOldChannel().getGuild().getAFKChannel().getID())){
            bot.say(new MessageBuilder(bot.getLocale()).buildMessage(Message.FUNC_WELCOME_MESSAGE, event.getUser().mention()));
        }
    }
}
