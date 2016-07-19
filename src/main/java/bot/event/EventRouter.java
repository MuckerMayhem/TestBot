package bot.event;

import bot.DiscordBot;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.UserVoiceChannelMoveEvent;

public final class EventRouter{
    
    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event){
        DiscordBot bot = DiscordBot.getInstance(event.getMessage().getGuild());
        if(bot == null) return;
        
        bot.getEventDispatcher().dispatchEvent(new BotMessageReceivedEvent(bot, event.getMessage()));
    }
    
    @EventSubscriber
    public void onUserVoiceChannelMove(UserVoiceChannelMoveEvent event){
        DiscordBot bot = DiscordBot.getInstance(event.getNewChannel().getGuild());
        if(bot == null) return;
        
        bot.getEventDispatcher().dispatchEvent(new BotVoiceChannelMoveEvent(bot, event.getUser(), event.getOldChannel(), event.getNewChannel()));
    }
}
