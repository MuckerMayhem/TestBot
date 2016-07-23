package bot.feature.function;

import bot.DiscordBot;
import bot.event.BotEventSubscriber;
import bot.event.BotMessageReceivedEvent;
import bot.feature.ToggleableBotFeature;
import bot.locale.Message;
import bot.locale.MessageBuilder;
import bot.settings.ArraySetting;
import util.YoutubeUtil;

import java.io.IOException;

public class FunctionGetVideoTime extends BotFunction implements ToggleableBotFeature{
    
    private static final ArraySetting SETTING_REGIONS = new ArraySetting("important_regions", new String[]{});
    private static final ArraySetting SETTING_YTBLACKLIST = new ArraySetting("blacklist_videoinfo", new String[]{});

    public FunctionGetVideoTime(){
        super("yttime");
    }

    @Override
    public boolean defaultEnabled(){
        return true;
    }
    
    @Override
    public void onRegister() {}

    @Override
    public void onEnable(DiscordBot bot){
        bot.getServerSettingsHandler().addSetting(SETTING_REGIONS);
        bot.getServerSettingsHandler().addSetting(SETTING_YTBLACKLIST);
        bot.getEventDispatcher().registerListener(this);
    }
    
    @Override
    public void onDisable(DiscordBot bot){
        bot.getServerSettingsHandler().removeSetting(SETTING_REGIONS);
        bot.getServerSettingsHandler().removeSetting(SETTING_YTBLACKLIST);
        bot.getEventDispatcher().unregisterListener(this);
    }

    @BotEventSubscriber
    public void onMessageReceived(BotMessageReceivedEvent event) throws IOException{
        DiscordBot bot = event.getBot();

        for(String s : (String[]) bot.getServerSettingsHandler().getSetting(SETTING_YTBLACKLIST)){
            if(event.getMessage().getChannel().getName().equals(s)) return;
        }

        MessageBuilder msgBuilder = new MessageBuilder(bot.getLocale());

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

        StringBuilder builder = new StringBuilder("*").append(msgBuilder.buildMessage(Message.FUNC_YTTIME_LENGTH, YoutubeUtil.formatTime(videoInfo[2]))).append("*");
        if(!videoInfo[3].isEmpty()){
            String[] regions = (String[]) bot.getServerSettingsHandler().getSetting(SETTING_REGIONS);
            builder.append("\n*").append(msgBuilder.buildMessage(Message.FUNC_YTTIME_BLOCKED, underlineRegions(regions, videoInfo[3]))).append("*");
            for(String s : regions){
                if(videoInfo[3].contains(s)){
                    builder.append("\n").append(msgBuilder.buildMessage(Message.FUNC_YTTIME_UNBLOCK));
                    break;
                }
            }
        }

        bot.say(event.getMessage().getChannel(), builder.toString());
    }
    
    private static String underlineRegions(String[] regions, String string){
        for(String s : regions){
            string = string.replaceAll("(.*)(" + s + ")(.*)", "$1__$2__$3");
        }
        return string;
    }
}
