package bot.locale;

import bot.commands.Command;
import bot.commands.CommandHandler;
import bot.function.BotFunction;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class LocaleHandler{

    private static HashMap<Locale, LocaleHandler> locales = new HashMap<>();

    private Locale locale;

    private HashMap<Command, String[]> commands = new HashMap<>();
    private HashMap<BotFunction, String> functions = new HashMap<>();
    private HashMap<Message, String> messages = new HashMap<>();

    public static LocaleHandler load(Locale locale) throws FileNotFoundException{
        File file = new File("lang" + File.separator + locale.getCode());
        if(!file.isDirectory()) return null;

        LocaleHandler localeHandler = new LocaleHandler();
        localeHandler.locale = locale;

        HashMap<String, String> values = new HashMap<>();

        File commands = new File(file + File.separator + "commands.lang");
        if(commands.exists()){
            System.out.println("Loading command info for locale '" + locale.getCode() + "'");

            Scanner input = new Scanner(commands);

            while(input.hasNextLine()){
                String line = input.nextLine();

                String[] split = line.split(":");
                values.put(split[0], split[1]);
            }
        }

        File functions = new File(file + File.separator + "functions.lang");
        if(functions.exists()){
            System.out.println("Loading function names for locale '" + locale.getCode() + "'");

            Scanner input = new Scanner(functions);

            while(input.hasNextLine()){
                String line = input.nextLine();

                String[] split = line.split(":");
                values.put(split[0], split[1]);
            }
        }

        File messages = new File(file + File.separator + "messages.lang");
        if(messages.exists()){
            System.out.println("Loading messages for locale '" + locale.getCode() + "'");

            Scanner input = new Scanner(messages);

            while(input.hasNextLine()){
                String line = input.nextLine();

                String[] split = line.split(":");
                values.put(split[0], split[1]);
            }
        }

        CommandHandler.getAllRegisteredCommands().stream().forEach(c -> {
            String[] info = new String[2];

            String name = values.get(c.getRegisteredName() + ".name");
            String desc = values.get(c.getRegisteredName() + ".desc");

            info[0] = name == null ? c.getRegisteredName() : name;
            info[1] = desc == null ? "command." + c.getRegisteredName() + ".desc" : desc;

            localeHandler.commands.put(c, info);
        });

        return localeHandler;
    }

    public static LocaleHandler get(Locale locale){
        try{
            LocaleHandler handler;
            if(locales.containsKey(locale)){
                handler = locales.get(locale);
            }
            else{
                handler = load(locale);
                locales.put(locale, handler);
            }
            return handler;
        }
        catch(FileNotFoundException e){
            return new LocaleHandler();
        }
    }

    public Locale getLocale(){
        return this.locale;
    }

    public String getLocalizedName(Command command){
        if(this.locale == null)
            return command.getRegisteredName();

        return this.commands.get(command)[0];
    }

    public String getLocalizedName(BotFunction function){
        if(this.locale == null)
            return "function." + function.getClass().getSimpleName().toLowerCase();

        return this.functions.get(function);
    }

    public String getLocalizedDescription(Command command){
        if(this.locale == null)
            return "command." + command.getRegisteredName() + ".desc";

        return this.commands.get(command)[1];
    }

    public String getLocalizedMessage(Message message){
        return ":)";
    }
}
