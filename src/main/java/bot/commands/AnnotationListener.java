package bot.commands;

import bot.NewBot;
import bot.chatter.DefaultResponses;
import bot.chatterbotapi.ChatterBotSession;
import sx.blah.discord.api.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.HTTP429Exception;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;

import java.util.List;

public class AnnotationListener
{

    ChatterBotSession session;
    List<ChatterBotSession> list;

    public AnnotationListener(ChatterBotSession session)
    {
        this.session = session;
    }

    public AnnotationListener(List<ChatterBotSession> list)
    {
        this.list = list;
    }

    public AnnotationListener()
    {

    }

//    @EventSubscriber
//    public void onJoin(UserJoinEvent event) throws HTTP429Exception, DiscordException
//    {
//        list.add()
//    }

    @EventSubscriber
    public void onReady(ReadyEvent event) throws HTTP429Exception, DiscordException
    {
        NewBot.getClient().changeUsername("Daddy");
    }

    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event) throws HTTP429Exception, DiscordException, MissingPermissionsException, Exception
    {
        MessageBuilder builder = new MessageBuilder(NewBot.getClient());

        if (event.getMessage().getChannel().getName().equals("bot2"))
        {
            builder.withChannel(event.getMessage().getChannel());

            String message = event.getMessage().getContent();
            String clientName = event.getMessage().getAuthor().getName();

            String response = DefaultResponses.getResponseFor(message.replaceAll("[.!?']+", ""));

            if (response != null)
            {
                response = session.think(message);
                builder.appendContent(response).build();
            }
            response = session.think(message);
            builder.appendContent(response).build();
            //            try
            //            {
            //                Thread.sleep(4500);
            //            } catch(InterruptedException ex)
            //            {
            //                Thread.currentThread().interrupt();
            //            }
        }
    }

//    @EventSubscriber
//    public void onMsgRecieved(MessageReceivedEvent event) throws HTTP429Exception, DiscordException
}
