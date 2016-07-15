package bot.feature.command;

import bot.DiscordBot;
import bot.locale.Message;
import bot.locale.MessageBuilder;
import bot.settings.ArraySetting;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import util.DiscordUtil;

import java.util.Arrays;

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
                bot.respond(builder.buildMessage(Message.CMD_CLEAR_DELETED, DiscordUtil.deleteAllMessages(message.getChannel())), 3500L);
                return;
            }
        }

        bot.say(message.getChannel(), builder.buildMessage(Message.CMD_CLEAR_CANCELLED), 3500L);
    }

    /*
    public void deleteMessages(DiscordBot bot, IChannel channel, int size, int count){
        MessageBuilder builder = new MessageBuilder(bot.getLocale());

        final int deleted = size < 50 ? size : 50;
        final int newCount = count + deleted;
        TimerTask task = new TimerTask(){
            @Override
            public void run(){
                try{
                    channel.getMessages().load(deleted);
                    channel.getMessages().deleteFromRange(0, deleted);
                }
                catch(RateLimitException | DiscordException | MissingPermissionsException e){
                    e.printStackTrace();
                }

                if(size - deleted > 50)
                    deleteMessages(bot, channel, size - deleted, newCount);
                else
                    bot.say(channel, builder.buildMessage(Message.CMD_CLEAR_DELETED, newCount), 3500L);
            }
        };
        if(size > 0)
            new Timer().schedule(task, 500L);
    }
    */
}
