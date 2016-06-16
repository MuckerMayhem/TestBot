package bot.commands;

import bot.NewBot;
import bot.commands.sound.Sound;
import org.tritonus.share.sampled.file.TAudioFileFormat;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import sx.blah.discord.util.audio.AudioPlayer;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class CommandSound extends BotCommand
{
    @Override
    public String getName()
    {
        return "sound";
    }

    @Override
    public String getDescription()
    {
        return "Play a sound";
    }

    @Override
    public void onCommand(IDiscordClient client, IMessage message, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException{
        if(args.length == 0){
            new MessageBuilder(NewBot.getClient()).withChannel(message.getChannel())
                    .appendContent("No sound specified!").build();
            return;
        }

        Sound sound = Sound.get(args[0]);
        if(sound == null){
            new MessageBuilder(NewBot.getClient()).withChannel(message.getChannel())
                    .appendContent("Invalid sound '" + args[0] + "'").build();
            return;
        }

        int times = 1;
        if(args.length >= 2){
            try{
                times = Integer.parseInt(args[1]);
            }
            catch(NumberFormatException ignored){}
        }

        if(message.getAuthor().getVoiceChannel().isPresent()){
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
                System.out.printf("Failed to load sound '%s': %s", args[1], e.getClass().getSimpleName());
                return;
            }

            long duration;
            if(format instanceof TAudioFileFormat){
                Long micros = (Long) ((TAudioFileFormat) format).properties().get("duration");
                duration = micros / 1000;
            }
            else return;

            IVoiceChannel channel = message.getAuthor().getVoiceChannel().get();
            channel.join();

            AudioPlayer player = new AudioPlayer(message.getGuild());
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
            new MessageBuilder(NewBot.getClient()).withChannel(message.getChannel())
                    .appendContent("You are not in a voice channel!").build();
        }
    }

}
