package bot.locale;

import bot.feature.commands.BotCommand;
import bot.feature.commands.CommandHandler;
import bot.feature.function.BotFunction;
import bot.settings.Setting;
import bot.settings.SettingsHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class LocaleHandler{

    private static HashMap<Locale, LocaleHandler> locales = new HashMap<>();

    private Locale locale;

    private HashMap<BotCommand, String[]> commands = new HashMap<>();
    private HashMap<Setting, String[]> settings = new HashMap<>();
    private HashMap<BotFunction, String> functions = new HashMap<>();
    private HashMap<Message, String> messages = new HashMap<>();

    public static LocaleHandler load(Locale locale) throws FileNotFoundException{
        File file = new File("lang" + File.separator + locale.getCode());
        if(!file.isDirectory()) return null;

        LocaleHandler localeHandler = new LocaleHandler();
        localeHandler.locale = locale;

        HashMap<String, String> values = new HashMap<>();

        int ln = 0;

        File commands = new File(file + File.separator + "commands.lang");
        if(commands.exists()){
            System.out.println("Loading command strings for locale '" + locale.getCode() + "'");

            Scanner input = new Scanner(commands);

            ln = 0;
            while(input.hasNextLine()){
                String line = input.nextLine();
                ln++;
                if(line.isEmpty()) continue;

                String[] split = line.split(":");
                try{
                    values.put(split[0], split[1]);
                }
                catch(ArrayIndexOutOfBoundsException e){
                    System.err.println("Invalid string found in file '" + commands.getPath() + "' (Line: " + ln + ")");
                    values.put(split[0], "");
                }
            }
            input.close();
        }

        File functions = new File(file + File.separator + "functions.lang");
        if(functions.exists()){
            System.out.println("Loading function strings for locale '" + locale.getCode() + "'");

            Scanner input = new Scanner(functions);

            ln = 0;
            while(input.hasNextLine()){
                String line = input.nextLine();
                ln++;
                if(line.isEmpty()) continue;

                String[] split = line.split(":");
                try{
                    values.put(split[0], split[1]);
                }
                catch(ArrayIndexOutOfBoundsException e){
                    System.err.println("Invalid string found in file '" + functions.getPath() + "' (Line: " + ln + ")");
                    values.put(split[0], "");
                }
            }
            input.close();
        }

        File messages = new File(file + File.separator + "messages.lang");
        if(messages.exists()){
            System.out.println("Loading message strings for locale '" + locale.getCode() + "'");

            Scanner input = new Scanner(messages);

            ln = 0;
            while(input.hasNextLine()){
                String line = input.nextLine();
                ln++;
                if(line.isEmpty()) continue;

                String[] split = line.split(":");
                try{
                    values.put(split[0], split[1]);
                }
                catch(ArrayIndexOutOfBoundsException e){
                    System.err.println("Invalid string found in file '" + messages.getPath() + "' (Line: " + ln + ")");
                    values.put(split[0], "");
                }
            }
            input.close();
        }

        File settings = new File(file + File.separator + "settings.lang");
        if(settings.exists()){
            System.out.println("Loading setting strings for locale '" + locale.getCode() + "'");

            Scanner input = new Scanner(settings);

            while(input.hasNextLine()){
                String line = input.nextLine();
                if(line.isEmpty()) continue;

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

        Arrays.stream(Message.values()).forEach(m -> {
            String value = values.get(m.getName());
            localeHandler.messages.put(m, value == null ? m.getName() : value);
        });

        SettingsHandler.getAllRegisteredSettings().stream().forEach(s -> {
            String[] info = new String[2];

            String name = values.get(s.getName() + ".name");
            String desc = values.get(s.getName() + ".desc");

            info[0] = name == null ? s.getName() : name;
            info[1] = desc == null ? "setting." + s.getName() + ".desc" : desc;

            localeHandler.settings.put(s, info);
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

    public String getLocalizedMessage(Message message){
        if(this.locale == null)
            return message.getName();

        return this.messages.get(message);
    }

    public String getLocalizedName(BotCommand command){
        if(this.locale == null)
            return command.getRegisteredName();

        return this.commands.get(command)[0];
    }

    public String getLocalizedName(BotFunction function){
        if(this.locale == null)
            return "function." + function.getClass().getSimpleName().toLowerCase();

        return this.functions.get(function);
    }

    public String getLocalizedName(Setting setting){
        if(this.locale == null)
            return "setting." + setting.getName() + ".name";

        return this.settings.get(setting)[0];
    }

    public String getLocalizedDescription(BotCommand command){
        if(this.locale == null)
            return "command." + command.getRegisteredName() + ".desc";

        return this.commands.get(command)[1];
    }

    public String getLocalizedDescription(Setting setting){
        if(this.locale == null)
            return "setting." + setting.getName() + ".desc";

        return this.settings.get(setting)[1];
    }
}
