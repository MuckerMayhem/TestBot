package bot.commands;

import bot.DiscordBot;
import bot.commands.sound.Sound;
import org.tritonus.share.sampled.file.TAudioFileFormat;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import sx.blah.discord.util.audio.AudioPlayer;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class CommandSound extends Command
{
    public static void playSound(DiscordBot bot, IVoiceChannel channel, Sound sound){

        AudioFileFormat format;
        long length;

        try{
            if(sound.getPath() == null){
                format = AudioSystem.getAudioFileFormat(sound.getUrl());

            }
            else{
                format = AudioSystem.getAudioFileFormat(sound.getPath());
            }
        }
        catch(IOException | UnsupportedAudioFileException e){
            System.err.printf("Failed to load sound '%s': %s", sound.getName(), e.getClass().getSimpleName());
            return;
        }

        long duration;
        if(format instanceof TAudioFileFormat){
            System.out.println("TRIGGERED");
            Long micros = (Long) format.properties().get("duration");
            duration = micros / 1000;
        }
        else
        {
            System.out.println("NOT TRIGGERED.");
            return;
        }

        System.out.println("Duration: " + duration);

        channel.join();

        AudioPlayer player = new AudioPlayer(channel.getGuild());
        try{
            if(sound.getPath() == null){
                player.queue(sound.getUrl());
            }
            else player.queue(sound.getPath());
            player.provide();
        }
        catch(UnsupportedAudioFileException | IOException e){
            e.printStackTrace();
        }

        TimerTask task = new TimerTask(){
            @Override
            public void run(){
                channel.leave();
            }
        };

        new Timer().schedule(task, duration + 400L);
    }

    @Override
    public void onExecute(DiscordBot bot, IMessage message, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException{

        Sound sound;
        if(args.length == 0){
            sound = Sound.values()[new Random().nextInt(Sound.values().length)];
        }
        else sound = Sound.get(args[0]);

        if(sound == null){
            bot.respond("Invalid sound '" + args[0] + "'");
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

        if(message.getAuthor().getVoiceChannel().isPresent()){

            long duration = getDuration(sound);
            if(duration == -1){
                return;
            }
            /*
            AudioFileFormat format;
            long length;

            try{
                if(sound.getPath() == null){
                    format = AudioSystem.getAudioFileFormat(sound.getUrl());

                }
                else{
                    format = AudioSystem.getAudioFileFormat(sound.getPath());
                }
            }
            catch(IOException | UnsupportedAudioFileException e){
                System.err.printf("Failed to load sound '%s': %s", args[1], e.getClass().getSimpleName());
                return;
            }

            long duration;
            if(format instanceof TAudioFileFormat){
                Long micros = (Long) ((TAudioFileFormat) format).properties().get("duration");
                duration = micros / 1000;
            }
            else return;
            */

            IVoiceChannel channel = message.getAuthor().getVoiceChannel().get();
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
                e.printStackTrace();
            }

            TimerTask task = new TimerTask(){
                @Override
                public void run(){
                    channel.leave();
                }
            };

            new Timer().schedule(task, (duration * times) + 400L);
        }
        else{
            bot.say(message.getChannel(), "You are not in a voice channel!");
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
            System.err.printf("Failed to load sound '%s': %s", sound.getName(), e.getClass().getSimpleName());
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
            duration = (long) (length / (frameSize * frameRate) * 1000);
        }
        return duration;
    }
}
