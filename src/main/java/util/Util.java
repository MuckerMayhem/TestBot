package util;

public class Util{

    /**
     * Parses an array of Strings, grouping together words in quotation marks.<br>
     * Incomplete quotations are parsed literally. For example, the arguments<br>
     * <pre>
     *     one" "two" "three
     * </pre>
     * would be parsed as
     * <pre>
     *     [one", two, "three]
     * </pre>
     * The following are some other examples:
     * <br>
     * <table border="1">
     *     <tr>
     *         <th>Given arguments</th>
     *         <th>Result</th>
     *     </tr>
     *     <tr>
     *         <td>one two three</td>
     *         <td>[one, two, three]</td>
     *     </tr>
     *     <tr>
     *         <td>"one two" "three"</td>
     *         <td>[one two, three]</td>
     *     </tr>
     *     <tr>
     *         <td>"one two" three</td>
     *         <td>[one two, three]</td>
     *     </tr>
     *     <tr>
     *         <td>one" "two three"</td>
     *         <td>[one", two three]</td>
     *     </tr>
     *     <tr>
     *         <td>"one "two "three</td>
     *         <td>["one, "two, "three]</td>
     *     </tr>
     *     <tr>
     *         <td>"one</td>
     *         <td>["one]</td>
     *     </tr>
     *     <tr>
     *         <td>one"</td>
     *         <td>[one"]</td>
     *     </tr>
     *     <tr>
     *         <td>one</td>
     *         <td>[one]</td>
     *     </tr>
     * </table>
     * @param args Arguments to parse
     * @return A new array containing the parsed arguments
     */
    public static String[] parseQuotes(String[] args){
        int length = 0;
        int quoted = 0;
        boolean counting = true;
        for(String s : args){
            if(s.startsWith("\"") && !s.endsWith("\"")){//Has begin quote
                length++;
                if(!counting){//If not currently counting each word (already had begin quote)
                    length += quoted;
                    quoted = 0;
                }
                counting = false;
            }
            else if(s.endsWith("\"") && !s.startsWith("\"")){//Has end quote
                if(counting) length++;
                quoted = 0;
                counting = true;
            }
            else if(counting) length++;//Has no quotes, not in quotes
            else quoted++;//Has no quotes, in quotes
        }
        if(!counting) length += quoted;

        String[] newArgs = new String[length];

        StringBuilder builder = new StringBuilder();
        int index = 0;
        int words = 0;
        boolean quotes = false;
        for(String s : args){
            if(s.startsWith("\"") && !s.endsWith("\"")){//Has begin quote
                if(quotes){
                    if(builder.length() > 0){
                        newArgs[index] = builder.toString();
                        builder.setLength(0);
                        index++;
                    }
                }

                builder.append(s);
                words++;

                quotes = true;
            }
            else if(s.endsWith("\"") && !s.startsWith("\"")){//Has end quote
                if(words > 0){
                    builder.append(" ");
                }
                builder.append(s);
                if(words > 0){
                    String arg = builder.toString();
                    newArgs[index] = arg.substring(1, arg.length()).substring(0, arg.length() - 2);//Remove begin and end quote
                }
                else newArgs[index] = builder.toString();

                builder.setLength(0);
                index++;
                words = 0;

                quotes = false;
            }
            else if(quotes){//In quotes
                builder.append(" ").append(s);
            }
            else{
                newArgs[index] = s;
                index++;
            }
        }
        if(quotes)
            for(String s : builder.toString().split(" ")){
                newArgs[index] = s;
                index++;
            }

        return newArgs;
    }
    
    public static String realNewLines(String input){
        return input.replace("\\n", "\n");
    }
    
    /**
     * Returns true if array <i>a1</i> contains all strings from array <i>a2</i>
     * Case-insensitive
     * @param a1 First array
     * @param a2 Second array
     * @return Whether array a1 contains all Strings from a2
     */
    public static boolean containsAll(String[] a1, String... a2){
        int count = 0;
        for(String s : a1){
            for(String s1 : a2){
                if(s.equalsIgnoreCase(s1)) count++;
            }
        }
        return count == a2.length;
    }

    public static boolean containsAny(String a1[], String... a2){
        for(String s : a1){
            for(String s1: a2){
                if(s.equalsIgnoreCase(s1)) return true;
            }
        }
        return false;
    }

    /**
     * Returns true if array <i>a</i> contains string <i>toFind</i>
     * Case-insensitive
     * @param a Array to search
     * @param toFind String to search for
     * @return Whether the array contains the specified String
     */
    public static boolean contains(String[] a, String toFind){
        for(String s : a){
            if(s.equalsIgnoreCase(toFind)) return true;
        }
        return false;
    }
}
