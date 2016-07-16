package bot;

import gui.BotGui;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.GuildCreateEvent;
import sx.blah.discord.handle.impl.events.GuildLeaveEvent;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.Image;
import sx.blah.discord.util.RateLimitException;

import java.io.File;
import java.util.ArrayList;

public class EventListener{

    private static final String NAME = "Weeb-bot";//Bot's username
    private static final Image IMAGE = Image.forFile(new File("kirino.png"));//Bot's avatar
    private static final String GAME = "俺の妹がこんなに可愛いわけがないポータブル";//Game the bot will be displayed as playing

    private static ArrayList<IGuild> guildQueue = new ArrayList<>();

    @EventSubscriber
    public void onReady(ReadyEvent event){
        IDiscordClient client = event.getClient();
        
        try{
            client.changeUsername(NAME);
        }
        catch(DiscordException | RateLimitException e){
            System.err.println("Exception occurred while trying to change client username: " + e.getMessage());
        }

        try{
            client.changeAvatar(IMAGE);
        }
        catch(DiscordException | RateLimitException e){
            System.err.println("Exception occurred while trying to change client avatar: " + e.getMessage());
        }

        client.changeGameStatus(GAME);

        System.out.println("Client ready. Initializing DiscordBot instances...");

        for(IGuild g : guildQueue){
            new DiscordBot(g);
            System.out.printf("Joined guild '%s' (%s)\n", g.getName(), g.getID());
        }
    }

    @EventSubscriber
    public void onGuildCreate(GuildCreateEvent event){
        guildQueue.add(event.getGuild());
    }
    
    @EventSubscriber
    public void onGuildRemove(GuildLeaveEvent event){
        BotGui.getGui().getGuildPanel().update();
    }
}
