
import com.github.kaioru.instructability.Instructables;
import com.github.kaioru.instructability.command.Command;
import com.github.kaioru.instructability.discord4j.InstructabilityModule;
import com.google.code.chatterbotapi.ChatterBot;
import com.google.code.chatterbotapi.ChatterBotFactory;
import com.google.code.chatterbotapi.ChatterBotSession;
import com.google.code.chatterbotapi.ChatterBotType;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.IListener;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.util.DiscordException;


public class NewBot
{
    static IDiscordClient client;

    public static void main(String[] args) throws Exception
    {
        ChatterBotFactory factory = new ChatterBotFactory();

        ChatterBot bot1 = factory.create(ChatterBotType.CLEVERBOT);
        ChatterBotSession bot1session = bot1.createSession();

        Instructables.getRegistry().registerCommand(new AnnotationListener(bot1session));

        client = new ClientBuilder().withToken("MTkxMzk3NDkzNDE2Nzg3OTY4.Cj5sTg.-gE8kl3iH_c7NSUp17zwzJ9-K-w").login();
        client.getModuleLoader().loadModule(new InstructabilityModule());

        client.getDispatcher().registerListener((IListener<ReadyEvent>) event ->
                Instructables.getRegistry().setPrefix("!")
        );


    }
}


