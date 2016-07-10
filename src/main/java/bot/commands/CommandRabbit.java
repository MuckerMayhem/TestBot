package bot.commands;

import bot.DiscordBot;
import bot.settings.StringSetting;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.io.IOException;

public class CommandRabbit extends Command{

    private static final StringSetting RABBIT_NAME = new StringSetting("rabbit_room", "Name of your rabb.it room", "");

    @Override
    protected void onRegister(){
        getUserSettingsHandler().registerNewSetting(RABBIT_NAME);
    }

    @Override
    protected void onExecute(DiscordBot bot, IMessage message, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException, IOException{
        String rabbit = (String) getUserSettingsHandler().getSetting(message.getAuthor().getID(), RABBIT_NAME);
        if(rabbit.isEmpty()){
            bot.info("You do not have a rabb.it room set. Use the settings command to set one.", true);
            return;
        }

        bot.respond("http://rabb.it/" + rabbit);
    }
}
