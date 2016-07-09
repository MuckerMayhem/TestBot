package bot.commands;

import bot.DiscordBot;
import bot.InputBot;
import bot.settings.ArraySetting;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class CommandClear extends Command{

    private static final String[] ALLOWED = {"bottest", "bot", "music-bot"};
    private static final ArraySetting WHITELIST_CLEAR = new ArraySetting("clear_whitelist", "List of channels the clear command can be used in", ALLOWED);

    @Override
    protected void onRegister(){
        DiscordBot.getGlobalSettingsHandler().registerNewSetting(WHITELIST_CLEAR);
    }

    @Override
    protected void onExecute(DiscordBot bot, IMessage message, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException{
        String[] whitelist = (String[]) DiscordBot.getGlobalSettingsHandler().getUserSetting(null, WHITELIST_CLEAR);

        if(!Arrays.asList(whitelist).contains(message.getChannel().getName())){
            bot.say(message.getChannel(), "You can't do this here!", 3000L);
            return;
        }

        if(!(bot instanceof InputBot)){
            System.err.print("Non-input bot tried to execute command requiring user input. The nerve!");
            return;
        }

        InputBot inputBot = (InputBot) bot;

        inputBot.say(message.getChannel(), "Are you sure you want to do this? This will clear **ALL MESSAGES** in this channel!");
        inputBot.say(message.getChannel(), "Type *yes* if you are okay with this.");
        if(inputBot.nextLine(message.getChannel()).equalsIgnoreCase("yes")){
            inputBot.say(message.getChannel(), "Are you ***ABSOLUTELY SURE*** that you wish to do this? Deleted messages can *NOT* be recovered!");
            inputBot.say(message.getChannel(), "Type *Yes, I do* if you are completely sure that you want to delete **__ALL MESSAGES__** in this channel.");
            if(inputBot.nextLine(message.getChannel()).equals("Yes, I do")){
                IMessage botMessage = inputBot.say(message.getChannel(), "Deleting all messages in channel *" + message.getChannel().getName() + "*...");
                deleteMessages(inputBot, message.getChannel(), 0);
                return;
            }
        }

        inputBot.say(message.getChannel(), "Cancelled.", 3500L);
    }

    public void deleteMessages(DiscordBot bot, IChannel channel, int count){
        int size = channel.getMessages().size();
        final int newCount = count + (size < 50 ? size : 50);
        TimerTask task = new TimerTask(){
            @Override
            public void run(){
                try{
                    channel.getMessages().deleteFromRange(0, size < 50 ? size - 1 : 49);
                }
                catch(RateLimitException | DiscordException | MissingPermissionsException e){
                    e.printStackTrace();
                }
                if(size > 50)
                    deleteMessages(bot, channel, newCount);
                else
                    bot.say(channel, "Deleted " + newCount + " messages", 3500L);
            }
        };
        if(size > 0)
            new Timer().schedule(task, 1000L);
    }
}
