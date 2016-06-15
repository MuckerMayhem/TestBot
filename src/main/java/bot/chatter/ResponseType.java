package bot.chatter;

import util.Util;

import java.util.Random;

public enum ResponseType{

    GREETING(0.1F, 0.0F),
    FAREWELL(0.1F, 0.0F),
    INSULT(-0.25F, 0.2F),
    JAB(-0.05F, 0.1F),
    QUESTION(0.0F, 0.05F),
    CONFUSION(0.0F, 0.08F),
    TRIGGER(-0.3F, 0.4F),
    NAME(0.1F, 0.0F),
    APPRECIATION(0.2F, 0.05F),
    YEA(0.0F, 0.0F),
    NAY(0.0F, 0.0F),
    STATUS(0.1F, 0.1F),
    CRITIQUE(0.0F, 0.0F),
    EMOJI(0.025F, -0.05F),
    ANSWER(0.0F, 0.0F),
    EXPLANATORY(0.0F, 0.0F),
    WELCOME(0.2F, 0.0F),
    ACTIVITY(0.0F, 0.0F);

    private float happiness;
    private float annoyance;

    private static String[] greetings = {"hi", "hello", "hey", "hola", "hiya", "hoi", "sup"};
    private static String[] farewells = {"cya", "bye", "go", "leave"};
    private static String[] aggressions = {"fucking", "fuck", "damn", "suck", "dick", "hate", "mean", "meanie", "stupid", "dumb", "fag", "faggot", "gay", "die"};
    private static String[] triggers = {"ice", "cream", "furry", "furries"};
    private static String[] compliments = {"love", "cool", "dandy", "neat"};
    private static String[] yea = {"yes", "yeah", "yea", "yep", "aye", "affirmative", "mhmm", "alright", "allright"};
    private static String[] nay = {"no", "nope", "nada", "negative"};
    private static String[] interrogative = {"how", "did", "is", "am", "do", "are", "can"};
    private static String[] explanatory = {"why", "how"};

    ResponseType(float happinessGain, float annoyanceGain){
        this.happiness = happinessGain;
        this.annoyance = annoyanceGain;
    }

    public static ResponseType generateType(String input){
        String[] split = input.split(" ");

        int count = split.length;

        if(input.equalsIgnoreCase("whats up")) return ACTIVITY;

        if(count == 1){
            if(isEmoji(split[0])) return EMOJI;
        }

        if(isExplanatory(split[0])) return EXPLANATORY;
        boolean question = isInterrogative(split[0]);

        int yes = 0;
        int no = 0;

        int aggression = 0;
        int offense = 0;
        int farewell = 0;
        int compliment = 0;

        if(Util.containsAll(split, "why") && !question) return ANSWER;
        if(Util.containsAll(split, "you", "fuck")) return INSULT;
        if(Util.containsAll(split, "who", "you") || Util.containsAll(split, "your", "name")) return NAME;
        if(Util.containsAll(split, "how", "are", "you")) return STATUS;
        if(Util.containsAny(split, "i", "im") && Util.containsAny(split, "return", "back") && !(Util.containsAny(split, "ill", "will", "going"))) return WELCOME;

        for(String s : split){
            if(isPositiveResponse(s)) yes++;
            if(isNegativeResponse(s)) no++;
            if(isAggressive(s)) aggression++;
            if(isTrigger(s)) offense++;
            if(isFarewell(s)) farewell++;
            if(isCompliment(s)) compliment++;
            if(isGreeting(s)) return GREETING;
        }

        if(Util.containsAll(split, "you") && compliment >= 1){
            if(question) return YEA;
            return APPRECIATION;
        }

        if(farewell / (float) count > 0.666){
            if(question) return NAY;
            return FAREWELL;
        }
        if(offense / (float) count > 0.100){
            if(question) return NAY;
            return TRIGGER;
        }
        if(aggression / (float) count > 0.333){
            if(question) return INSULT;
            return JAB;
        }

        if(yes / (float) count >= 0.32 && no == 0) return CRITIQUE;
        else if(no / (float) count >= 0.32 && yes == 0) return CRITIQUE;
        else if(question) return new Random().nextInt(2) == 0 ? YEA : NAY;
        else return ResponseType.CONFUSION;
    }

    public static boolean isGreeting(String string){
        for(String s : greetings){
            if(s.equalsIgnoreCase(string)) return true;
        }
        return false;
    }

    public static boolean isFarewell(String string){
        for(String s : farewells){
            if(s.equalsIgnoreCase(string)) return true;
        }
        return false;
    }

    public static boolean isTrigger(String string){
        for(String s : triggers){
            if(s.equalsIgnoreCase(string)) return true;
        }
        return false;
    }

    public static boolean isAggressive(String string){
        for(String s : aggressions){
            if(s.equalsIgnoreCase(string)) return true;
        }
        return false;
    }

    public static boolean isCompliment(String string){
        for(String s : compliments){
            if(s.equalsIgnoreCase(string)) return true;
        }
        return false;
    }

    public static boolean isPositiveResponse(String string){
        for(String s : yea){
            if(s.equalsIgnoreCase(string)) return true;
        }
        return false;
    }

    public static boolean isNegativeResponse(String string){
        for(String s : nay){
            if(s.equalsIgnoreCase(string)) return true;
        }
        return false;
    }

    public static boolean isInterrogative(String string){
        for(String s : interrogative){
            if(s.equalsIgnoreCase(string)) return true;
        }
        return false;
    }

    public static boolean isExplanatory(String string){
        for(String s : explanatory){
            if(s.equalsIgnoreCase(string)) return true;
        }
        return false;
    }

    public static boolean isEmoji(String string){
        //Pattern stolen from http://stackoverflow.com/questions/30767631/check-if-emoji-character
        return string.matches("([\\u20a0-\\u32ff\\ud83c\\udc00-\\ud83d\\udeff\\udbb9\\udce5-\\udbb9\\udcee])");
    }

    public float getHappiness(){
        return this.happiness;
    }

    public float getAnnoyance(){
        return this.annoyance;
    }
}
