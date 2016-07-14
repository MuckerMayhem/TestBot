package bot.feature;

import bot.DiscordBot;

public abstract class BotFeature{

    public abstract void onRegister();

    public abstract void onEnable(DiscordBot bot);

    public abstract void onDisable(DiscordBot bot);

    public static void registerFeature(){

    }
}
