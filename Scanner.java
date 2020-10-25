import java.util.*;
import java.io.IOException;

// Create the Scanner class
public class Scanner {
    // Class attributes
    StreamClass streamObject;
    List<String> listOfKeywords;
    int currentLine;
    int currentCharIndex;
    static final String[] stringOfOperators = new String[] {"(", ",", ")", "{", "}", "=", "==", "<", ">", "<=", ">=", "!=", "+", "-", "*", "/", ";"};
    static final Set<String> setOfOperators = new HashSet<>(Arrays.asList(stringOfOperators));

    // Constructor that takes in a stream and a list of keywords.
    public Scanner(StreamClass stream, List<String> keywordlist){
        streamObject = stream;
        listOfKeywords = keywordlist;
        currentLine = 0;
        currentCharIndex = 0;
    }

    // Identifier := ( _ | Alpha ) { ( _ | Digit | Alpha ) }
    // Check if the string entered is an identifier. Returns true if it is an identifier, false otherwise. 
    public boolean isIdentifier (String currString) {
        // Need at least one character to be a proper indentifier
        if (currString.length() < 1) {
            return false;
        }

        boolean isIndentifierCheck;
        char firstIndex = currString.charAt(0);
        if (firstIndex != '_' || !isAlpha(firstIndex)) {
            isIndentifierCheck = false;
        } else {
            for (int i = 0 ; i < currString.length(); i++) {
                char currChar = currString.charAt(i);
                if (currChar != '_' || !isDigit(currChar) || !isAlpha(currChar)) {
                    isIndentifierCheck = false;
                }
            }
            isIndentifierCheck = true;
        }
        return isIndentifierCheck;
    }

    // Check if the string entered is a keyword. Returns true if it is an keyword, false otherwise. 
    public boolean isKeyword (String currString) { 
        boolean isKeywordCheck = false;
        if (listOfKeywords.contains(currString)) {
            isKeywordCheck = true;
        }
        return isKeywordCheck;
    }

    // Operator := ( | , | ) | { | } | = | == | < | > | <= | >= | != | + | - | * | / | ;
    // Check if character or string is an operator. Can take in either char or string. Will return true if it is an operator, false otherwise.
    // If 0 is passed in for charOrString, that means we passed in a character. If 1, that means we passed in a string. 
    public boolean isOperator(char currChar, String currString, int charOrString) {
        boolean isOperatorCheck = false;
        if (charOrString == 0) {
            String charAsString = Character.toString(currChar);
            isOperatorCheck = setOfOperators.contains(charAsString);
        } else if (charOrString == 1) {
            isOperatorCheck = setOfOperators.contains(currString);
        }
        return isOperatorCheck;
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

        // Create a list of strings based on keywords from project 2 description
        List<String> keywordlist = Arrays.asList("unsigned", "char", "short", "int", "long", "float", "double", "while", "if", "return", "void", "main");
        StreamClass stream = new StreamClass(inputtedFileName);
        Scanner scannerObject = new Scanner(stream, keywordlist);

        Token MyToken = new Token("a", Token.TokenType.IDENTIFIER, 1, 5);
        scannerObject.printToken(MyToken);

    }
}