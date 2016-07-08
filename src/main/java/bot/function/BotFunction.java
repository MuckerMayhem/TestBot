package bot.function;

import bot.DiscordBot;
import bot.settings.SettingsHandler.Setting;

public abstract class BotFunction{

    public DiscordBot bot;
    private boolean active;

    public void activate(){
        this.active = true;
        this.bot.getClient().getDispatcher().registerListener(this);
        this.onActivate();
    }

    public abstract void onActivate();

    public void deactivate(){
        this.active = false;
        this.bot.getClient().getDispatcher().unregisterListener(this);
        this.onDeactivate();
    }

    public abstract void onDeactivate();

    public boolean isActive(){
        return this.active;
    }

    public boolean checkSetting(String userId, Setting setting){
        return this.bot.getSettingsHandler().getUserSetting(userId, setting);
    }
}
