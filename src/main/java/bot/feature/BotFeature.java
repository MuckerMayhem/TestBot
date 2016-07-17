package bot.feature;

import bot.DiscordBot;

public abstract class BotFeature{

    public String name;
    
    public abstract void onRegister();

    public abstract void onEnable(DiscordBot bot);

    public abstract void onDisable(DiscordBot bot);

    /**
     * Gets the name this feature was registered under
     * @return The name this feature was registered under
     */
    public String getRegisteredName(){
        return this.name;
    }
    
    public static void registerFeature(){

    }
}
