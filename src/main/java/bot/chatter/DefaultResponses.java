package bot.chatter;

import java.util.Random;

public enum DefaultResponses{
    HELLO(new String[] {"hello", "hello!", "hi", "hi!"}, new String[] {"Hi there!", "Hey!", "fak u bich"}),
    SUP(new String[] {"what's up?"}, new String[] {"Nothing much.", "Just bot stuff.", "ur mom"}),
    NAME(new String[] {"what's your name?", "what is your name?"}, new String[] {"I have no name.", "My name is {SENDER}"});

    private String[] triggers;
    private String[] responses;

    DefaultResponses(String[] triggers, String[] responses){
        this.triggers = triggers;
        this.responses = responses;
    }

    public static String getResponseFor(String trigger){
        for(DefaultResponses d : values()){
            for(String s : d.triggers){
                if(trigger.equalsIgnoreCase(s)) return d.responses[new Random().nextInt(d.responses.length)];
            }
        }
        return null;
    }

    public String[] getTriggers(){
        return this.triggers;
    }

    public String[] getResponses(){
        return this.responses;
    }
}
