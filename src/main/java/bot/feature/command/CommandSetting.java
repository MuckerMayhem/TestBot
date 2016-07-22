package bot.feature.command;

import bot.DiscordBot;
import bot.locale.Message;
import bot.settings.Setting;
import bot.settings.SettingsHandler;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.Permissions;
import util.DiscordUtil;

public class CommandSetting extends BotCommand{

    public CommandSetting(){
        super("setting");
    }

    @Override
    public void onRegister() {}

    @Override
    public void onEnable(DiscordBot bot) {}

    @Override
    public void onDisable(DiscordBot bot) {}
    
    @Override
    protected void onExecute(DiscordBot bot, IMessage message, String[] args){
        String userId = message.getAuthor().getID();

        if(args.length == 0){
            bot.info(getDetailedDescription(), true);
            return;
        }
   
        boolean serverAccess = DiscordUtil.userHasPermission(message.getAuthor(), message.getGuild(), Permissions.MANAGE_SERVER);
        if(args[0].equalsIgnoreCase(getLocalArgs()[0])){
            StringBuilder builder = new StringBuilder(buildMessage(Message.CMD_SETTING_LIST) + "\n" +
                    DiscordBot.getUserSettingsHandler().getSettings(userId).toString(bot.getLocale()));

            if(serverAccess)
                builder.append(buildMessage(Message.CMD_SETTING_MORE))
                        .append("\n")
                        .append(bot.getServerSettingsHandler().getSettings().toString(bot.getLocale()));

            builder.append("\n").append(buildMessage(Message.CMD_SETTING_ENTER_NUMBER));
            if(serverAccess) builder.append("\n").append(buildMessage(Message.CMD_SETTING_ENTER_NUMBER_SERVER));

            bot.respond(builder.toString(), 5000L * DiscordBot.getUserSettingsHandler().getAddedSettings().size());

            return;
        }

        if(args[0].equalsIgnoreCase(getLocalArgs()[1])){
            DiscordBot.getUserSettingsHandler().resetUserSettings(userId);
            bot.info(buildMessage(Message.CMD_SETTING_RESET));
            return;
        }

        if(args.length < 2){
            bot.info(getDetailedDescription(), true);
            return;
        }

        SettingsHandler handler = DiscordBot.getUserSettingsHandler();
        int index = 0;
        if(args.length >= 3 && args[0].equalsIgnoreCase(getLocalArgs()[2])){
            if(serverAccess){
                handler = bot.getServerSettingsHandler();
                index++;
            }
            else{
                bot.respond(buildMessage(Message.CMD_SETTING_NO_ACCESS));
                return;
            }
        }

        int choice;
        try{
            choice = Integer.parseInt(args[index]) - 1;
        }
        catch(NumberFormatException e){
            bot.info(buildMessage(Message.CMD_SETTING_INVALID_NUMBER));
            return;
        }

        if(choice >= handler.getSettings(userId).getSettings().size()){
            bot.info(buildMessage(Message.CMD_SETTING_INVALID_SETTING, args[index]));
            return;
        }

        Setting setting = handler.getSettings(userId).getSettings().get(choice);

        Object value = setting.parse(args[index + 1]);
        if(value == null){
            bot.info(buildMessage(Message.CMD_SETTING_INVALID_VALUE, args[1]));
            return;
        }

        handler.setSetting(userId, setting, value);
        bot.info(buildMessage(Message.CMD_SETTING_SET, setting.getName(bot.getLocale()), setting.getValueAsString(value)));
        
        if(setting.requiresRestart())
            bot.info(buildMessage(Message.MSG_RESTART_REQUIRED), true);
    }
}
