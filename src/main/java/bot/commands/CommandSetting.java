package bot.commands;

import bot.DiscordBot;
import bot.settings.Settings;
import bot.settings.SettingsHandler.Setting;
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
            bot.getSettingsHandler().setUserSettings(userId, Settings.DEFAULT);
            bot.info("Reset all your settings to their default values.");
            return;
        }

        if(args.length < 2){
            bot.info(getDetailedDescription(), true);
            return;
        }

        if(!"truefalsedefault".contains(args[1].toLowerCase())) return;

        for(Setting s : Setting.values()){
            if(args[0].equalsIgnoreCase(s.getName())){
                boolean value = args[1].equalsIgnoreCase("default") ? s.getDefault() : Boolean.parseBoolean(args[1]);
                bot.getSettingsHandler().setUserSetting(userId, s, value);
                bot.info("Setting " + s.getName() + " set to *" + value + "*");
                return;
            }
        }
    }

    @Override
    public String getDetailedDescription(){
        return "Change your personal settings\n" +
                "Usage:\n" +
                " " + this.getHandle() + " list\n" +
                "  *List all settings and their current value*\n" +
                " " + this.getHandle() + " <setting> <true/false>\n" +
                "  *Change a setting*";
    }
}
