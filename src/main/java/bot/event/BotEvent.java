package bot.event;

import bot.DiscordBot;

public abstract class BotEvent{
    
    private final DiscordBot bot;
    
    private boolean cancelled;
    
    public BotEvent(DiscordBot bot){
        this.bot = bot;
    }
    
    public boolean isCancelled(){
        return this.cancelled;
    }
    
    public DiscordBot getBot(){
        return this.bot;
    }
    
    public void setCancelled(boolean cancelled){
        this.cancelled = cancelled;
    }
}
