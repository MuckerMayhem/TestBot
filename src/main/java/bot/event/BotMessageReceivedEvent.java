package bot.event;

import bot.DiscordBot;
import sx.blah.discord.handle.obj.IMessage;

public class BotMessageReceivedEvent extends BotEvent{

    private final IMessage message;

    public BotMessageReceivedEvent(DiscordBot bot, IMessage message){
        super(bot);
        this.message = message;
    }

    public IMessage getMessage(){
        return this.message;
    }
}
