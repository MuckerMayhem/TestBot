package logging;

import bot.DiscordBot;
import bot.event.BotEvent;
import sx.blah.discord.handle.obj.IUser;

public class UserLoggedEvent extends BotEvent{
    
    private final IUser user;
    
    public UserLoggedEvent(DiscordBot bot, IUser user){
        super(bot);
        this.user = user;
    }
    
    public IUser getUser(){
        return this.user;
    }
}
