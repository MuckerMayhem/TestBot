package bot;

import util.Util;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ArgumentParser{

    private final TreeMap<String, Argument> validArgs = new TreeMap<>();
    
    public ArgumentParser() {};

    /**
     * Adds an additional argument to this parser. The parser will look for the argument when parsing.
     * @param name Name of the argument as it should be in <code>args[]</code>, such as <i>-myvalue</i> or <i>--myflag</i>
     * @param priority Priority of this argument. Arguments with a higher priority (lower number) are executed first
     * @param argument Argument to be added to this parser
     * @return This ArgumentParser, for chaining
     * @see Argument
     */
    public ArgumentParser withArgument(String name, int priority, Argument argument){
        argument.priority = priority;
        this.validArgs.put(name, argument);
        return this;
    }

    /**
     * Adds an additional argument to this parser. The parser will look for the argument when parsing.
     * @param name Name of the argument as it should be in <code>args[]</code>, such as <i>-myvalue</i> or <i>--myflag</i>
     * @param argument Argument to be added to this parser
     * @return This ArgumentParser, for chaining
     * @see Argument
     */
    public ArgumentParser withArgument(String name, Argument argument){
        argument.priority = validArgs.size();
        this.validArgs.put(name, argument);
        return this;
    }

    /**
     * Parses an array of arguments, calling {@link Argument#handle(Object)} on each in order of priority
     * @param args Arguments to parse
     */
    public void parse(String[] args){
        Iterator<String> iterator = Arrays.stream(Util.parseQuotes(args)).iterator();
        
        HashMap<Argument, Object> values = new HashMap<>();
        
        while(iterator.hasNext()){
            String s = iterator.next();
            if(validArgs.containsKey(s)){
                Argument argument = validArgs.get(s);
                values.put(argument, argument.parse(iterator));
            }
        }
        
        for(Argument a : values.keySet().stream().sorted().collect(Collectors.toList())){//Sort based on priority and iterate
            a.handle(values.get(a));
        }
    }

    /**
     * Represents a command-line argument
     */
    public static abstract class Argument implements Comparable<Argument>{
        private int priority = 0;
        
        public abstract void handle(Object value);
        
        public abstract Object parse(Iterator<String> argsIterator);
        
        @Override
        public int compareTo(@Nonnull Argument other){
            return this.priority - other.priority;
        }
    }

    /**
     * Represents a command-line argument flag. A flag is an argument that<br>
     * represents a boolean value (<code>true</code> if the flag exists, otherwise <code>false</code>.)
     */
    public static abstract class Flag extends Argument{
        @Override
        public Boolean parse(Iterator<String> argsIterator){
            return true;
        }
    }

    /**
     * Represents a command-line argument with a value.<br>
     * The value is always interpreted as a string.
     */
    public static abstract class Value extends Argument{
        @Override
        public String parse(Iterator<String> argsIterator){
            if(argsIterator.hasNext())
                return argsIterator.next();
            else
                return null;
        }
    }
}
