package matcher;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

public class Matcher {
    static CharNode root = new CharNode('\0');

    public static void AddPatterns(List<String> patternList) {
        for (String word : patternList) {
            CharNode curNode = root;
            String[] sections = word.split("\\.\\*");
            int sectionsCount = sections.length;
            for (int i = 0; i < sectionsCount; i++) {
                String section = sections[i];
                int sectLen = section.length();
                for (int j = 0; j < sectLen; j++) {
                    char curChar = section.charAt(j);
                    curNode = curNode.AddAndReturnChildNode(curChar, j == 0);
                    if (i == sectionsCount - 1 && j == sectLen - 1) {
                        curNode.setMatchedString(word);
                    }
                }
            }
        }
    }

    /**
     * Match the given text with all stored patterns.
     * @param text The given text.
     * @param longTextMode if the text is long(over 30 chars for example), and the stored patterns contains many RegEx patterns(1000 entries for example), using longTextMode can increase the matching speed.
     * @return
     */
    public static Optional<String> Match(String text, boolean longTextMode) {
        int len = text.length();
        if (len == 0) {
            return Optional.empty();
        }
        // if longTextMode param is true, construct the "Remaining Character Table" to filter useless robots
        HashSet<Character> remainChar = null;
        HashMap<Character, Integer> remainCharNum = null;
        if (longTextMode) {
            remainChar = new HashSet<>();
            remainCharNum = new HashMap<>();
            for (int i = 0; i < len; i++) {
                char curChar = text.charAt(i);
                if (!remainChar.contains(curChar)) {
                    remainChar.add(curChar);
                    remainCharNum.put(curChar, 1);
                } else {
                    remainCharNum.put(curChar, remainCharNum.get(curChar) + 1);
                }
            }
        }
        // start searching
        Queue<TrieRobot> robotQueue = new LinkedList<>();
        robotQueue.offer(new TrieRobot(root));
        for (int i = 0; i < len; i++) {
            int queueLen = robotQueue.size();
            while (queueLen-- > 0) {
                TrieRobot robot = robotQueue.poll();
                CharNode node = robot.getCurCharNode();
                String matchedStr = node.getMatchedString();
                if (matchedStr != null) {
                    return Optional.of(matchedStr);
                }
                if (i < len) {
                    char curChar = text.charAt(i);
                    if (longTextMode) {
                        if (remainChar.contains(curChar)) {
                            int curCharRemain = remainCharNum.get(curChar);
                            if (--curCharRemain == 0) {
                                remainChar.remove(curChar);
                            } else {
                                remainCharNum.put(curChar, curCharRemain);
                            }
                        }
                    }
                    HashMap<Character, CharNode> consecutiveChild = node.getConsecutiveChild();
                    HashMap<Character, CharNode> gapChild = node.getGapChild();
                    if (!robot.isHadGap() && consecutiveChild.containsKey(curChar)) {
                        robotQueue.offer(new TrieRobot(consecutiveChild.get(curChar)));
                    }
                    if (gapChild.containsKey(curChar)) {
                        robotQueue.offer(new TrieRobot(gapChild.get(curChar)));
                    }
                    // Here is a magic number "10", which I used as a default value for LongTextMode, maybe modified as a configured value in the following version.
                    // In LongTextMode, if a node where a robot locates on has over 10 gapChildren, the robot won't be obsoleted.
                    // Otherwise, the robot will be obsoleted if gapChild doesn't contain any char of the remaining chars in the given text.
                    // In ShortTextMode, if gapChild of a node has no elements, the robot staying at this node will be obsoleted.
                    int gapChildCount = gapChild.size();
                    boolean reserveRobot = longTextMode 
                        ? (gapChildCount > 10 || Utility.IsOverlap(remainChar, gapChild.keySet()))
                        : gapChildCount > 0;
                    if (reserveRobot) {
                        robot.setHadGap();
                        robotQueue.offer(robot);
                    }
                }
            }
        }
        return Optional.empty();
    }
}
