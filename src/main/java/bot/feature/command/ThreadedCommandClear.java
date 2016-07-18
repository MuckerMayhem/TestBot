package bot.feature.command;

import bot.DiscordBot;
import bot.locale.Message;
import bot.settings.ArraySetting;
import sx.blah.discord.handle.obj.IMessage;
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
    protected void onExecute(DiscordBot bot, IMessage message, String[] args){
        String[] whitelist = (String[]) bot.getServerSettingsHandler().getSetting(WHITELIST_CLEAR);

        if(!Arrays.asList(whitelist).contains(message.getChannel().getName())){
            bot.say(message.getChannel(), buildMessage(Message.CMD_CLEAR_NOT_HERE), 3000L);
            return;
        }

        bot.say(message.getChannel(), buildMessage(Message.CMD_CLEAR_PROMPT_1));
        bot.say(message.getChannel(), buildMessage(Message.CMD_CLEAR_CONF_1));
        if(nextLine(message.getChannel()).equalsIgnoreCase(buildMessage(Message.CMD_CLEAR_RESPONSE_1))){
            bot.say(message.getChannel(), buildMessage(Message.CMD_CLEAR_PROMPT_2));
            bot.say(message.getChannel(), buildMessage(Message.CMD_CLEAR_CONF_2));
            if(nextLine(message.getChannel()).equalsIgnoreCase(buildMessage(Message.CMD_CLEAR_RESPONSE_2))){
                bot.say(message.getChannel(), buildMessage(Message.CMD_CLEAR_DELETING, message.getChannel().getName()));
                bot.respond(buildMessage(Message.CMD_CLEAR_DELETED, DiscordUtil.deleteAllMessages(message.getChannel())), 3500L);
                return;
            }
        }

        bot.say(message.getChannel(), buildMessage(Message.CMD_CLEAR_CANCELLED), 3500L);
    }
}
