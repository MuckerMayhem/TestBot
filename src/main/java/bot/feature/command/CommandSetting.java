package bot.feature.command;

import bot.DiscordBot;
import bot.locale.Message;
import bot.locale.MessageBuilder;
import bot.settings.Setting;
import bot.settings.SettingsHandler;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import util.DiscordUtil;

import java.io.IOException;

public class CommandSetting extends BotCommand{

    @Override
    public void onRegister(){

    }

    @Override
    public void onEnable(DiscordBot bot){

    }

    @Override
    public void onDisable(DiscordBot bot){

    }

    @Override
    protected void onExecute(DiscordBot bot, IMessage message, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException, IOException{
        String userId = message.getAuthor().getID();

        if(args.length == 0){
            bot.info(getDetailedDescription(), true);
            return;
        }

        MessageBuilder msgBuilder = new MessageBuilder(bot.getLocale());

        boolean serverAccess = DiscordUtil.userHasPermission(message.getAuthor(), message.getGuild(), Permissions.MANAGE_SERVER);
        if(args[0].equalsIgnoreCase("list")){
            StringBuilder builder = new StringBuilder(msgBuilder.buildMessage(Message.CMD_SETTING_LIST) + "\n" +
                    DiscordBot.getUserSettingsHandler().getSettings(userId).toString(bot.getLocale()));

            if(serverAccess)
                builder.append(msgBuilder.buildMessage(Message.CMD_SETTING_MORE))
                        .append("\n")
                        .append(bot.getServerSettingsHandler().getSettings().toString(bot.getLocale()));

            builder.append("\n").append(msgBuilder.buildMessage(Message.CMD_SETTING_ENTER_NUMBER));
            if(serverAccess) builder.append("\n").append(msgBuilder.buildMessage(Message.CMD_SETTING_ENTER_NUMBER_SERVER));

            bot.respond(builder.toString(), 5000L * DiscordBot.getUserSettingsHandler().getRegisteredSettings().size());

            return;
        }

        if(args[0].equalsIgnoreCase("reset")){
            DiscordBot.getUserSettingsHandler().resetUserSettings(userId);
            bot.info(msgBuilder.buildMessage(Message.CMD_SETTING_RESET));
            return;
        }

        if(args.length < 2){
            bot.info(getDetailedDescription(), true);
            return;
        }

        SettingsHandler handler = DiscordBot.getUserSettingsHandler();
        int index = 0;
        if(args.length >= 3 && args[0].equalsIgnoreCase("server")){
            if(serverAccess){
                handler = bot.getServerSettingsHandler();
                index++;
            }
            else{
                bot.respond(msgBuilder.buildMessage(Message.CMD_SETTING_NO_ACCESS));
                return;
            }
        }

        int choice;
        try{
            choice = Integer.parseInt(args[index]) - 1;
        }
        catch(NumberFormatException e){
            bot.info(msgBuilder.buildMessage(Message.CMD_SETTING_INVALID_NUMBER));
            return;
        }

        if(choice >= handler.getSettings(userId).getSettings().size()){
            bot.info(msgBuilder.buildMessage(Message.CMD_SETTING_INVALID_SETTING, args[index]));
            return;
        }

        Setting setting = handler.getSettings(userId).getSettings().get(choice);

        Object value = setting.parse(args[index + 1]);
        if(value == null){
            bot.info(msgBuilder.buildMessage(Message.CMD_SETTING_INVALID_VALUE, args[1]));
            return;
        }

        handler.setSetting(userId, setting, value);
        bot.info(msgBuilder.buildMessage(Message.CMD_SETTING_SET, setting.getName(bot.getLocale()), setting.getValueAsString(value)));
        if(setting.requiresRestart()) bot.info(msgBuilder.buildMessage(Message.CMD_SETTING_RESTART_REQUIRED, true));
    }

    /*
    @Override
    public String getDetailedDescription(){
        return "Change your personal settings\n" +
                "Usage:\n" +
                " " + this.getHandle() + " list\n" +
                "  *List all settings and their current value*\n" +
                " " + this.getHandle() + " <setting> <value>\n" +
                "  *Change a setting*";
    }
    */
}
