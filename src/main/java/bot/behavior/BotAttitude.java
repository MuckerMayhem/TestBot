package bot.behavior;

import bot.NewBot;
import bot.chatter.ResponseType;
import bot.chatter.Response;

import java.util.*;

public class BotAttitude{

    private static final BotAttitude GLOBAL = new BotAttitude(null, Mood.HAPPY);

    private static HashMap<String, BotAttitude> attitudes = new HashMap<>();

    private String userId;

    private float happiness;
    private float annoyance;

    private static float fatigue;
    private static float boredom;

    private Mood defaultMood;

    private ArrayList<ResponseType> previousQueries = new ArrayList<ResponseType>(5);

    /**
     * Creates a new bot attitude for a user with a default mood
     * The bot will maintain this attitude when speaking with the specified user
     * @param user - UserID of the person this attitude is attached to
     * @param defaultMood - Default mood of the bot
     */
    public BotAttitude(String user, Mood defaultMood){
        this.userId = user;
        this.defaultMood = defaultMood;
        setMood(this.defaultMood);

        if(NewBot.getClient().getUserByID(user) != null){
            attitudes.put(user, this);
        }

        schedule();
    }

    /**
     * Creates a new bot attitude for a user with a random default mood
     * The bot will maintain this attitude when speaking with the specified user
     * @param user
     */
    public BotAttitude(String user){
        this.userId = user;
        this.defaultMood = Mood.values()[new Random().nextInt(Mood.values().length)];
        System.out.println("User: " + NewBot.getClient().getUserByID(user).getName() + ", Mood: " + this.defaultMood.name());
        setMood(this.defaultMood);

        if(NewBot.getClient().getUserByID(user) != null){
            attitudes.put(user, this);
        }

        schedule();
    }

    public static boolean botExistsFor(String user){
        return attitudes.containsKey(user);
    }

    public static BotAttitude getBotFor(String user){
        return attitudes.get(user);
    }

    private void assignBotToUser(String user, BotAttitude personality){
        attitudes.put(user, personality);
    }

    public String talkTo(String input){
        input = input.replaceAll("[.!?']+", "");
        previousQueries.add(null);
        ResponseType type = ResponseType.generateType(input);
        cheer(type.getHappiness());
        annoy(type.getAnnoyance());
        this.boredom = 0;
        return Response.getResponseFor(input, this);
//        return DefaultResponses.getResponseFor(input.replaceAll("[.!?']+", ""));
    }

    public String getUser(){
        return NewBot.getClient().getUserByID(this.userId).getName();
    }

    public float getHappiness(){
        return this.happiness;
    }

    public float getAnnoyance(){
        return this.annoyance;
    }

    public float getFatigue(){
        return fatigue;
    }

    public float getBoredom(){
        return boredom;
    }

    public Mood getDefaultMood(){
        return this.defaultMood;
    }

    public void setMood(Mood newMood){
        this.happiness = newMood.getHappiness();
        this.annoyance = newMood.getAnnoyance();
    }

    public void setDefaultMood(Mood newMood){
        this.defaultMood = newMood;
    }

    public void annoy(float amount){
        this.annoyance += amount;

        if(this.annoyance > 1.0F) this.annoyance = 1.0F;
        if(this.annoyance < 0.0F) this.annoyance = 0.0F;
    }

    public void cheer(float amount){
        this.happiness += amount;

        if(this.happiness > 1.0F) this.happiness = 1.0F;
        if(this.happiness < 0.0F) this.happiness = 0.0F;
    }

    private void schedule(){
        TimerTask task = new TimerTask(){
            @Override
            public void run(){
                tick();
            }
        };
        new Timer().schedule(task, 0L, 30000);//1200000
    }

    private void tick(){
        float happinessDiff = this.defaultMood.getHappiness() - this.happiness;
        float annoyanceDiff = this.defaultMood.getAnnoyance() - this.annoyance;
        if(happinessDiff != 0){
            this.happiness += (happinessDiff / Math.abs(happinessDiff) / 100);
        }

        if(annoyanceDiff != 0){
            this.annoyance += (annoyanceDiff / Math.abs(annoyanceDiff) / 50);
        }

        GLOBAL.cheer((-boredom / 0.6F) * 0.1F);
        GLOBAL.annoy((-boredom / 0.6F) * 0.2F);

        if(boredom < 1.0F) boredom += 0.05F;

        cheer(-0.05F * boredom / 0.15F);
        annoy(boredom / 0.05F);

        if(boredom >= 0.6F){
            NewBot.say(Response.getBoredomMessage(GLOBAL));
            boredom = 0.0F;
        }
    }
}
