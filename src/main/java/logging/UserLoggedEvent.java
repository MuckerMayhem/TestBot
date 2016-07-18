package logging;

import bot.event.BotEvent;
import sx.blah.discord.handle.obj.IUser;

public class UserLoggedEvent extends BotEvent{
    
    private final IUser user;
    
    public UserLoggedEvent(IUser user){
        this.user = user;
    }
    
    public IUser getUser(){
        return this.user;
    }
}
