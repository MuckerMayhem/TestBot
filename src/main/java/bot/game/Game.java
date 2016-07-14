package bot.game;

import bot.DiscordBot;

public abstract class Game{

    protected DiscordBot bot;

    public abstract boolean isMultiplayer();

    public abstract boolean isReplayable();

    public abstract void play();

    public abstract void quit();
}
