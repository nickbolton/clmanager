package net.deuce.clmanager.util;

public class Utils {

    public static boolean isEqual(Object o1, Object o2) {
        if (o1 == o2) return true;
        if (o1 == null || o2 == null) return false;
        if (o1.hashCode() != o2.hashCode()) return false;
        return o1.equals(o2);
    }

}
