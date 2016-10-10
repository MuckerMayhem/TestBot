package bot.feature.command.sound;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public enum Sound{

    QUACK("quack", new File("sound/quack.mp3"), "duck"),
    BOOTY("booty", new File("sound/booty.mp3"), "(͡°͜ʖ͡°)"),
    BOOTY2X(null, new File("sound/booty2x.wav")),
    BOOTY3X(null, new File("sound/booty3x.wav")),
    BOOTYSLOW(null, new File("sound/bootyslow.mp3")),
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
    BUBBERDUCK("bubberduck", new File("sound/bubberduck.mp3"), "bubberducky"),
    BONETROUSLE("bones", new File("sound/bonetrousle.mp3")),
    BONES("bonel", new File("sound/bones.mp3")),
    HOLYSHIT("holyshit", new File("sound/HolyShit_F.wav")),
    ANUS("anus", new File("sound/anus.mp3"), "myanusisbleeding"),
    SPOON("spoon", new File("sound/spoon.mp3"), "myspoonistoobig", "myspoonstoobig"),
    MURICA("murica", new File("sound/murica.mp3")),
    NAVY_SEAL("navyseal", new File("sound/navyseal.mp3"), "whatthefuckdidyoujustfuckingsayaboutmeyoulittlebitchillhaveyouknowigraduatedtopofmyclassinthenavysealsandivebeeninvolvedinnumeroussecretraidsonalquaedaandihaveover300confirmedkillsiamtrainedingorillawarfareandimthetopsniperintheentireusarmedforcesyouarenothingtomebutjustanothertargetiwillwipeyouthefuckoutwithprecisionthelikesofwhichhasneverbeenseenbeforeonthisearthmarkmyfuckingwordsyouthinkyoucangetawaywithsayingthatshittomeovertheinternetthinkagainfuckeraswespeakiamcontactingmysecretnetworkofspiesacrosstheusaandyouripisbeingtracedrightnowsoyoubetterprepareforthestormmaggotthestormthatwipesoutthepatheticlittlethingyoucallyourlifeyourefuckingdeadkidicanbeanywhereanytimeandicankillyouinoversevenhundredwaysandthatsjustwithmybarehandsnotonlyamiextensivelytrainedinunarmedcombatbutihaveaccesstotheentirearsenaloftheunitedstatesmarinecorpsandiwilluseittoitsfullextenttowipeyourmiserableassoffthefaceofthecontinentyoulittleshitifonlyyoucouldhaveknownwhatunholyretributionyourlittleclevercommentwasabouttobringdownuponyoumaybeyouwouldhaveheldyourfuckingtonguebutyoucouldntyoudidntandnowyourepayingthepriceyougoddamnidiotiwillshitfuryalloveryouandyouwilldrowninityourefuckingdeadkiddo"),
    DYLAN("dylan", new File("sound/dylan.wav")),
    DINOS("dinos", new File("sound/dinos.wav")),
    FAIL("fail", new File("sound/fail.mp3")),
    GOODSHIT("goodshit", new File("sound/goodshit.mp3"), "\uD83D\uDC4C\uD83D\uDC40\uD83D\uDC4C\uD83D\uDC40\uD83D\uDC4C\uD83D\uDC40\uD83D\uDC4C\uD83D\uDC40\uD83D\uDC4C\uD83D\uDC40goodshitgo౦ԁsHit\uD83D\uDC4Cthats✔somegood\uD83D\uDC4C\uD83D\uDC4Cshitright\uD83D\uDC4C\uD83D\uDC4Cthere\uD83D\uDC4C\uD83D\uDC4C\uD83D\uDC4Cright✔there✔✔ifidoƽaүsomyself\uD83D\uDCAFisayso\uD83D\uDCAFthatswhatimtalkingaboutrightthererightthere(chorus:ʳᶦᵍʰᵗᵗʰᵉʳᵉ)mMMMMᎷМ\uD83D\uDCAF\uD83D\uDC4C\uD83D\uDC4C\uD83D\uDC4CНO0ОଠOOOOOОଠଠOoooᵒᵒᵒᵒᵒᵒᵒᵒᵒ\uD83D\uDC4C\uD83D\uDC4C\uD83D\uDC4C\uD83D\uDC4C\uD83D\uDCAF\uD83D\uDC4C\uD83D\uDC40\uD83D\uDC40\uD83D\uDC40\uD83D\uDC4C\uD83D\uDC4CGoodshit"),
    KEEM("keem", new File("sound/keem.mp3"), "cancer", "keemstar"),
    NOPE("nope", new File("sound/engineer_no01.mp3")),
    YERAWIZARD("yerawizard", new File("sound/yerawizard.mp3")),
    MAGNUMDONG("magnumdong", new File("sound/magnumdong.mp3"), "monstercondom"),
    NOOT("noot", new File("sound/noot.mp3")),
    NYAT("nyat", new File("sound/NYAT.mp3")),
    FUCKINGNOOT("fuckingnoot", new File("sound/fuckingnoot.mp3")),
    WOT("wot", new File("sound/wot.mp3")),
    HEYBABY("heybaby", new File("sound/heybaby.mp3")),
    HEYBABYR("ybabyeh", new File("sound/heybaby3.mp3")),
    HEYBABYDEMONIC("heybab", new File("sound/heybaby2.mp3")),
    KNOCK("knock", new File("sound/knock.mp3")),
    IWILLKILLYOU("iwillkillyou", new File("sound/iwillkillyou.mp3")),
    NERFTHIS("nerfthis", new File("sound/nerfthis.mp3"));

    private final String name;
    private final String[] aliases;

    private String url;
    private File path;

    /**
     * Registers a new sound from a URL<br>
     * <i>Note: Sound must be in .wav format to work. If you want to register<br>
     * a sound that is in .mp3 format you must do it from a file.</i>
     *
     * @param name    Name to be used when playing this sound
     * @param url     URL pointing to the sound
     * @param aliases Aliases that can be used to play this sound
     */
    Sound(String name, String url, String... aliases){
        this.name = name; this.url = url; this.aliases = aliases;
    }

    /**
     * Registers a new sound from a file. <i>Must be in .mp3 or .wav format</i>
     *
     * @param name    Name to be used when playing this sound
     * @param path    Path pointing to the sound
     * @param aliases Aliases that can be used to play this sound
     */
    Sound(String name, File path, String... aliases){
        this.name = name; this.aliases = aliases; this.path = path;
    }

    /**
     * Finds a sound with the specified name or alias.<br>
     * Will return null if no sound exists with this name or alias.
     *
     * @param name Name or alias of the sound you wish to look for
     * @return Sound the specified name or alias, or null if no Sound was found
     */
    public static Sound get(String name){
        for(Sound s : values()){
            if(s.name == null) continue;
            if(s.getName().equalsIgnoreCase(name)) return s;
            for(String a : s.aliases){
                if(a.equalsIgnoreCase(name)) return s;
            }
        } return null;
    }

    public String getName(){
        return this.name;
    }

    public String[] getAliases(){
        return this.aliases;
    }

    public URL getUrl() throws MalformedURLException{
        return new URL(this.url);
    }
    
    public File getPath(){
        return this.path;
    }
}
