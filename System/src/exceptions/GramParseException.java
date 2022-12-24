package exceptions;
import Token.*;


public class GramParseException extends Exception {
    String mess;
    private GramParseException from;
    private Token curToken;
    private int level;

    @Override
    public String getMessage() {
        return this.mess;
    }

    public GramParseException getFrom() {
        return from;
    }

    @Override
    public String toString() {
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < level; i++) {
            temp.append(' ');
        }
        temp.append(curToken.getLine() + " " + curToken.getValue() + " in level:" + level + "\t" + mess +"\n");
        return temp.toString();
    }

    public Token getCurToken() {
        return curToken;
    }
}
