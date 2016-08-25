package bot.event;

import bot.DiscordBot;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IVoiceChannel;

public class BotVoiceChannelMoveEvent extends BotEvent{
    
    private final IUser user;
    private final IVoiceChannel from;
    private final IVoiceChannel to;
    
    public BotVoiceChannelMoveEvent(DiscordBot bot, IUser user, IVoiceChannel from, IVoiceChannel to){
        super(bot);
        this.user = user;
        this.from = from;
        this.to = to;
    }
    
    public IUser getUser(){
        return this.user;
    }
    
    public IVoiceChannel getOldChannel(){
        return this.from;
    }
    
    public IVoiceChannel getNewChannel(){
        return this.to;
    }
}
