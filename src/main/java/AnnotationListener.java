import com.github.kaioru.instructability.Defaults;
import com.github.kaioru.instructability.discord4j.Discord4JCommand;
import com.google.code.chatterbotapi.ChatterBotSession;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.util.MessageBuilder;

import java.util.LinkedList;


public class AnnotationListener extends Discord4JCommand
{

    private final ChatterBotSession bot1session;

    public AnnotationListener(ChatterBotSession bot1session)
    {
        this.bot1session = bot1session;
    }

    @Override
    public String getName()
    {
        return "demo";
    }

    @Override
    public String getDesc()
    {
        return Defaults.DESCRIPTION;
    }

    @Override
    public void execute(LinkedList<String> args, MessageReceivedEvent event, MessageBuilder msg) throws Exception
    {
        msg.appendContent("Cleverbot says: " + bot1session.think("Hello."))
                .build();
    }
}

