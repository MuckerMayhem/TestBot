package bot.function;

import bot.DiscordBot;
import bot.settings.BooleanSetting;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class FunctionBreakMessages extends BotFunction{

    public static final String BREAKUP_MESSAGE = "-";
    public static final int MAX_MESSAGES = 12;

    private static final Random RANDOM = new Random();

    //Setting for this function
    private static final BooleanSetting ALLOW_WALL_BREAKING = new BooleanSetting("break_walls", "Change whether the bot can break up your walls of text", true);

    private static MessageReceivedEvent lastEvent = null;
    private static HashMap<IChannel, String> messages = new HashMap<>();
    private static HashMap<IChannel, Integer> counts = new HashMap<>();

    private static HashMap<String, String> facts = new HashMap<>();
    private static String lastFact = "";

    @Override
    public void init(){
        this.bot.getUserSettingsHandler().registerNewSetting(ALLOW_WALL_BREAKING);
    }

    @Override
    protected void onActivate(){
        if(facts.isEmpty()){
            Document doc = null;
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

            facts.put("Cole is bae ❤", "wiizerdofwiierd");
        }

        System.out.println("Successfully loaded facts list.");
    }

    @Override
    protected void onDeactivate(){

    }

    @EventSubscriber
    public void onMessageEvent(MessageReceivedEvent event){
        if(!checkSetting(event, ALLOW_WALL_BREAKING)) return;

        IChannel channel = event.getMessage().getChannel();

        if(messages.containsKey(channel)){
            if(messages.get(channel).equals(event.getMessage().getAuthor().getID())){
                counts.put(channel, counts.get(channel) + 1);
            }
            else counts.put(channel, 0);

            if(counts.get(channel) >= MAX_MESSAGES + RANDOM.nextInt(7) - 3){
                try{
                    String fact = randomFact();
                    DiscordBot.instance.say(event.getMessage().getChannel(), fact);
                    if(fact.equalsIgnoreCase(lastFact)){
                        DiscordBot.instance.type(event.getMessage().getChannel(), "...but you probably already knew that! \uD83D\uDE04", 3000L);
                    }
                    lastFact = fact;
                }
                catch(IOException e){
                    DiscordBot.instance.say(BREAKUP_MESSAGE);
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

    private String randomFact() throws IOException{
        StringBuilder builder = new StringBuilder();
        String fact = new ArrayList<>(facts.keySet()).get(new Random().nextInt(facts.size()));
        builder.append("Did you know? ").append(fact).append("\n*Fact from http://mentalfloss.com/");
        if(!facts.get(fact).isEmpty()){
            builder.append(" Submitted by: ").append(facts.get(fact));
        }
        builder.append("*");

        return builder.toString();
    }
}
