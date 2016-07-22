package bot.feature;

import bot.DiscordBot;

public interface ToggleableBotFeature{

    public void onDisable(DiscordBot bot);
}
