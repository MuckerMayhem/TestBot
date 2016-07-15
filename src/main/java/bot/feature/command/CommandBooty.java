package bot.feature.command;

import bot.DiscordBot;
import bot.feature.command.sound.Sound;
import sx.blah.discord.handle.obj.IMessage;

import java.util.Arrays;

public class CommandBooty extends BotCommand{

    private static String[] theBooty = {"͡°", "͜ʖ", "͡°)"};
    private static String[] theBooty2x = {"͡⊙", "͜ʖ", "͡⊙)"};
    private static String[] theBooty3x = {"͡◉", "͜ʖ", "͡◉)"};
    private static String[] theBootySlow = {"͡o", "͜ʖ", "͡o)"};

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
    protected void onExecute(DiscordBot bot, IMessage message, String[] args){
        if(args.length < 3) return;

        if(!message.getAuthor().getVoiceChannel().isPresent()) return;

        if(Arrays.equals(theBooty, args)) CommandSound.playSound(bot, message.getAuthor().getVoiceChannel().get(), Sound.BOOTY);
        else if(Arrays.equals(theBooty2x, args)) CommandSound.playSound(bot, message.getAuthor().getVoiceChannel().get(), Sound.BOOTY2X);
        else if(Arrays.equals(theBooty3x, args)) CommandSound.playSound(bot, message.getAuthor().getVoiceChannel().get(), Sound.BOOTY3X);
        else if(Arrays.equals(theBootySlow, args)) CommandSound.playSound(bot, message.getAuthor().getVoiceChannel().get(), Sound.BOOTYSLOW);
    }

    @Override
    public String getDetailedDescription(){
        return "Acquires the booty\n" +
                "Usage: " + CommandHandler.getCommandPrefix() + "( ͡° ͜ʖ ͡°)";
    }
}
