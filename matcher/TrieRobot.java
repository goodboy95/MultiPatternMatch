package matcher;

public class TrieRobot {
    private CharNode curCharNode;
    private boolean hadGap;
    public TrieRobot(CharNode node) {
        curCharNode = node;
        hadGap = false;
    }

    public CharNode getCurCharNode() {
        return curCharNode;
    }

    public void setCurCharNode(CharNode curCharNode) {
        this.curCharNode = curCharNode;
    }

    public boolean isHadGap() {
        return hadGap;
    }

    public void setHadGap() {
        this.hadGap = true;
    }

    
}
