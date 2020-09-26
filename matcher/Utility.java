package matcher;

import java.util.HashSet;
import java.util.Set;

public class Utility {
    public static <T> boolean IsOverlap(HashSet<T> set1, Set<T> set2) {
        for (T element2 : set2) {
            if (set1.contains(element2)) {
                return true;
            }
        }
        return false;
    }
}
