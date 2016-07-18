package bot.feature.function;

import bot.DiscordBot;
import bot.locale.Message;
import bot.locale.MessageBuilder;
import bot.settings.ArraySetting;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.UserVoiceChannelMoveEvent;
import util.YoutubeUtil;

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
    public void onMessageReceived(DiscordBot bot, MessageReceivedEvent event) throws Exception{
        if(bot == null) return;

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
            builder.append("\n*").append(msgBuilder.buildMessage(Message.FUNC_YTTIME_BLOCKED, underlineRegions(videoInfo[3]))).append("*");
            for(String s : IMPORTANT_REGIONS){
                if(videoInfo[3].contains(s)){
                    builder.append("\n").append(msgBuilder.buildMessage(Message.FUNC_YTTIME_UNBLOCK));
                    break;
                }
            }
        }

        bot.say(event.getMessage().getChannel(), builder.toString());
    }

    @Override
    public void onVoiceChannelMove(DiscordBot bot, UserVoiceChannelMoveEvent event) throws Exception {}

    private static String underlineRegions(String regions){
        for(String s : IMPORTANT_REGIONS){
            regions = regions.replaceAll("(.*)(" + s + ")(.*)", "$1__$2__$3");
        }
        return regions;
    }
}
