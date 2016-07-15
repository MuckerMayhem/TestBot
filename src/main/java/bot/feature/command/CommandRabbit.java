package bot.feature.command;

import bot.DiscordBot;
import bot.locale.Message;
import bot.settings.StringSetting;
import sx.blah.discord.handle.obj.IMessage;

public class CommandRabbit extends BotCommand{

    private static final StringSetting RABBIT_NAME = new StringSetting("rabbit_room", "");

    @Override
    public void onRegister(){
        DiscordBot.getUserSettingsHandler().registerNewSetting(RABBIT_NAME);
    }

    @Override
    public void onEnable(DiscordBot bot) {}

    @Override
    public void onDisable(DiscordBot bot) {}

    @Override
    protected void onExecute(DiscordBot bot, IMessage message, String[] args){
        String rabbit = (String) getUserSetting(message.getAuthor().getID(), RABBIT_NAME);
        if(rabbit.isEmpty()){
            bot.info(buildMessage(Message.CMD_RABBIT_NOT_FOUND), true);
            return;
        }

        bot.respond("http://rabb.it/" + rabbit + (args.length > 0 ? " — " + String.join(" ", args) : ""));
    }
}
