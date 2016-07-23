package bot.feature.function;

import bot.DiscordBot;
import bot.event.BotEventSubscriber;
import bot.event.BotMessageReceivedEvent;
import bot.feature.ToggleableBotFeature;
import bot.locale.Message;
import bot.locale.MessageBuilder;
import bot.settings.BooleanSetting;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class FunctionBreakMessages extends BotFunction implements ToggleableBotFeature{

    public static final String BREAKUP_MESSAGE = "-";
    public static final int MAX_MESSAGES = 12;

    private static final Random RANDOM = new Random();

    private static final HashMap<IChannel, String> messages = new HashMap<>();
    private static final HashMap<IChannel, Integer> counts = new HashMap<>();
    private static final HashMap<IChannel, String> last = new HashMap<>();
    private static final HashMap<String, String> facts = new HashMap<>();
    
    //Setting for this function
    private static final BooleanSetting ALLOW_WALL_BREAKING = new BooleanSetting("break_walls", true);

    private static MessageReceivedEvent lastEvent = null;
    
    public FunctionBreakMessages(){
        super("breakwalls");
    }

    @Override
    public void onRegister(){
        DiscordBot.getUserSettingsHandler().addSetting(ALLOW_WALL_BREAKING);

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
                facts.put(fact, "http://mentalfloss.com/");
            }
            
            try{
                doc = Jsoup.connect("http://catfacts-api.appspot.com/api/facts?number=100").ignoreContentType(true).get();
            }
            catch(IOException e){
                System.err.println("Failed to load cat facts! :(");
                return;
            }
            
            element = new JsonParser().parse(doc.body().text());
            if(!element.isJsonObject()) return;
            
            JsonElement factArray = element.getAsJsonObject().get("facts");
            if(!factArray.isJsonArray()) return;
            
            for(JsonElement e : factArray.getAsJsonArray()){
                facts.put(e.getAsString(), "https://catfacts-api.appspot.com/credit.html");
            }
        }

        System.out.println("Successfully loaded facts list.");
    }

    @Override
    public void onEnable(DiscordBot bot){
        bot.getEventDispatcher().registerListener(this);
    }

    @Override
    public void onDisable(DiscordBot bot){
        bot.getEventDispatcher().unregisterListener(this);
    }
    
    @BotEventSubscriber
    public void onMessageReceived(BotMessageReceivedEvent event){
        DiscordBot bot = event.getBot();

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
                    if(fact.equals(last.get(channel))){
                        DiscordBot.getGuildlessInstance().type(event.getMessage().getChannel(), "...but you probably already knew that! \uD83D\uDE04", 3000L);
                    }
                    last.put(channel, fact);
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
    
    private String randomFact(DiscordBot bot) throws IOException{
        MessageBuilder msgBuilder = new MessageBuilder(bot.getLocale());

        StringBuilder builder = new StringBuilder();
        String fact = new ArrayList<>(facts.keySet()).get(new Random().nextInt(facts.size()));

        builder.append(msgBuilder.buildMessage(Message.FUNC_BREAK_INTRO))
                .append(" ")
                .append(fact)
                .append("\n*")
                .append(msgBuilder.buildMessage(Message.FUNC_BREAK_FROM, facts.get(fact)))
                .append("*");

        return builder.toString();
    }
}
