package bot.feature.command;

import bot.DiscordBot;
import bot.feature.command.sound.Sound;
import bot.locale.Message;
import org.tritonus.share.sampled.file.TAudioFileFormat;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.audio.AudioPlayer;
import util.DiscordUtil;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class CommandSound extends BotCommand
{
    public CommandSound(){
        super("sound", Permissions.VOICE_SPEAK);
    }
    
    public static void playSound(DiscordBot bot, IVoiceChannel channel, Sound sound, float volume){
        if(channel == null) return;
        
        long duration = getDuration(sound);
        if(duration <= 0){
            return;
        }

        try{
            channel.join();
        }
        catch(MissingPermissionsException e){
            bot.log(e, "Failed to join voice channel.");
            return;
        }

        AudioPlayer player = new AudioPlayer(channel.getGuild());
        player.setVolume(volume);
        try{
            if(sound.getPath() == null){
                player.queue(sound.getUrl());
            }
            else player.queue(sound.getPath());
            player.provide();
        }
        catch(UnsupportedAudioFileException | IOException e){
            bot.log(e, "Failed to play sound '" + sound.getName() + "'");
            return;
        }

        TimerTask task = new TimerTask(){
            @Override
            public void run(){
                channel.leave();
            }
        };

        new Timer().schedule(task, duration + 400L);
    }
    
    public static void playSound(DiscordBot bot, IVoiceChannel channel, Sound sound){
        playSound(bot, channel, sound, 1.0F);
    }

    @Override
    public void onRegister() {}

    @Override
    public void onEnable(DiscordBot bot) {}

    @Override
    public void onDisable(DiscordBot bot) {}

    @Override
    protected void onExecute(DiscordBot bot, IMessage message, String[] args) throws MissingPermissionsException{
        Sound sound;
        if(args.length == 0){
            sound = Sound.values()[new Random().nextInt(Sound.values().length)];
        }
        else sound = Sound.get(args[0]);

        if(sound == null){
            bot.info(buildMessage(Message.CMD_SOUND_INVALID, args[0]));
            return;
        }

        long duration = getDuration(sound);
        if(duration <= 0){
            return;
        }

        int times = 1;
        if(args.length >= 2){
            try{
                times = Integer.parseInt(args[1]);
            }
            catch(NumberFormatException ignored){}
        }
        if(times <= 0) return;

        float volume = 1.0F;
        if(args.length >= 3){
            try{
                volume = Integer.parseInt(args[2]) / 100F;
            }
            catch(NumberFormatException ignored){}
        }
        if(volume > 3.0F) volume = 3.0F;

        IVoiceChannel channel = DiscordUtil.getVoiceChannel(bot.getGuild(), message.getAuthor());

        if(channel == null){
            bot.info(buildMessage(Message.CMD_SOUND_NOCHANNEL));
        }
        else{
            channel.join();

            AudioPlayer player = new AudioPlayer(message.getGuild());
            player.setVolume(volume);
            try{

                for(int i = 0;i< times;i++){
                    if(sound.getPath() == null){
                        player.queue(sound.getUrl());
                    }
                    else player.queue(sound.getPath());
                }
                player.provide();
            }
            catch(UnsupportedAudioFileException | IOException e){
                bot.log(e, "Could not play sound " + sound.getName());
                return;
            }

            TimerTask task = new TimerTask(){
                @Override
                public void run(){
                    if(channel.isConnected()) 
                        channel.leave();
                }
            };

            new Timer().schedule(task, (duration * times) + 400L);
        }
    }

    private static long getDuration(Sound sound){
        AudioFileFormat format;
        long length;

        try{
            if(sound.getPath() == null){
                format = AudioSystem.getAudioFileFormat(sound.getUrl());
                length = sound.getUrl().openConnection().getContentLength();

            }
            else{
                format = AudioSystem.getAudioFileFormat(sound.getPath());
                length = sound.getPath().length();
            }
        }
        catch(IOException | UnsupportedAudioFileException e){
            System.err.printf("Failed to load sound '%s': %s", sound.getName(), e.getClass().getSimpleName() + "\n");
            return -1;
        }

        long duration;
        if(format instanceof TAudioFileFormat){
            Long micros = (Long) format.properties().get("duration");
            duration = micros / 1000;
        }
        else{
            int frameSize = format.getFormat().getFrameSize();
            float frameRate = format.getFormat().getFrameRate();
            duration = (long) (length / (frameSize * frameRate)) * 1000;
        }
        return duration;
    }
}
