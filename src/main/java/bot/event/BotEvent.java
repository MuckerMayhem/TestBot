package bot.event;

import bot.DiscordBot;

public abstract class BotEvent{
    
    private DiscordBot bot;
    private boolean cancelled;
    
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
