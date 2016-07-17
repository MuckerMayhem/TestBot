package bot.feature.command;

import bot.DiscordBot;
import bot.feature.command.sound.Sound;
import bot.locale.Message;
import bot.locale.MessageBuilder;
import org.tritonus.share.sampled.file.TAudioFileFormat;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.audio.AudioPlayer;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class CommandSound extends BotCommand
{
    public static void playSound(DiscordBot bot, IVoiceChannel channel, Sound sound){
        long duration = getDuration(sound);
        if(duration <= 0){
            return;
        }

        try{
            channel.join();
        }
        catch(MissingPermissionsException e){
            e.printStackTrace();
        }

        AudioPlayer player = new AudioPlayer(channel.getGuild());
        try{
            if(sound.getPath() == null){
                player.queue(sound.getUrl());
            }
            else player.queue(sound.getPath());
            player.provide();
        }
        catch(UnsupportedAudioFileException | IOException e){
            bot.log(e, "Could not play sound " + sound.getName());
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
    protected void onExecute(DiscordBot bot, IMessage message, String[] args) throws MissingPermissionsException{
        Sound sound;
        if(args.length == 0){
            sound = Sound.values()[new Random().nextInt(Sound.values().length)];
        }
        else sound = Sound.get(args[0]);

        MessageBuilder builder = new MessageBuilder(bot.getLocale());

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

        if(message.getAuthor().getVoiceChannel().isPresent()){
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
                bot.log(e, "Could not play sound " + sound.getName());
                return;
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
            bot.info(buildMessage(Message.CMD_SOUND_NOCHANNEL));
        }
    }

    /*
    @Override
    public String getDetailedDescription(){
        return "Plays a sound\n" +
                "Usage: " + this.commandHandler.getCommandPrefix() + this.name + " <sound> [times] [volume]\n" +
                "Will play a sound the specified number of times at the specified volume.\n" +
                "Times can be any integer greater than zero and volume is the volume of the sound, as a percentage value from 0-300.\n" +
                "The following are valid sounds:```python\n" +
                String.join("\n", Arrays.stream(Sound.values())
                        .filter(s -> s.getName() != null)
                        .map(Sound::getName)
                        .sorted()
                        .collect(Collectors.toList())) + "```";
    }
    */

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
