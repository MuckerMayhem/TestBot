package bot.commands;

import bot.chatter.DefaultResponses;
import com.github.kaioru.instructability.discord4j.Discord4JCommand;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.util.MessageBuilder;

import java.util.LinkedList;

public class CommandTest extends Discord4JCommand{

    MessageReceivedEvent last = null;

    @Override
    public String getName(){
        return "bot, ";
    }

    @Override
    public String getDesc(){
        return "";
    }

    @Override
    public void execute(LinkedList<String> linkedList, MessageReceivedEvent event, MessageBuilder builder) throws Exception{
        if(this.last != null && this.last.getMessage().equals(event.getMessage())) return;

        String message = event.getMessage().getContent();
        String clientName = event.getClient().getOurUser().getName();

        StringBuilder stringBuilder = new StringBuilder();
        linkedList.forEach(s -> stringBuilder.append(s).append(" "));
        System.out.println(stringBuilder.toString().trim());

        String response = DefaultResponses.getResponseFor(stringBuilder.toString().trim().replaceAll("\\{SENDER\\}", clientName));

        if(response != null){
            builder.appendContent(response).build();
        }

        this.last = event;
    }
}
