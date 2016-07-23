package bot.feature.command;

import bot.DiscordBot;
import bot.feature.command.sound.Sound;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.handle.obj.Permissions;

import java.util.Arrays;
import java.util.Optional;

public class CommandBooty extends BotCommand{

    private static final String[] theBooty = {"͡°", "͜ʖ", "͡°)"};
    private static final String[] theBooty2x = {"͡⊙", "͜ʖ", "͡⊙)"};
    private static final String[] theBooty3x = {"͡◉", "͜ʖ", "͡◉)"};
    private static final String[] theBootySlow = {"͡o", "͜ʖ", "͡o)"};

    public CommandBooty(){
        super("(", Permissions.VOICE_SPEAK);
    }
    
    @Override
    public void onRegister() {}

    @Override
    public void onEnable(DiscordBot bot) {}

    @Override
    public void onDisable(DiscordBot bot) {}

    @Override
    protected void onExecute(DiscordBot bot, IMessage message, String[] args){
        if(args.length < 3) return;

        Optional<IVoiceChannel> channel = message.getAuthor().getConnectedVoiceChannels().stream().filter(c -> c.getGuild() == bot.getGuild()).findFirst();

        if(Arrays.equals(theBooty, args)) CommandSound.playSound(bot, channel.orElseGet(null), Sound.BOOTY);
        else if(Arrays.equals(theBooty2x, args)) CommandSound.playSound(bot, channel.orElseGet(null), Sound.BOOTY2X);
        else if(Arrays.equals(theBooty3x, args)) CommandSound.playSound(bot, channel.orElseGet(null), Sound.BOOTY3X);
        else if(Arrays.equals(theBootySlow, args)) CommandSound.playSound(bot, channel.orElseGet(null), Sound.BOOTYSLOW);
    }
}
