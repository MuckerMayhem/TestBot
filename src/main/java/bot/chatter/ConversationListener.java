package bot.chatter;

import bot.DiscordBot;
import bot.behavior.BotAttitude;
import bot.behavior.Mood;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

public class ConversationListener
{
    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event) throws RateLimitException, DiscordException, MissingPermissionsException
    {
        IMessage imessage = event.getMessage();

        if(!imessage.getContent().startsWith(DiscordBot.instance.getCommandHandler().getCommandPrefix()))
        {
            BotAttitude attitude = BotAttitude.getBotFor(imessage.getAuthor().getID());
            if (attitude == null)
            {
                attitude = new BotAttitude(imessage.getAuthor().getID());
                attitude.setDefaultMood(Mood.HAPPY);//Debug
            }

            MessageBuilder builder = new MessageBuilder(DiscordBot.instance.getClient());

            if (event.getMessage().getChannel().getName().equals("bot")){
                builder.withChannel(event.getMessage().getChannel());

                String message = event.getMessage().getContent();
                String clientName = event.getMessage().getAuthor().getName();

                String response = attitude.talkTo(message);

                if (response != null){
                    response = response.replaceAll("\\{SENDER\\}", clientName);
                    builder.appendContent(response).build();
                }
            }
        }

    }
}
