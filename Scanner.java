import java.util.*;
import java.io.IOException;

// Create the Scanner class
public class Scanner {
    // Class attributes
    StreamClass streamObject;
    List<String> listOfKeywords;
    int currentLine;
    int currentCharIndex;

    // Constructor that takes in a stream and a list of keywords.
    public Scanner(StreamClass stream, List<String> keywordlist){
        streamObject = stream;
        listOfKeywords = keywordlist;
        currentLine = 0;
        currentCharIndex = 0;
    }

    // Digit := 0 – 9
    // Check if a character falls in the ASCII range for a digit. Returns true if it does, false otherwise
    public boolean isDigit(char currChar) {
        boolean isDigitCheck = false;
        if (currChar >= '0' && currChar <= '9') {
            isDigitCheck = true;
        }
        return isDigitCheck;
    }

    // Alpha := A – Z | a – z
    // Check if a character falls in the ASCII range for an alphabetical character. Returns true if it does, false otherwise
    public boolean isAlpha(char currChar) {
        boolean isAlphaCheck = false;
        if (currChar >= 'A' && currChar <= 'Z' || currChar >= 'a' && currChar <= 'z') {
            isAlphaCheck = true;
        }
        return isAlphaCheck;
    }

    // WhiteSpace := Space | Tab | CarriageReturn | NewLine
    // Check if a character is a whitespace. Returns true if it does, false otherwise
    public boolean isWhiteSpace(char currChar) {
        boolean isWhiteSpaceCheck = false;
        if (currChar == ' ' || currChar == '\t' || currChar == '\r' || currChar == '\n') {
            isWhiteSpaceCheck = true;
        }
        return isWhiteSpaceCheck;
    }

    // CharacterLiteral := Space - ! | # - [ | ] - ~
    // Check if a character is a character literal. Returns true if it does, false otherwise
    public boolean isCharacterLiteral(char currChar) {
        boolean isCharacterLiteralCheck = false;
        if (currChar >= ' ' && currChar <= '!' || currChar >= '#' && currChar <= '[' || currChar >= ']' && currChar <= '~') {
            isCharacterLiteralCheck = true;
        }
        return isCharacterLiteralCheck;
    }

    // EscapedCharacter := \b | \n | \r | \t | \\ | \' | \"
    // Check if a character is an escaped character. Returns true if it does, false otherwise
    public boolean isEscapedCharacter(char currChar) {
        boolean isEscapedCharacterCheck = false;
        if (currChar == '\b' || currChar == '\n' || currChar == '\r' || currChar == '\t' || currChar == '\\' || currChar == '\'' || currChar == '\"') {
            isEscapedCharacterCheck = true;
        }
        return isEscapedCharacterCheck;
    }

    // Returns the next token without consuming it. If no more tokens are available a None token is returned. 
    public Token peekNextToken() {
        return null;
    }

    // Returns the next token and consumes it. If no more tokens are available a None token is returned.
    public Token getNextToken() {
        return null;
    }

    // Print out the token's line number, character position, token type and token text
    public void printToken (Token tokenToPrint) {
        System.out.println("@\t" + Integer.toString(tokenToPrint.getLineNumber()) + ",\t"
                            + Integer.toString(tokenToPrint.getCharPosition()) + "\t"
                            + tokenToPrint.getType().toString() + " " 
                            + "\"" + tokenToPrint.getText() + "\""); 
    }

    public static void main(String[] args) throws IOException {
        // If the user did not specify a file or put more than one file
        if (args.length != 1) {
            throw new IllegalArgumentException("Please enter the name of the file that you wish to use.");
        }

        // Get the name of the file inputted from the user in the command line
        String inputtedFileName = args[0];

        // Create a list of strings based on some of the Java keywords
        List<String> keywordlist = Arrays.asList("abstract", "assert", "boolean", "case", "char", "catch", "continue", "else", "final", "short");
        StreamClass stream = new StreamClass(inputtedFileName);
        Scanner scannerObject = new Scanner(stream, keywordlist);

        Token MyToken = new Token("a", Token.TokenType.IDENTIFIER, 1, 5);
        scannerObject.printToken(MyToken);

    }
}