package plc.project;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.List;


/**
 * The lexer works through three main functions:
 *
 *  - {@link #lex()}, which repeatedly calls lexToken() and skips whitespace
 *  - {@link #lexToken()}, which lexes the next token
 *  - {@link CharStream}, which manages the state of the lexer and literals
 *
 * If the lexer fails to parse something (such as an unterminated string) you
 * should throw a {@link ParseException} with an index at the character which is
 * invalid.
 *
 * The {@link #peek(String...)} and {@link #match(String...)} functions are * helpers you need to use, they will make the implementation a lot easier. */
public final class Lexer {


    private final CharStream chars;


    public Lexer(String input) {
        chars = new CharStream(input);
    }


    /**
     * Repeatedly lexes the input using {@link #lexToken()}, also skipping over
     * whitespace where appropriate.
     */
    public List<Token> lex() {
        throw new UnsupportedOperationException(); //TODO
    }


    /**
     * This method determines the type of the next token, delegating to the
     * appropriate lex method. As such, it is best for this method to not change
     * the state of the char stream (thus, use peek not match).
     *
     * The next character should start a valid token since whitespace is handled
     * by {@link #lex()}
     */
    public Token lexToken() {
        // Skip whitespace characters??


        char Char = chars.get(0);

        //if (Char == '@' || Character.isLetter(Char)) {
        // Identifier or string (starting with '@')
        return lexCharacter();

        //}

        // If none of the above conditions match, throw a ParseException
        //throw new UnsupportedOperationException();
    }

    public Token lexDecimal() {
        String input = chars.input.toString();

        // Define a regular expression pattern for a valid decimal number
        String decimalPattern = "([-]?[1-9][0-9]*\\.[0-9]+|[0-9]+\\.[0-9]+)";

        // Check if the input matches the decimal pattern
        if (input.matches("^" + decimalPattern)) {
            return new Token(Token.Type.DECIMAL, input, 0);
        } else {
            return new Token(Token.Type.DECIMAL, "Invalid decimal", 0);
        }
    }




    public Token lexIdentifier() {

        String input = chars.input.toString();
        String temp = "";
        int index = input.length();
        for (int i = 0; i < input.length(); i++) {
            if (i==0){
                if (match("(@|[A-Za-z])")){
                    temp += input.charAt(i);
                }
                else{

                }
            }

            else{
                if (match("[A-Za-z0-9_-]*")){
                    temp += input.charAt(i);
                }
                else{

                }
            }

        }
        return new Token(Token.Type.IDENTIFIER, temp, 0);
    }


    public Token lexNumber() {
        String input = chars.input.toString();
        String temp = "";
        int index = input.length();
        for (int i = 0; i < input.length(); i++) {
            if (i ==0) {
                if (match("[-0-9]")) {
                    temp += input.charAt(i);
                    if (input.charAt(0) == '0'){
                        return new Token(Token.Type.INTEGER, temp, 0);
                    }
                    if (input.charAt(0) == '-'){
                        if (match("[1-9]")){
                            temp += input.charAt(i+1);
                        }
                    }
                }
            }
            else{
                if (match("[-0-9]")) {
                    temp += input.charAt(i);
                }
            }
        }
        return new Token(Token.Type.INTEGER, temp, 0);
    }


    public Token lexCharacter() {
        String input = chars.input.toString();

        // Check if the input matches the character pattern
        if (input.matches("^'([^'\n\r\\\\]|\\\\.)'$")) {
            return new Token(Token.Type.CHARACTER, input, 0);
        } else {
            return new Token(Token.Type.CHARACTER, "Invalid character", 0);
        }
    }



    public Token lexString() {
        System.out.println("entered lexString");
        throw new UnsupportedOperationException(); //TODO
    }


    public void lexEscape() {
        System.out.println("entered lexEscape");
        throw new UnsupportedOperationException(); //TODO
    }


    public Token lexOperator() {
        System.out.println("entered lexOperator");
        throw new UnsupportedOperationException(); //TODO
    }


    /**
     * Returns true if the next sequence of characters match the given patterns,
     * which should be a regex. For example, {@code peek("a", "b", "c")} would
     * return true if the next characters are {@code 'a', 'b', 'c'}.
     */
    public boolean peek(String... patterns) {
        for (int i=0; i < patterns.length; i++){
            if (!chars.has(i) ||
                    !String.valueOf(chars.get(i)).matches(patterns[i])){

                return false;
            }
        }
        return true;
    }


    /**
     * Returns true in the same way as {@link #peek(String...)}, but also
     * advances the character stream past all matched characters if peek returns
     * true. Hint - it's easiest to have this method simply call peek.
     */
    public boolean match(String... patterns) {
        boolean peek = peek(patterns);
        if (peek){
            for (int i = 0; i < patterns.length; i++){
                chars.advance();
            }
        }
        return peek;
    }


    /**
     * A helper class maintaining the input string, current index of the char
     * stream, and the current length of the token being matched.
     *
     * You should rely on peek/match for state management in nearly all cases.
     * The only field you need to access is {@link #index} for any {@link
     * ParseException} which is thrown.
     */
    public static final class CharStream {


        private final String input;
        private int index = 0;
        private int length = 0;


        public CharStream(String input) {
            this.input = input;
        }


        public boolean has(int offset) {
            return index + offset < input.length();
        }


        public char get(int offset) {
            return input.charAt(index + offset);
        }


        public void advance() {
            index++;
            length++;
        }


        public void skip() {
            length = 0;
        }


        public Token emit(Token.Type type) {
            int start = index - length;
            skip();
            return new Token(type, input.substring(start, index), start);
        }


    }


}
