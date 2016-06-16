package bot.commands;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.audio.impl.AudioManager;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import sx.blah.discord.util.audio.AudioPlayer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Owner on 2016-06-14.
 */
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
        return "Sound command";
    }

    @Override
    public void onCommand(IDiscordClient client, IMessage message, String[] args) throws IOException, UnsupportedAudioFileException, RateLimitException, DiscordException, MissingPermissionsException
    {
        MessageBuilder builder = new MessageBuilder(client).withChannel(message.getChannel());

        List<IVoiceChannel> list = message.getAuthor().getConnectedVoiceChannels();


        try
        {
            IVoiceChannel channel = list.get(0);
            AudioManager manager = new AudioManager(message.getGuild());
            AudioPlayer player = new AudioPlayer(manager);

                channel.join();

//            try
//            {
//                Thread.sleep(1000);
//            } catch (InterruptedException e)
//            {
//                Thread.currentThread().interrupt();
//            }

            System.out.println("get to here?");
            File soundFile = new File("C:/Users/Owner/Desktop/macho_alert16.mp3");

            AudioInputStream in = AudioSystem.getAudioInputStream(soundFile);


            player.queue(in);
            player.provide();


            try
            {
                Thread.sleep(4000);
            } catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }

            System.out.println("Tried to play.");

            System.out.println("get to here?#2");


            try
            {
                Thread.sleep(1000);
            } catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }

            System.out.println("get to here?#3");
            channel.leave();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }

}
