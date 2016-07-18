package bot.feature.function;

import bot.DiscordBot;
import bot.locale.Message;
import bot.locale.MessageBuilder;
import bot.settings.BooleanSetting;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.UserVoiceChannelMoveEvent;
import sx.blah.discord.handle.obj.IChannel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class FunctionBreakMessages extends BotFunction{

    public static final String BREAKUP_MESSAGE = "-";
    public static final int MAX_MESSAGES = 12;

    private static final Random RANDOM = new Random();

    private static final HashMap<IChannel, String> messages = new HashMap<>();
    private static final HashMap<IChannel, Integer> counts = new HashMap<>();
    private static final HashMap<String, String> facts = new HashMap<>();
    
    //Setting for this function
    private static final BooleanSetting ALLOW_WALL_BREAKING = new BooleanSetting("break_walls", true);

    private static MessageReceivedEvent lastEvent = null;
    
    private static String lastFact = "";

    @Override
    public void onRegister(){
        DiscordBot.getUserSettingsHandler().registerNewSetting(ALLOW_WALL_BREAKING);

        if(facts.isEmpty()){
            Document doc;
            try{
                doc = Jsoup.connect("http://mentalfloss.com/api/1.0/views/amazing_facts.json").ignoreContentType(true).get();
            }
            catch(IOException e){
                System.err.print("Failed to load facts from mentalfloss.com :(");
                return;
            }

            JsonElement element = new JsonParser().parse(doc.body().text());
            for(JsonElement e : element.getAsJsonArray()){
                JsonObject object = e.getAsJsonObject();
                String fact = object.get("nid").getAsString().replace("\\\\", "");
                String author = object.get("submitted by").getAsString();
                facts.put(fact, author);
            }
        }

        System.out.println("Successfully loaded facts list.");
    }

    @Override
    public void onEnable(DiscordBot bot) {}

    @Override
    public void onDisable(DiscordBot bot) {}

    @Override
    public void onMessageReceived(DiscordBot bot, MessageReceivedEvent event){
        if(bot == null) return;

        if(!bot.checkSetting(event.getMessage().getAuthor().getID(), ALLOW_WALL_BREAKING)) return;

        IChannel channel = event.getMessage().getChannel();

        if(messages.containsKey(channel)){
            if(messages.get(channel).equals(event.getMessage().getAuthor().getID())){
                counts.put(channel, counts.get(channel) + 1);
            }
            else counts.put(channel, 0);

            if(counts.get(channel) >= MAX_MESSAGES + RANDOM.nextInt(7) - 3){
                try{
                    String fact = randomFact(bot);
                    DiscordBot.getGuildlessInstance().say(event.getMessage().getChannel(), fact);
                    if(fact.equalsIgnoreCase(lastFact)){
                        DiscordBot.getGuildlessInstance().type(event.getMessage().getChannel(), "...but you probably already knew that! \uD83D\uDE04", 3000L);
                    }
                    lastFact = fact;
                }
                catch(IOException e){
                    DiscordBot.getGuildlessInstance().say(BREAKUP_MESSAGE);
                }
                counts.put(channel, 0);
            }
        }
        else{
            messages.put(channel, event.getMessage().getAuthor().getID());
            counts.put(channel, 1);
        }

        messages.put(channel, event.getMessage().getAuthor().getID());
    }

    @Override
    public void onVoiceChannelMove(DiscordBot bot, UserVoiceChannelMoveEvent event) throws Exception {}

    private String randomFact(DiscordBot bot) throws IOException{
        MessageBuilder msgBuilder = new MessageBuilder(bot.getLocale());

        StringBuilder builder = new StringBuilder();
        String fact = new ArrayList<>(facts.keySet()).get(new Random().nextInt(facts.size()));

        builder.append(msgBuilder.buildMessage(Message.FUNC_BREAK_INTRO))
                .append(fact).append("\n")
                .append(msgBuilder.buildMessage(Message.FUNC_BREAK_FROM, " http://mentalfloss.com/"));

        if(!facts.get(fact).isEmpty()){
            builder.append(" ").append(msgBuilder.buildMessage(Message.FUNC_BREAK_SUBMITTED, facts.get(fact)));
        }
        builder.append("*");

        return builder.toString();
    }
}
