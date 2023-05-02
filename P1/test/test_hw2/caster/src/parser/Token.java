package parser;

/**
 * {@code Token} class represents a token object in the origin sentence.
 */
public class Token {

    private Lexer.Type type;

    private String str;

    Token() {
        type = Lexer.Type.ERR;
        str = "";
    }

    Token(Lexer.Type type, String str) {
        this.str = str;
        this.type = type;
    }

    Lexer.Type getType() {
        return type;
    }

    String getStr() {
        return str;
    }
    
}
