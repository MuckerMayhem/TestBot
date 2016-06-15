package util;

public class Util{

    /**
     * Returns true if array <i>a1</i> contains all strings from array <i>a2</i>
     * Case-insensitive
     * @param a1
     * @param a2
     * @return
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
     * @param a
     * @param toFind
     * @return
     */
    public static boolean contains(String[] a, String toFind){
        for(String s : a){
            if(s.equalsIgnoreCase(toFind)) return true;
        }
        return false;
    }
}
