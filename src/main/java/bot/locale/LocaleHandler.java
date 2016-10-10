package bot.locale;

import bot.feature.FeatureSet;
import bot.feature.command.BotCommand;
import bot.feature.function.BotFunction;
import bot.settings.Setting;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class LocaleHandler{

    private static final LinkedList<String> sources = new LinkedList<>();
    
    private static final HashMap<Locale, LocaleHandler> locales = new HashMap<>();
    
    private static final String COMMAND = "command.";
    private static final String FUNCTION = "function.";
    private static final String SET = "set.";
    private static final String MESSAGE = "message.";
    private static final String SETTING = "setting.";
    
    private static final String NAME = ".name";
    private static final String DESCRIPTION = ".desc";
    private static final String ARGUMENTS = ".args";
    
    private static final String PRETTY = ".pretty";
    private static final String DETAIL = ".detail";

    private Locale locale;

    private HashMap<String, String> values = new HashMap<>();
    
    static{
        sources.add(new File("lang").getAbsolutePath());
    }
    
    public static void addExternalSource(File folder){
        sources.add(folder.getAbsolutePath());
    }
    
    public static LocaleHandler load(Locale locale) throws FileNotFoundException{
        
        LocaleHandler localeHandler = new LocaleHandler();
        localeHandler.locale = locale;

        HashMap<String, String> values = new HashMap<>();
        
        for(String s : sources){
            File source = new File(s);
            if(!source.isDirectory()) return null;
            
            File file = new File(source + File.separator + locale.getCode());
            if(!file.isDirectory()) return null;

            System.out.println("Loading localization files from directory " + file.getAbsolutePath());
            
            int ln;

            for(File f : file.listFiles((dir, name) -> {
                return name.toLowerCase().endsWith(".lang");
            })){

                System.out.println("Loading " + f.getName() + " for locale '" + locale.getCode() + "'");

                Scanner input = new Scanner(f);

                ln = 0;
                while(input.hasNextLine()){
                    String line = input.nextLine();
                    ln++;
                    if(line.isEmpty()) continue;

                    String[] split = line.split(":", 2);
                    try{
                        values.put(FilenameUtils.getBaseName(f.getName()) + "." + split[0], split[1]);
                    }
                    catch(ArrayIndexOutOfBoundsException e){
                        System.err.println("Invalid string found in file '" + f.getPath() + "' (Line: " + ln + ")");
                        values.put(split[0], "");
                    }
                }
                input.close();
            }
        }

        localeHandler.values = values;
        
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
        String key = MESSAGE + message.getName();
        
        if(this.locale == null)
            return key;

        return this.values.getOrDefault(key, key);
    }

    public String getLocalizedName(BotCommand command){
        String key = COMMAND + command.getRegisteredName() + NAME;
        
        if(this.locale == null)
            return key;
        
        return this.values.getOrDefault(key, key);
    }

    public String getPrettyName(BotCommand command){
        String key = COMMAND + command.getRegisteredName() + PRETTY;
        
        if(this.locale == null)
            return key;
        
        return this.values.getOrDefault(key, StringUtils.capitalize(getLocalizedName(command) + " command"));
    }
    
    public String getLocalizedName(BotFunction function){
        String key = FUNCTION + function.getRegisteredName() + NAME;
        
        if(this.locale == null)
            return key;
        
        return this.values.getOrDefault(key, key);
    }

    public String getLocalizedName(FeatureSet featureSet){
        String key = SET + featureSet.getRegisteredName() + NAME;
        
        if(this.locale == null)
            return key;
        
        return this.values.getOrDefault(key, key);
    }
    
    public String getLocalizedName(Setting setting){
        String key = SETTING + setting.getName() + NAME;
        
        if(this.locale == null)
            return key;
        
        return this.values.getOrDefault(key, key);
    }

    public String getLocalizedDescription(BotCommand command){
        String key = COMMAND + command.getRegisteredName() + DESCRIPTION;
        
        if(this.locale == null)
            return key;

        return this.values.getOrDefault(key, key);
    }

    public String getDetailedDescription(BotCommand command){
        String key = COMMAND + command.getRegisteredName() + DETAIL;
        
        if(this.locale == null)
            return key;
        
        return this.values.getOrDefault(key, getLocalizedMessage(Message.CMD_HELP_NO_DETAIL));
    }
    
    public String getLocalizedDescription(BotFunction function){
        String key = FUNCTION + function.getRegisteredName() + DESCRIPTION;
        
        if(this.locale == null)
            return key;
        
        return this.values.getOrDefault(key, key);
    }
    
    public String getLocalizedDescription(FeatureSet featureSet){
        String key = SET + featureSet.getRegisteredName() + DESCRIPTION;
        
        if(this.locale == null)
            return key;
        
        return this.values.getOrDefault(key, key);
    }
    
    public String getLocalizedDescription(Setting setting){
        String key = SETTING + setting.getName() + DESCRIPTION;
        
        if(this.locale == null)
            return key;

        return this.values.getOrDefault(key, key);
    }
    
    public String[] getLocalizedArguments(BotCommand command){
        String key = COMMAND + command.getRegisteredName() + ARGUMENTS;
        
        if(this.locale == null)
            return new String[0];
        
        return this.values.get(key).split(",");
    }
}
