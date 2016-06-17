package bot.function;

import bot.DiscordBot;

public abstract class BotFunction{

    public DiscordBot bot;

    public abstract void activate();

    public abstract void deactivate();
}
