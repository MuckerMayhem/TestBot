package bot.feature.commands;

import bot.DiscordBot;
import bot.locale.Message;
import bot.locale.MessageBuilder;
import bot.settings.ArraySetting;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class ThreadedCommandClear extends ThreadedCommand{

    private static final ArraySetting WHITELIST_CLEAR = new ArraySetting("clear_whitelist", new String[] {});

    @Override
    public void onRegister(){

    }

    @Override
    public void onEnable(DiscordBot bot){
        bot.getServerSettingsHandler().registerNewSetting(WHITELIST_CLEAR);
    }

    @Override
    public void onDisable(DiscordBot bot){

    }

    @Override
    protected void onExecute(DiscordBot bot, IMessage message, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException{
        MessageBuilder builder = new MessageBuilder(bot.getLocale());

        String[] whitelist = (String[]) bot.getServerSettingsHandler().getSetting(WHITELIST_CLEAR);

        if(!Arrays.asList(whitelist).contains(message.getChannel().getName())){
            bot.say(message.getChannel(), builder.buildMessage(Message.CMD_CLEAR_NOT_HERE), 3000L);
            return;
        }

        bot.say(message.getChannel(), builder.buildMessage(Message.CMD_CLEAR_PROMPT_1));
        bot.say(message.getChannel(), builder.buildMessage(Message.CMD_CLEAR_CONF_1));
        if(nextLine(message.getChannel()).equalsIgnoreCase(builder.buildMessage(Message.CMD_CLEAR_RESPONSE_1))){
            bot.say(message.getChannel(), builder.buildMessage(Message.CMD_CLEAR_PROMPT_2));
            bot.say(message.getChannel(), builder.buildMessage(Message.CMD_CLEAR_CONF_2));
            if(nextLine(message.getChannel()).equalsIgnoreCase(builder.buildMessage(Message.CMD_CLEAR_RESPONSE_2))){
                IMessage botMessage = bot.say(message.getChannel(), builder.buildMessage(Message.CMD_CLEAR_DELETING, message.getChannel().getName()));
                deleteMessages(bot, message.getChannel(), 0);
                return;
            }
        }

        bot.say(message.getChannel(), builder.buildMessage(Message.CMD_CLEAR_CANCELLED), 3500L);
    }

    public void deleteMessages(DiscordBot bot, IChannel channel, int count){
        MessageBuilder builder = new MessageBuilder(bot.getLocale());

        int size = channel.getMessages().size();
        final int newCount = count + (size < 50 ? size : 50);
        TimerTask task = new TimerTask(){
            @Override
            public void run(){
                try{
                    channel.getMessages().deleteFromRange(0, size < 50 ? size - 1 : 49);
                }
                catch(RateLimitException | DiscordException | MissingPermissionsException e){
                    e.printStackTrace();
                }
                if(size > 50)
                    deleteMessages(bot, channel, newCount);
                else
                    bot.say(channel, builder.buildMessage(Message.CMD_CLEAR_DELETED, newCount), 3500L);
            }
        };
        if(size > 0)
            new Timer().schedule(task, 1000L);
    }
}
