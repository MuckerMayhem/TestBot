package bot.settings;

import bot.settings.SettingsHandler.Setting;

public class Settings{

    public static final Settings DEFAULT = new Settings(false);

    private boolean seeWelcomeNotifications;
    private boolean seeWaifuNotifications;
    private boolean allowEmojiEating;
    private boolean allowWallBreaking;

    private boolean canModify;

    public Settings(){
        this.seeWelcomeNotifications = true;
        this.seeWaifuNotifications   = true;
        this.allowEmojiEating        = true;
        this.allowWallBreaking       = true;
        this.canModify               = true;
    }

    private Settings(boolean canModify){
        this();
        this.canModify = canModify;
    }

    public boolean get(Setting setting){
        switch(setting){
            case SEE_WELCOME_NOTIFICATIONS: return this.seeWelcomeNotifications;
            case SEE_WAIFU_NOTIFICATIONS:   return this.seeWaifuNotifications;
            case ALLOW_EMOJI_EATING:        return this.allowEmojiEating;
            case ALLOW_WALL_BREAKING:       return this.allowWallBreaking;
            default: return setting.getDefault();
        }
    }

    public void set(Setting setting, boolean value){
        if(!this.canModify) return;

        switch(setting){
            case SEE_WELCOME_NOTIFICATIONS:
                this.seeWelcomeNotifications = value;
                break;
            case SEE_WAIFU_NOTIFICATIONS:
                this.seeWaifuNotifications = value;
                break;
            case ALLOW_EMOJI_EATING:
                this.allowEmojiEating = value;
                break;
            case ALLOW_WALL_BREAKING:
                this.allowWallBreaking = value;
                break;
        }
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder("```");

        int longest = 0;
        for(Setting s : Setting.values()){
            if(s.getName().length() > longest) longest = s.getName().length();
        }
        longest += 3;

        for(Setting s : Setting.values()){
            builder.append(String.format("%-" + (get(s) ? longest : longest - 1) + "s", s.getName() + ":"))//lol
                    .append(get(s))
                    .append(" | ")
                    .append(s.getDescription())
                    .append(" (Default: ")
                    .append(s.getDefault())
                    .append(")")
                    .append("\n");
        }

        return builder.append("```").toString();
    }
}
