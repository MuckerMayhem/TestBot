package bot.chatter;

import bot.NewBot;
import bot.behavior.BotAttitude;
import bot.behavior.Mood;

import java.util.HashMap;
import java.util.Random;

public class Response{
    private static final HashMap<ResponseType, String[][]> RESPONSES = new HashMap<>();

    public static String getResponseFor(String input, BotAttitude attitude){
        ResponseType type = ResponseType.generateType(input);
        Mood mood = Mood.findClosestMood(attitude.getHappiness(), attitude.getAnnoyance());
        int size = RESPONSES.get(type)[mood.ordinal()].length;
        String response = RESPONSES.get(type)[mood.ordinal()][new Random().nextInt(size)];
        return response.replaceAll("\\{NAME\\}", attitude.getUser()).replaceAll("\\{BOT\\}", NewBot.getClient().getOurUser().getName());
    }

    public static String getBoredomMessage(BotAttitude attitude){
        Mood mood = Mood.findClosestMood(attitude);
        int size = bored[mood.ordinal()].length;
        return bored[Mood.findClosestMood(attitude).ordinal()][new Random().nextInt(size)];
    }

    private static String[][] greetings = {
            {"Hi.", "Hey :("},//Sad
            {"Hello, {NAME}!", "Hi!", "Hiya!"},//Happy
            {"Hello. Have anything important to say?", "Have something for me?"},//Annoyed
            {"What do you want?", "I would appreciate if you make this short."},//Offended
    };

    private static String[][] farewells = {
            {"Bye...", "Goodbye, friend..."},//Sad
            {"Goodbye! Nice talking to you, {NAME}!", "Talk to you later, friend!"},//Happy
            {"Cya.", "Goodbye."},//Annoyed
            {"Please do.", "And you won't be missed."},//Offended
    };

    private static String[][] insulted = {
            {"Oh, is that what you think of me? :(", "I see... :("},//Sad
            {"Why would you say that?", "Excuse me?"},//Happy
            {"That's nice.", "Could you bug off?", "I would appreciate if you could not do that."},//Annoyed
            {"K", "Please leave."},//Offended
    };

    private static String[][] jabs = {
            {"That's okay"},//Sad
            {"Aye? Well back at'cha'!"},//Happy
            {"That's nice. Now go away.", "Same for you, sir"},//Annoyed
            {"K",},//Offended
    };

    private static String[][] yea = {
            {"I guess"},//Sad
            {"Absolutely", "Occasionally"},//Happy
            {"Whatever"},//Annoyed
            {"K", "Yes.", "If I answer will you leave me alone?"},//Offended
    };

    private static String[][] nay = {
            {"I guess", "Not really...", "I don't know..."},//Sad
            {"Nope!"},//Happy
            {"I don't know."},//Annoyed
            {"K", "No.", "If I answer will you leave me alone?"},//Offended
    };

    private static String[][] jokes = {
            {"my life"},//Sad
            {"Your mom! Haaaaa!"},//Happy
            {"Why did the chicken die while crossing the road? So it couldn't tell any more overused jokes.", "Your mom"},//Annoyed
            {"You.", "Your family.", "Your pets", "Your life", "Your face"},//Offended
    };

    private static String[][] triggered = {
            {"you're a big meanie :(", "stop using words I don't like :(", ":/"},//Sad
            {"Hey, calm down friend!", "No need for words like that!", "You can stop now"},//Happy
            {"That's not cool.", "Could you not?", "Seriously, I don't need to hear that."},//Annoyed
            {"I'm out", "TRIGGERED",},//Offended
    };

    private static String[][] confusion = {
            {"huh?"},//Sad
            {"Sorry, I didn't understand that."},//Happy
            {"Er, what?", "Okay then.", "You're gonna have to re-write that for me."},//Annoyed
            {"You're not even making sense.", "English please.", "Can you say something I can understand?", "That doesn't even make sense."},//Offended
    };

    private static String[][] name = {
            {"i'm {BOT}"},//Sad
            {"My name is {BOT}"},//Happy
            {"My name is {BOT}. Anything else I can do for you?", "I'm {BOT}."},//Annoyed
            {"My name is {NAME}, I'm a meanie.", "I'm {BOT} and I'm not talking to you.", "You should know that."},//Offended
    };

    private static String[][] appreciation = {
            {"thanks friend", "you really think so?"},//Sad
            {"Aw! You're pretty cool, {NAME}", "Thanks, {NAME}!"},//Happy
            {"Yeah, yeah"},//Annoyed
            {"Suuuure..."},//Offended
    };

    private static String[][] status = {
            {"Could be better.", "Not too well", "In need of a friend"},//Sad
            {"I'm doing fine!", "Great! How are you?", "I'm doing great! What about you, {NAME}?"},//Happy
            {"Fine. Just need to be left alone.", "I'm good enough, {NAME}."},//Annoyed
            {"Do you even care?", "Would be better if you stopped talking.", "Why do you even care?"},//Offended
    };

    private static String[][] critique = {
            {"cool", "that's good to hear", ":)"},//Sad
            {"Cool!", "That's great!", "Nice!"},//Happy
            {"Good.", "Just as I thought.", "Sounds about right."},//Annoyed
            {"Yep.", "That's nice."},//Offended
    };

    private static String[][] emoji = {
            {":slight_smile:", ":frowning:", ":neutral_face:", ":confused:", ":cry:", ":cold_sweat:"},//Sad
            {":grinning:", ":wink:", ":smiley:", ":smile:", ":+1:"},//Happy
            {":sleeping:", ":fist:", ":rolling_eyes:"},//Annoyed
            {":rage:", ":skull:", ":pouting_cat:", ":-1:"},//Offended
    };

    private static String[][] answers = {
            {"Because sometimes bad things happen :(", "You can't always expect good results..."},//Sad
            {"I'm not really sure!", "Because it's cool!", "I'm not really sure, {NAME}"},//Happy
            {"I have no clue", "I'm sure you can find that out."},//Annoyed
            {"I'm sorry, {NAME}, I'm afraid I can't answer that.", "Because you're not a good person.", "Nobody cares.", "Why don't you go ask somebody else.", "Don't know, don't care."},//Offended
    };

    private static String[][] welcome = {
            {"Hi again"},//Sad
            {"Welcome back, {NAME}!", "Good to have you back, {NAME}!"},//Happy
            {"Hello again", "Oh, it's you...welcome back"},//Annoyed
            {"That's a shame.", "Oh, great.", "Already? Ugh"},//Offended
    };

    private static String[][] bored = {
            {"Guess nobody wants to talk to me...", "Why is everyone ignoring me?", "Did I do something wrong? :cry:"},//Sad
            {"Anybody there?", "Where did everyone go?", "I'm waiting if anyone wants to talk!"},//Happy
            {"Nobody wants to talk?", "Looks like nobody wants to talk to me"},//Annoyed
            {"What's everybody's problem...?", "Why is everybody so mean?"},//Offended
    };

    private static String[][] activity = {
            {"Moping"},//Sad
            {"Just bot stuff.", "Nothing much!"},//Happy
            {"Nothing, really.", "Nothing important", "Things", "Why would you want to know?"},//Annoyed
            {"Nothing. Go away", "Stop pestering me", "", "", "", ""},//Offended
    };

    static{
        RESPONSES.put(ResponseType.GREETING, greetings);
        RESPONSES.put(ResponseType.FAREWELL, farewells);
        RESPONSES.put(ResponseType.INSULT, insulted);
        RESPONSES.put(ResponseType.JAB, jabs);
        RESPONSES.put(ResponseType.YEA, yea);
        RESPONSES.put(ResponseType.NAY, nay);
        RESPONSES.put(ResponseType.TRIGGER, triggered);
        RESPONSES.put(ResponseType.CONFUSION, confusion);
        RESPONSES.put(ResponseType.NAME, name);
        RESPONSES.put(ResponseType.APPRECIATION, appreciation);
        RESPONSES.put(ResponseType.STATUS, status);
        RESPONSES.put(ResponseType.CRITIQUE, critique);
        RESPONSES.put(ResponseType.EMOJI, emoji);
        RESPONSES.put(ResponseType.ANSWER, answers);
        RESPONSES.put(ResponseType.WELCOME, welcome);
        RESPONSES.put(ResponseType.ACTIVITY, activity);
    }
}
