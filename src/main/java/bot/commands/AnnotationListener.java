package bot.commands;

import bot.NewBot;
import bot.chatter.DefaultResponses;
import sx.blah.discord.api.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.HTTP429Exception;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;

public class AnnotationListener {

    @EventSubscriber
    public void onReady(ReadyEvent event) throws HTTP429Exception, DiscordException{
        NewBot.getClient().changeUsername("Botty McBotface");
    }

    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event) throws HTTP429Exception, DiscordException, MissingPermissionsException{
        MessageBuilder builder = new MessageBuilder(NewBot.getClient());

        if(event.getMessage().getChannel().getName().equals("bot")){
            builder.withChannel(event.getMessage().getChannel());
            System.out.println("Client spoke in bot channel");

            String message = event.getMessage().getContent();
            String clientName = event.getMessage().getClient().getOurUser().getName();

            String response = DefaultResponses.getResponseFor(message);

            if(response != null){
                response = response.replaceAll("\\{SENDER\\}", clientName);
                builder.appendContent(response).build();
            }
        }
    }
}
