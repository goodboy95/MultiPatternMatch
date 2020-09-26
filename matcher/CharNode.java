package matcher;

import java.util.HashMap;

public class CharNode {
    private String matchedString;
    private HashMap<Character, CharNode> consecutiveChild;
    private HashMap<Character, CharNode> gapChild;
    private char currentChar;

    public CharNode(char c) {
        consecutiveChild = new HashMap<Character, CharNode>();
        gapChild = new HashMap<Character, CharNode>();
        currentChar = c;
    }

    /**
     * Add a childNode representing char c in current node (if not exists), and return the child node.
     * @param c
     * @param isGap
     * @return
     */
    public CharNode AddAndReturnChildNode(char c, boolean isGap) {
        HashMap<Character, CharNode> operatingMap = isGap ? gapChild : consecutiveChild;
        if (!operatingMap.containsKey(c)) {
            operatingMap.put(c, new CharNode(c));
        }
        return operatingMap.get(c);
    }

    public String getMatchedString() {
        return matchedString;
    }

    public void setMatchedString(String matchedString) {
        this.matchedString = matchedString;
    }

    public HashMap<Character, CharNode> getConsecutiveChild() {
        return consecutiveChild;
    }

    public HashMap<Character, CharNode> getGapChild() {
        return gapChild;
    }

    public char getCurrentChar() {
        return currentChar;
    }
}
