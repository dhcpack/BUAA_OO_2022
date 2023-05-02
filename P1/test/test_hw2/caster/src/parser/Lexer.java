package parser;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@code Lexer} class represents a lexer tool. It translate origin {@code sentence} into 
 * standardized characters defined in {@link Lexer.Type} .
 * 
 * The most important method is {@code next()} and {@code peek()} , which make this object to 
 * move to the next token, and get current token, respectively.
 */
public class Lexer {
    
    public enum Type {
        ERR,
        EOL,
        CONST,
        VAR,
        LPAREN,
        RPAREN,
        PLUS,
        MINUS,
        STAR,
        COMMA,
        EXP,
        FUNCT,
        SIN,
        COS,
        SUM,
        CIRCI,
        EQUAL,
    }

    private static final Pattern TOKEN_PATTERN = Pattern.compile(
        "(?<EQUAL>=)|(?<PLUS>\\+)|(?<MINUS>-)|(?<EXP>\\*\\*)|(?<STAR>\\*)|(?<COMMA>,)|" + 
        "(?<CONST>\\d+)|(?<VAR>[xyz])|(?<FUNCT>[fgh])|(?<SIN>sin)|(?<COS>cos)|" +
        "(?<SUM>sum)|(?<CIRCI>i)|(?<LPAREN>\\()|(?<RPAREN>\\))|(?<ERR>.)|(?<EOL>.)"
    );

    private String sentence;

    private ArrayList<Token> tokens = new ArrayList<>();

    private int pos;
    
    private Token currentToken;
    
    public Lexer(String sentence) {
        this.sentence = sentence;
        this.regulation();
        this.extractTokens();
        this.pos = 0;
        this.currentToken = (Token)this.tokens.get(0);
    }
    
    void next() {
        if (!this.reachEnd()) {
            ++pos;
            currentToken = tokens.get(pos);
        }
    }
    
    Token peek() {
        return currentToken;
    }
    
    boolean reachEnd() {
        return ((Token)this.tokens.get(this.pos)).getType() == Lexer.Type.EOL;
    }
    
    private void extractTokens() {
        Matcher matcher = TOKEN_PATTERN.matcher(sentence);
        
        while (matcher.find()) {
            
            Lexer.Type[] var2 = Lexer.Type.values();
    
            for (Type var5 : var2) {
                if (matcher.group(var5.toString()) != null) {
                    this.tokens.add(new Token(var5, matcher.group(var5.toString())));
                }
            }
        }
        
        this.tokens.add(new Token(Lexer.Type.EOL, ""));
    }

    private void regulation() {
        sentence = sentence.trim().replace(" ", "").replace('\t' + "", "");
        while (
                        sentence.contains("++")
                        || sentence.contains("--")
                        || sentence.contains("+-")
                        || sentence.contains("-+")
        ) {
            sentence = sentence.replace("++", "+")
                    .replace("--", "+")
                    .replace("+-", "-")
                    .replace("-+", "-");
        }
    }
}
