package bot.commands;

import bot.DiscordBot;
import bot.settings.Setting;
import bot.settings.SettingsHandler;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import util.DiscordUtil;

import java.io.IOException;

public class CommandSetting extends Command{
    @Override
    protected void onRegister(){

    }

    @Override
    protected void onExecute(DiscordBot bot, IMessage message, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException, IOException{
        String userId = message.getAuthor().getID();

        if(args.length == 0){
            bot.info(getDetailedDescription(), true);
            return;
        }

        boolean serverAccess = DiscordUtil.userHasPermission(message.getAuthor(), message.getGuild(), Permissions.MANAGE_SERVER);
        if(args[0].equalsIgnoreCase("list")){
            StringBuilder builder = new StringBuilder("Here is the list of settings you can change:\n" +
                    bot.getUserSettingsHandler().getSettings(userId));

            if(serverAccess)
                builder.append("In addition, you have access to the following global (server) settings:\n")
                        .append(DiscordBot.getGlobalSettingsHandler().getSettings().toString());

            bot.respond(builder.toString(), 5000L * bot.getUserSettingsHandler().getRegisteredSettings().size());

            return;
        }

        if(args[0].equalsIgnoreCase("reset")){
            bot.getUserSettingsHandler().resetUserSettings(userId);
            bot.info("Reset all your settings to their default values.");
            return;
        }

        if(args.length < 2){
            bot.info(getDetailedDescription(), true);
            return;
        }

        SettingsHandler handler = bot.getUserSettingsHandler();

        Setting serverSetting = DiscordBot.getGlobalSettingsHandler().getSettingByName(args[0]);
        Setting setting = bot.getUserSettingsHandler().getSettingByName(args[0]);

        if(serverAccess && serverSetting != null){
            setting = serverSetting;
            handler = DiscordBot.getGlobalSettingsHandler();
        }
        else if(setting == null){
            bot.info("Invalid setting '" + args[0] + "'!");
            return;
        }

        Object value = setting.parse(args[1]);
        if(value == null){
            bot.info("Invalid value '" + args[1] + "'");
            return;
        }

        handler.setSetting(userId, setting, value);
        bot.info("Setting " + setting.getName() + " set to *" + setting.getValueAsString(value) + "*");
    }

    @Override
    public String getDetailedDescription(){
        return "Change your personal settings\n" +
                "Usage:\n" +
                " " + this.getHandle() + " list\n" +
                "  *List all settings and their current value*\n" +
                " " + this.getHandle() + " <setting> <value>\n" +
                "  *Change a setting*";
    }
}
