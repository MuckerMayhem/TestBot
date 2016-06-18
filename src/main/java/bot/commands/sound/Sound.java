package bot.commands.sound;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public enum Sound
{

    QUACK("quack", new File("sound/quack.mp3"), "duck"),
    BOOTY("booty", new File("sound/booty"), "(͡°͜ʖ͡°)"),
    HIGHNOON("highnoon", new File("sound/highnoon.mp3"), "whattimeisit"),
    LOL("lol", new File("sound/lol.mp3")),
    SOGOOD("sogood", new File("sound/sogood.mp3")),
    ALLAHU("allahuakbar", new File("sound/allahuakbar.mp3"), "jihad", "allahu"),
    YEAH("raymanyeah", new File("sound/raymanyeah.mp3")),
    DUMMY("dummy", new File("sound/dummy.mp3")),
    BALLSOFSTEEL("ballsofsteel", new File("sound/ballsofsteel.mp3")),
    BALLS("balls", new File("sound/balls.mp3")),
    MACHO1("circle", new File("sound/macho_alert16.mp3")),
    MACHO2("getyou", new File("sound/macho_alert13.mp3")),
    ALIVE("alive", new File("sound/alive.mp3")),
    BUBBERDUCK("bubberduck", new File("sound/bubberduck.mp3")),
    BONETROUSLE("bones", new File("sound/bonetrousle.mp3")),
    BONES("bonel", new File("sound/bones.mp3")),
    HOLYSHIT("holyshit", new File("sound/HolyShit_F.wav"));
    private String name;
    private String[] aliases;

    private String url;
    private File path;

    Sound(String name, String url, String... aliases)
    {
        this.name = name;
        this.url = url;
        this.aliases = aliases;
    }

    @Deprecated
    Sound(String name, File path, String... aliases)
    {
        this.name = name;
        this.aliases = aliases;
        this.path = path;
    }

    public static Sound get(String name)
    {
        for (Sound s : values())
        {
            if (s.getName().equalsIgnoreCase(name)) return s;
            for (String a : s.aliases)
            {
                if (a.equalsIgnoreCase(name)) return s;
            }
        }
        return null;
    }

    public String getName()
    {
        return this.name;
    }

    public String[] getAliases()
    {
        return this.aliases;
    }

    public URL getUrl() throws MalformedURLException
    {
        return new URL(this.url);
    }

    @Deprecated
    public File getPath()
    {
        return this.path;
    }
}
