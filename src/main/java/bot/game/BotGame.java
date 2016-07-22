package bot.game;

import bot.DiscordBot;

//TODO: Do something with this
//TODO: Make BotFeature
public abstract class BotGame{

    protected DiscordBot bot;

    public abstract boolean isMultiplayer();

    public abstract boolean isReplayable();

    public abstract void play();

    public abstract void quit();
}
