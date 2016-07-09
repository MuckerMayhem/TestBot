package bot.commands;

import bot.DiscordBot;
import bot.settings.Setting;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

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

        if(args[0].equalsIgnoreCase("list")){
            bot.respond("Here is the list of settings you can change:\n" +
                        bot.getSettingsHandler().getUserSettings(userId), 10000L);
            return;
        }

        if(args[0].equalsIgnoreCase("reset")){
            bot.getSettingsHandler().resetUserSettings(userId);
            bot.info("Reset all your settings to their default values.");
            return;
        }

        if(args.length < 2){
            bot.info(getDetailedDescription(), true);
            return;
        }

        Setting setting = bot.getSettingsHandler().getSettingByName(args[0]);
        if(setting == null){
            bot.info("Invalid setting '" + args[0] + "'!");
            return;
        }

        Object value = setting.parse(args[1]);
        if(value == null){
            bot.info("Invalid value '" + args[1] + "'");
            return;
        }

        bot.getSettingsHandler().setUserSetting(userId, setting, value);
        bot.info("Setting " + setting.getName() + " set to *" + value + "*");
        return;
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
