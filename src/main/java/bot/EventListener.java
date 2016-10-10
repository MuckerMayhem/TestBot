package bot;

import gui.BotGui;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.GuildCreateEvent;
import sx.blah.discord.handle.impl.events.GuildLeaveEvent;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.Status;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RateLimitException;

import java.util.ArrayList;

public class EventListener{

    //https://discordapp.com/oauth2/authorize?client_id=195313542130302977&scope=bot&permissions=8 Bot authorization url

    //俺の妹がこんなに可愛いわけがないポータブル
    private static final ArrayList<IGuild> guildQueue = new ArrayList<>();

    @EventSubscriber
    public void onReady(ReadyEvent event){
        IDiscordClient client = event.getClient();
        client.getDispatcher().registerListener(this);
        
        if(!(DiscordBot.getUsername() == null || DiscordBot.getUsername().isEmpty())){
            try{
                client.changeUsername(DiscordBot.getUsername());
            }
            catch(DiscordException | RateLimitException e){
                System.err.println("Exception occurred while trying to change client username: " + e.getMessage());
            }
        }

        if(DiscordBot.getAvatarImg() != null){
            try{
                client.changeAvatar(DiscordBot.getAvatarImg());
            }
            catch(DiscordException | RateLimitException e){
                System.err.println("Exception occurred while trying to change client avatar: " + e.getMessage());
            }
        }
        
        if(DiscordBot.getGameStatus() != null){
            client.changeStatus(Status.game(DiscordBot.getGameStatus()));
        }
        
        System.out.println("Client ready. Initializing DiscordBot instances...");

        for(IGuild g : client.getGuilds()){
            new DiscordBot(g);
            System.out.printf("Joined guild '%s' (%s)\n", g.getName(), g.getID());
        }
        
        /*
        for(IGuild g : guildQueue){
            new DiscordBot(g);
            System.out.printf("Joined guild '%s' (%s)\n", g.getName(), g.getID());
        }
        */
        
        if(!BotGui.isDisabled()){
            BotGui.getGui().getGuildPanel().update();
        }
    }

    @EventSubscriber
    public void onGuildCreate(GuildCreateEvent event){
        guildQueue.add(event.getGuild());
    }
    
    @EventSubscriber
    public void onGuildRemove(GuildLeaveEvent event){
        if(!BotGui.isDisabled()){
            BotGui.getGui().getGuildPanel().update();
        }
    }
}
