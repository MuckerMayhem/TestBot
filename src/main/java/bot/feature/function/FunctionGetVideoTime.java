package bot.feature.function;

import bot.DiscordBot;
import bot.settings.ArraySetting;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import util.YoutubeUtil;

import java.io.IOException;

public class FunctionGetVideoTime extends BotFunction{

    private static final String[] IMPORTANT_REGIONS = {"US", "CA", "AU"};

    private static final ArraySetting SETTING_YTBLACKLIST = new ArraySetting("blacklist_videoinfo", new String[]{});

    @Override
    public void onRegister() {}

    @Override
    public void onEnable(DiscordBot bot){
       bot.getServerSettingsHandler().registerNewSetting(SETTING_YTBLACKLIST);
    }

    @Override
    public void onDisable(DiscordBot bot){

    }


    @EventSubscriber
    public void onMessageEvent(MessageReceivedEvent event) throws IOException, RateLimitException, DiscordException, MissingPermissionsException{
        DiscordBot bot = DiscordBot.getInstance(event.getMessage().getGuild());
        if(bot == null) return;

        for(String s : (String[]) bot.getServerSettingsHandler().getSetting(SETTING_YTBLACKLIST)){
            if(event.getMessage().getChannel().getName().equals(s)) return;
        }

        String content = event.getMessage().getContent();

        //Accounts for most youtube links that Discord can expand
        if(!content.matches(".*http[s]?://(www.)?(?:youtube.com/watch\\?v=|youtu.be/)([\\w-]{11})[^\\w-]?.*")) return;

        //Single out dat id
        String videoId = content.replaceAll(".*http[s]?://(www.)?(?:youtube.com/watch\\?v=|youtu.be/)([\\w-]{11})[^\\w-]?.*", "$2");

        /*Keeping this here to fall back on in case something goes horribly wrong
        String videoId;
        if(content.contains("https://www.youtube.com/watch?v=")){
            videoId = content.replaceAll(".*(http[s]?://.*)[ ].*", "$1").split("=")[1];
        }
        else if(content.contains("https://youtu.be/")){
            videoId = content.replaceAll(".*(http[s]?://.*)[ ].*", "$1").split("be/")[1];
        }
        else return;
        */

        String[] videoInfo = YoutubeUtil.getVideoInfo(videoId);

        StringBuilder builder = new StringBuilder("*Video length: ").append(YoutubeUtil.formatTime((String) videoInfo[2])).append("*");
        if(!videoInfo[3].isEmpty()){
            builder.append("\n*This video blocked in regions: ").append(underlineRegions(videoInfo[3])).append("*");
            for(String s : IMPORTANT_REGIONS){
                if(videoInfo[3].contains(s)){
                    builder.append("\n*Blocked in your region? use http://www.unblockyoutube.co.uk/ to get around it!*");
                    break;
                }
            }
        }

        bot.say(event.getMessage().getChannel(), builder.toString());
    }

    private static String underlineRegions(String regions){
        for(String s : IMPORTANT_REGIONS){
            regions = regions.replaceAll("(.*)(" + s + ")(.*)", "$1__$2__$3");
        }
        return regions;
    }
}
