package bot.function;

import bot.DiscordBot;
import bot.settings.BooleanSetting;
import bot.settings.SingleSettingsHandler;
import bot.settings.UserSettingsHandler;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;

public abstract class BotFunction{

    public DiscordBot bot;
    private boolean active;

    public static SingleSettingsHandler getGlobalSettingsHandler(){
        return DiscordBot.getGlobalSettingsHandler();
    }

    public abstract void init();

    protected abstract void onActivate();

    protected abstract void onDeactivate();

    public UserSettingsHandler getUserSettingsHandler(){
        return this.bot.getUserSettingsHandler();
    }

    public void activate(){
        this.active = true;
        this.bot.getClient().getDispatcher().registerListener(this);
        this.onActivate();
    }

    public void deactivate(){
        this.active = false;
        this.bot.getClient().getDispatcher().unregisterListener(this);
        this.onDeactivate();
    }

    public boolean isActive(){
        return this.active;
    }

    /**
     * Check the value of a {@link bot.settings.BooleanSetting}'s value
     * @param userId User to get value of setting for
     * @param setting Setting to check
     * @return
     */
    public boolean checkSetting(String userId, BooleanSetting setting){
        return (Boolean) this.bot.getUserSettingsHandler().getUserSetting(userId, setting);
    }

    public boolean checkSetting(MessageReceivedEvent event, BooleanSetting setting){
        return checkSetting(event.getMessage().getAuthor().getID(), setting);
    }

    public boolean checkSetting(IMessage message, BooleanSetting setting){
        return checkSetting(message.getAuthor().getID(), setting);
    }
}
