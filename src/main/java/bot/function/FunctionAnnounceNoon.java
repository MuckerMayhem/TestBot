package bot.function;

import bot.BotParameters;
import bot.DiscordBot;
import bot.commands.CommandSound;
import bot.commands.sound.Sound;
import sx.blah.discord.handle.obj.IVoiceChannel;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;

public class FunctionAnnounceNoon extends BotFunction{

    private static final String CHANNEL = "General";

    private boolean active;

    public static long timeUntilNextNoon(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime next = now.withHour(12).withMinute(0).withSecond(0).withNano(0);
        if(now.getHour() >= 12){
            next = next.plusDays(1);
        }

        LocalDateTime difference = LocalDateTime.from(now);

        long days = difference.until(next, ChronoUnit.DAYS);
        difference = difference.plusDays(days);

        long hours = difference.until(next, ChronoUnit.HOURS);
        difference = difference.plusHours(hours);

        long minutes = difference.until(next, ChronoUnit.MINUTES);
        difference = difference.plusMinutes(minutes);

        long seconds = difference.until(next, ChronoUnit.SECONDS);

        return (days * 86400000) + (hours * 3600000L) + (minutes * 60000L) + (seconds * 1000L);
    }

    @Override
    public void onActivate(){
        checkTime(timeUntilNextNoon());
    }

    @Override
    public void onDeactivate(){

    }

    public boolean isActive(){
        return this.active;
    }

    private void checkTime(long delay){
        TimerTask task = new TimerTask(){
            @Override
            public void run(){
                if(!FunctionAnnounceNoon.this.active){
                    this.cancel();
                    return;
                }

                DiscordBot bot = FunctionAnnounceNoon.this.bot;
                for(IVoiceChannel v : bot.getClient().getGuildByID(BotParameters.GUILD_ID).getVoiceChannels()){
                    if(v.getName().equals(CHANNEL)){
                        CommandSound.playSound(bot, v, Sound.HIGHNOON);
                        checkTime(timeUntilNextNoon());
                        return;
                    }
                }
            }
        };

        new Timer().schedule(task, delay);
    }
}
