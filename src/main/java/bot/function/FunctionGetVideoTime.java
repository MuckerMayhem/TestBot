package bot.function;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import util.YoutubeUtil;

import java.io.IOException;

public class FunctionGetVideoTime extends BotFunction{

    private static final String[] IMPORTANT_REGIONS = {"US", "CA"};

    @Override
    public void onActivate(){

    }

    @Override
    public void onDeactivate(){

    }

    @EventSubscriber
    public void onMessageEvent(MessageReceivedEvent event) throws IOException, RateLimitException, DiscordException, MissingPermissionsException{
        String content = event.getMessage().getContent();

        String videoId;
        if(content.contains("https://www.youtube.com/watch?v=")){
            videoId = content.replaceAll(".*(http[s]?://.*)[ ].*", "$1").split("=")[1];
        }
        else if(content.contains("https://youtu.be/")){
            videoId = content.replaceAll(".*(http[s]?://.*)[ ].*", "$1").split("be/")[1];
        }
        else return;

        String[] videoInfo = YoutubeUtil.getVideoInfo(videoId);
        System.out.println(videoInfo[2]);

        StringBuilder builder = new StringBuilder("*Video length: ").append(YoutubeUtil.formatTime((String) videoInfo[2])).append("*");
        if(!videoInfo[3].isEmpty()){
            builder.append("\n*This video blocked in countries: ").append(underlineRegions(videoInfo[3])).append("*");
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
