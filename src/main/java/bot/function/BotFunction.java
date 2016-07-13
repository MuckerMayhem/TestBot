package bot.function;

import bot.DiscordBot;

import java.util.ArrayList;
import java.util.List;

public abstract class BotFunction{

    private static ArrayList<BotFunction> global_functions = new ArrayList<>();

    private String name;
    private Class<? extends BotFunction> mainClass;

    public static List<BotFunction> getAllRegisteredFunctions(){
        return global_functions;
    }

    public static BotFunction registerFunction(String name, Class<? extends BotFunction> mainClass){
        BotFunction instance;
        try{
            instance = mainClass.newInstance();
            instance.name = name;
            instance.mainClass = mainClass;

            instance.onRegister();

            global_functions.add(instance);
        }
        catch(InstantiationException | IllegalAccessException e){
            System.err.print("Failed to register function '" + name + "': " + e.getClass().getSimpleName());
            return null;
        }

        return instance;
    }

    public abstract void onRegister();

    public abstract void onEnable(DiscordBot bot);

    public abstract void onDisable(DiscordBot bot);

    public String getName(){
        return this.name;
    }
}
